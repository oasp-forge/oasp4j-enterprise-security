package org.sixgems.service.impl;

import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.iplanet.sso.SSOTokenManager;
import org.sixgems.model.api.SsoAccessToken;
import org.sixgems.model.impl.OpenAMAccessToken;
import org.sixgems.service.api.SsoTokenExtractorService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Julian on 30.06.2015.
 */
public class OpenAMSsoTokenExtractorService implements SsoTokenExtractorService{
    @Override
    public SsoAccessToken extractToken(HttpServletRequest request) {
        SSOToken token = getSsoTokenFromServletRequest(request);
        if (isTokenValid(token)){
            return new OpenAMAccessToken(token);
        }
        return null;
    }

    private SSOToken getSsoTokenFromServletRequest(HttpServletRequest request){
        SSOToken ssoToken = null;
        try{
            SSOTokenManager tokenManager = SSOTokenManager.getInstance();
            ssoToken = tokenManager.createSSOToken(request);
        }
        catch(SSOException ex){
            return null;
        }
        return ssoToken;
    }

    private boolean isTokenValid(SSOToken token) {

        boolean result = false;
        if (token == null)
            return false;
        try {
            SSOTokenManager tokenManager = SSOTokenManager.getInstance();
            result = tokenManager.isValidToken(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
