package org.sixgems.filter;

import com.iplanet.sso.SSOToken;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import org.sixgems.model.api.SsoAccessToken;
import org.sixgems.model.api.SsoUserDetails;
import org.sixgems.model.impl.JwtTokenSessionHolder;
import org.sixgems.service.SsoUserDetailsCreationException;
import org.sixgems.service.api.JwtTokenService;
import org.sixgems.service.api.SsoTokenExtractorService;
import org.sixgems.service.api.SsoUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Julian on 30.06.2015.
 */

/**
 * <p>
 *     Preprocesses requests send to the Backend-Services.
 * </p>
 * <p>
 *     This Filter gets invoked with each request the user sends to the underlying backend-services
 *     after he authenticated on the central access-manager instance.
 *     For each request the Filter checks whether the {@link JwtTokenSessionHolder} contains a valid Json Web Token (JWT)
 *     for the current user Session and if it fits to the SSOToken extracted from the HttpServletRequest.
 *
 *     If no valid Json Web Token is present in the current session or the ssoTokenId does not fit to the incoming SSOToken
 *     a new JWT will be generated from the SSOToken
 * </p>
 */

@Component
public class JwtTokenRelayFilter extends ZuulFilter{

    private static Logger LOG = LoggerFactory.getLogger(JwtTokenRelayFilter.class);

    private final String jwtHeaderName = "X-AUTH-TOKEN";
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

        SsoAccessToken ssoToken = tokenExtractorService.extractToken(req);
        String currentSessionJwt = jwtTokenSessionHolder.getJwt();
        String currentSessionSSOTokenId = jwtTokenSessionHolder.getSsoTokenId();

        if (isTokenGenerationRequired(currentSessionJwt, currentSessionSSOTokenId, ssoToken)) {
            try {
                SsoUserDetails userDetails = userDetailsService.getSsoUserDetailsFromSsoToken(ssoToken);
                String jwt = jwtTokenService.convertToJwt(userDetails);

                jwtTokenSessionHolder.setJwt(jwt);
                jwtTokenSessionHolder.setSsoTokenId(ssoToken.getTokenId());
                LOG.info("Created Json Web Token: " + jwt);
            } catch (SsoUserDetailsCreationException e) {
                //Throw exception if userDetails cannot be created out of the SSOToken
                //toDo: Choose Exception type and finally logout user?
                throw new BadCredentialsException("Authentication of User failed", e);
            }

        }
        //Adds the generated Json Web Token to the Request Header as Bearer-token
        //ctx.addZuulRequestHeader(jwtHeaderName, jwtHeaderTokenType + " " + jwtTokenSessionHolder.getJwt());
        ctx.addZuulRequestHeader(jwtHeaderName, jwtTokenSessionHolder.getJwt());
        return new Object();
    }

    /**
     * Checks whether the Json Web Token and the ssoTokenID in the current session fit to the incoming ssoToken
     * @param currentJwt
     * @param currentSSOTokenId
     * @param ssoToken
     * @return true if a new Json Web Token needs to be generated because of an invalid JWT or ssoTokenId in the current session
     */
    private boolean isTokenGenerationRequired(String currentJwt, String currentSSOTokenId, SsoAccessToken ssoToken){
        if (currentJwt !=null && currentSSOTokenId!=null && ssoToken!=null){
            if (jwtTokenService.isValid(currentJwt) && currentSSOTokenId.equals(ssoToken.getTokenId())){
                return false;
            }
        }
        return true;
    }
}
