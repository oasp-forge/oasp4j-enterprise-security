package org.sixgems.service.impl;

import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.sun.identity.idm.AMIdentity;
import com.sun.identity.idm.IdRepoException;
import com.sun.identity.idm.IdUtils;
import org.sixgems.model.api.SsoAccessToken;
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
public class DefaultSsoUserDetailsService implements SsoUserDetailsService{

    @Value("#{'${openam.ssotoken.attributes.additionalAttributes}'.split(',')}")
    private List<String> additionalAttributeNames;

    @Value("${openam.ssotoken.properties.username}")
    private String usernameProperty;

    @Override
    public SsoUserDetails getSsoUserDetailsFromSsoToken(SsoAccessToken token) throws SsoUserDetailsCreationException {
        if (token!=null){
            try{

                String userName = token.getUsername(usernameProperty);
                String principalUser = userName;

                Map<String, Collection<String>> groups = token.getUserGroups();
                Map<String, Object> attributes = token.getUserAttributes(additionalAttributeNames);

                return new OpenAMSsoUser(principalUser, userName, groups, attributes);

            }catch (RuntimeException e){
                throw new SsoUserDetailsCreationException("Failed to extract user Details from SSOToken", e);
            }
        }
        throw new SsoUserDetailsCreationException("SsoAccessToken must be present");
    }

}
