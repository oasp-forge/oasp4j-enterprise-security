package org.sixgems.model.impl;

import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.sun.identity.idm.AMIdentity;
import com.sun.identity.idm.IdRepoException;
import com.sun.identity.idm.IdType;
import com.sun.identity.idm.IdUtils;
import org.sixgems.model.api.SsoAccessToken;
import org.sixgems.service.SsoUserDetailsCreationException;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

/**
 * Created by Julian on 23.07.2015.
 */
public class OpenAMAccessToken implements SsoAccessToken{

    private final String generalGroupName = "general";

    private SSOToken ssoToken;

    public OpenAMAccessToken(SSOToken ssoToken){
        this.ssoToken = ssoToken;
    }

    public String getTokenId(){
        if (this.ssoToken!=null){
            return ssoToken.getTokenID().toString();
        }
        return null;
    }

    @Override
    public String getUsername(String usernameProperty) throws RuntimeException{
        try{
            String userName = ssoToken.getProperty(usernameProperty);
            return userName;
        }
        catch(SSOException e){
            throw new RuntimeException("Unable to obtain username from token");
        }
    }

    @Override
    public Map<String, Collection<String>> getUserGroups() throws RuntimeException{
        Map<String, Collection<String>> userGroupsForApp = new HashMap<>();
        Collection<String> appGroups = obtainUserGroups(this.ssoToken);

        for (String appGroup: appGroups){
            String[] splittedAppAndGroup = getValidAppAndGroupName(appGroup);
            if (splittedAppAndGroup!=null){
                userGroupsForApp = addToUserGroupsMap(splittedAppAndGroup[0], splittedAppAndGroup[1], userGroupsForApp);
            }
        }

        return userGroupsForApp;
    }

    @Override
    public Map<String, Object> getUserAttributes(List<String> attrProps) throws RuntimeException{
        return obtainAdditionalAttributes(this.ssoToken, attrProps);
    }

    /**
     * Method for obtaining User Group information
     * @return
     */
    private Collection<String> obtainUserGroups(SSOToken ssoToken){
        Collection<String> groups= new ArrayList<>();
        try{
            AMIdentity userIdentity = IdUtils.getIdentity(ssoToken);
            Set<AMIdentity> amGroups = userIdentity.getMemberships(IdType.GROUP);
            for(AMIdentity amGroup : amGroups){
                if (amGroup.getType().equals(IdType.GROUP)){
                    groups.add(amGroup.getName());
                }
            }

            return groups;
        }
        catch(IdRepoException | SSOException e){
            throw new RuntimeException("Unable to obtain user groups");
        }
    }

    private Map<String, Object> obtainAdditionalAttributes(SSOToken token, List<String> attrProps){
        Map<String, Object> additionalAttributes = new HashMap<>();
        try{
            AMIdentity userIdentity = IdUtils.getIdentity(token);
            for (String attributeName: attrProps){
                Set attribute = userIdentity.getAttribute(attributeName);
                if (attribute!=null && !attribute.isEmpty()){
                    if (attribute.size()==1)
                        additionalAttributes.put(attributeName, attribute.iterator().next());
                    else
                        additionalAttributes.put(attributeName, attribute);
                }
            }
        }
        catch(IdRepoException | SSOException e){
            throw new RuntimeException("Unable to obtain user attributes from token");
        }

        return additionalAttributes;
    }

    /**
     * returns a String Array of size two. Index 0 is the Application name and Index 1 the Group name.
     * If the provided groupName has not the valid format ('APPNAME_GROUPNAME') null will be returned
     * @param groupName
     * @return
     */
    private String[] getValidAppAndGroupName (String groupName){
        String [] splitName = groupName.trim().split("_");
        return splitName.length==2 ? splitName : null;
    }

    /**
     * Adds an appName - groupName combination to the user groups Map
     * @param appName
     * @param groupName
     * @param currentMap
     * @return the provided currentMap plus the newly created entry
     */
    private Map<String, Collection<String>> addToUserGroupsMap (String appName, String groupName, Map<String, Collection<String>> currentMap){
        if (currentMap.containsKey(appName)){
            currentMap.get(appName).add(groupName);
        }
        else{
            Collection<String> groupList = new ArrayList<>();
            groupList.add(groupName);
            currentMap.put(appName, groupList);
        }

        return currentMap;
    }
}
