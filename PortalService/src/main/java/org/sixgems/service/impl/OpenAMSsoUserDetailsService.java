package org.sixgems.service.impl;

import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.sun.identity.idm.AMIdentity;
import com.sun.identity.idm.IdRepoException;
import com.sun.identity.idm.IdUtils;
import org.sixgems.model.impl.OpenAMSsoUser;
import org.sixgems.model.api.SsoUserDetails;
import org.sixgems.service.SsoUserDetailsCreationException;
import org.sixgems.service.api.SsoUserDetailsService;
import org.springframework.beans.factory.annotation.Value;

import java.security.Principal;
import java.util.*;

/**
 * Created by Julian on 16.07.2015.
 */
public class OpenAMSsoUserDetailsService implements SsoUserDetailsService{

    @Value("${openam.ssotoken.attributes.groupsAttribute}")
    private String groupsAttributeName;

    @Value("#{'${openam.ssotoken.attributes.additionalAttributes}'.split(',')}")
    private List<String> additionalAttributeNames;

    @Value("${openam.ssotoken.properties.username}")
    private String usernameProperty;

    @Override
    public SsoUserDetails getSsoUserDetailsFromSsoToken(SSOToken token) throws SsoUserDetailsCreationException {
        if (token!=null){
            try{
                //Principal ssoPrincipal = token.getPrincipal();
                String userName = token.getProperty(usernameProperty);
                String principalUser = userName;

                //get UserIdentity for User specific Attributes like groups
                AMIdentity userIdentity = IdUtils.getIdentity(token);
                Collection<String> groups = obtainUserGroups(userIdentity);
                Map<String, Object> attributes = obtainAdditionalAttributes(userIdentity);

                return new OpenAMSsoUser(principalUser, userName, groups, attributes);

            }catch (SSOException | IllegalArgumentException | IdRepoException e){
                throw new SsoUserDetailsCreationException();
            }
        }
        throw new SsoUserDetailsCreationException();
    }

    private Collection<String> obtainUserGroups(AMIdentity identity){
        try{
            return identity.getAttribute(groupsAttributeName);
        }
        catch(IdRepoException | SSOException e){
            return new LinkedList<String>();
        }
    }

    private Map<String, Object> obtainAdditionalAttributes(AMIdentity identity){
        Map<String, Object> additionalAttributes = new HashMap<>();
        try{
            for (String attributeName: additionalAttributeNames){
                Set attribute = identity.getAttribute(attributeName);
                if (attribute!=null && !attribute.isEmpty()){
                    if (attribute.size()==1)
                        additionalAttributes.put(attributeName, attribute.iterator().next());
                    else
                        additionalAttributes.put(attributeName, attribute);
                }
            }
        }
        catch(IdRepoException | SSOException e){
            return new HashMap<>();
        }

        return additionalAttributes;
    }

}
