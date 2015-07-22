package org.sixgems.filter;

import com.iplanet.sso.SSOToken;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.sixgems.model.api.SsoUserDetails;
import org.sixgems.model.impl.JwtTokenSessionHolder;
import org.sixgems.service.SsoUserDetailsCreationException;
import org.sixgems.service.api.JwtTokenService;
import org.sixgems.service.api.SsoTokenExtractorService;
import org.sixgems.service.api.SsoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Julian on 30.06.2015.
 */

/**
 *
 */
@Component
public class JwtTokenRelayFilter extends ZuulFilter{

    private final String jwtHeaderName = "authorization";
    private final String jwtHeaderTokenType = "Bearer";

    @Autowired
    SsoTokenExtractorService tokenExtractorService;

    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    SsoUserDetailsService userDetailsService;

    @Autowired
    JwtTokenSessionHolder jwtTokenSessionHolder;


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest req = ctx.getRequest();

        SSOToken ssoToken = tokenExtractorService.extractToken(req);
        String currentSessionJwt = jwtTokenSessionHolder.getJwt();
        String currentSessionSSOTokenId = jwtTokenSessionHolder.getSsoTokenId();

        if (isTokenGenerationRequired(currentSessionJwt, currentSessionSSOTokenId, ssoToken)) {
            try {
                SsoUserDetails userDetails = userDetailsService.getSsoUserDetailsFromSsoToken(ssoToken);
                String jwt = jwtTokenService.convertToJwt(userDetails);

                jwtTokenSessionHolder.setJwt(jwt);
                jwtTokenSessionHolder.setSsoTokenId(ssoToken.getTokenID().toString());
                System.out.println(jwt);
            } catch (SsoUserDetailsCreationException e) {
                //Throw exception if userDetails cannot be created out of the SSOToken
                //toDo: Choose Exception type and finally logout user?
                throw new BadCredentialsException("Authentication of User failed", e);
            }

        }
        //Adds the generated Json Web Token to the Request Header as Bearer-token
        ctx.addZuulRequestHeader(jwtHeaderName, jwtHeaderTokenType + " " + jwtTokenSessionHolder.getJwt());
        return new Object();
    }

    private boolean isTokenGenerationRequired(String currentJwt, String currentSSOTokenId, SSOToken ssoToken){
        if (currentJwt !=null && currentSSOTokenId!=null && ssoToken!=null){
            if (jwtTokenService.isValid(currentJwt) && currentSSOTokenId.equals(ssoToken.getTokenID().toString())){
                return false;
            }
        }
        return true;
    }
}
