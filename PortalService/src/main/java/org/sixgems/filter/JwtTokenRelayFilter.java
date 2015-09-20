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
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

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
 *     Furthermore the accessed application gets tracked in the {@link JwtTokenSessionHolder} If a user accesses a new application the JWT will
 *     be regenerated to check if user is in at least one application-group
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

    @Autowired
    ZuulProperties zuulProperties;


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


        Map<String, ZuulProperties.ZuulRoute> routes = zuulProperties.getRoutes();
        String currentAccessedApplication = getAccessedApplication(routes, req);


        SsoAccessToken ssoToken = tokenExtractorService.extractToken(req);
        String currentSessionJwt = jwtTokenSessionHolder.getJwt();
        String currentSessionSSOTokenId = jwtTokenSessionHolder.getSsoTokenId();

        //Checks whether token is invalid or another application is accessed
        if (isTokenGenerationRequired(currentSessionJwt, currentSessionSSOTokenId, ssoToken) || !currentAccessedApplication.equals(jwtTokenSessionHolder.getCachedApplicationName())) {
            try {
                SsoUserDetails userDetails = userDetailsService.getSsoUserDetailsFromSsoToken(ssoToken);

                //In case the user has no permission group for the application, user will be rejected
                if (userDetails.getSsoUserGroupsForApplication(currentAccessedApplication).isEmpty())
                    throw new RuntimeException("Access denied for application: " + currentAccessedApplication);
                String jwt = jwtTokenService.convertToJwt(userDetails);

                jwtTokenSessionHolder.setJwt(jwt);
                jwtTokenSessionHolder.setSsoTokenId(ssoToken.getTokenId());
                jwtTokenSessionHolder.setCachedApplicationName(currentAccessedApplication);
                LOG.info("Created Json Web Token: " + jwt);
            } catch (RuntimeException e) {
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

    /**
     * Returns the configured Application name in Zuul-Config for the currently accessed application
     * @param zuulRouteMap
     * @param request
     * @return
     */
    private String getAccessedApplication(Map<String, ZuulProperties.ZuulRoute> zuulRouteMap, HttpServletRequest request){
        String servletPath = getFirstServletPathSegment(request.getServletPath());
        for(Map.Entry<String, ZuulProperties.ZuulRoute> routeEntry : zuulRouteMap.entrySet()){
            if (routeEntry.getValue().getPath().contains(servletPath)){
                return routeEntry.getKey();
            }
        }
        return null;
    }

    private String getFirstServletPathSegment(String servletPath){
        String[] segments = servletPath.split("/");
        if (segments.length > 1){
            return segments[1];
        }
        return null;
    }

    private void handleInsufficientPermissionRequest(){

    }
}
