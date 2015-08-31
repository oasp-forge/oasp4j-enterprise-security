package org.sixgems.model.impl;

import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.sun.identity.idm.AMIdentity;
import com.sun.identity.idm.IdRepoException;
import com.sun.identity.idm.IdUtils;
import org.sixgems.model.api.SsoAccessToken;
import org.sixgems.service.SsoUserDetailsCreationException;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

/**
 * Created by Julian on 23.07.2015.
 */
public class OpenAMAccessToken implements SsoAccessToken{


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
    public Collection<String> getUserGroups(String groupsProp) throws RuntimeException{
        return obtainUserGroups(this.ssoToken, groupsProp);
    }

    @Override
    public Map<String, Object> getUserAttributes(List<String> attrProps) throws RuntimeException{
        return obtainAdditionalAttributes(this.ssoToken, attrProps);
    }

    /**
     * Method for obtaining User Group information
     * @return
     */
    private Collection<String> obtainUserGroups(SSOToken ssoToken, String groupsProp){

        try{
            AMIdentity userIdentity = IdUtils.getIdentity(ssoToken);
            return userIdentity.getAttribute(groupsProp);
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
}
