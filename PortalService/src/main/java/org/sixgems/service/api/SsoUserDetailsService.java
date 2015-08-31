package org.sixgems.service.api;

import com.iplanet.sso.SSOToken;
import org.sixgems.model.api.SsoAccessToken;
import org.sixgems.model.api.SsoUserDetails;
import org.sixgems.service.SsoUserDetailsCreationException;

/**
 * Created by Julian on 16.07.2015.
 */
public interface SsoUserDetailsService {

    /**
     * Generates SsoUserDetails from the SSOToken issued by OpenAM
     * @param ssoToken SSOToken issued by OpenAM as access-manager
     * @return
     * @throws SsoUserDetailsCreationException
     */
    SsoUserDetails getSsoUserDetailsFromSsoToken(SsoAccessToken ssoToken) throws SsoUserDetailsCreationException;
}
