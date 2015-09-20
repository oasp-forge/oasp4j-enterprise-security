package org.sixgems.service.impl;

import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.iplanet.sso.SSOTokenManager;
import com.netflix.zuul.context.RequestContext;
import com.sun.identity.authentication.AuthContext;
import com.sun.identity.authentication.spi.AuthLoginException;
import org.sixgems.service.SsoUserLogoutException;
import org.sixgems.service.api.LogoutService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Julian on 15.09.2015.
 */


public class OpenAMLogoutServiceImpl implements LogoutService{
    @Override
    public boolean logoutUser() throws SsoUserLogoutException{
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest req = ctx.getRequest();
        SSOToken token = getSsoTokenFromServletRequest(req);

        try{
            SSOTokenManager tokenManager = SSOTokenManager.getInstance();
            tokenManager.destroyToken(token);

        }catch(SSOException e) {
            throw new SsoUserLogoutException("Authentication Context of Login is not valid");
        }
        return true;
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
