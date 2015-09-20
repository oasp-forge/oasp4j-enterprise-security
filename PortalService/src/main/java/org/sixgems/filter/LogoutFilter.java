package org.sixgems.filter;

import com.netflix.client.http.HttpResponse;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.sixgems.service.SsoUserLogoutException;
import org.sixgems.service.api.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Julian on 15.09.2015.
 */
@Component
public class LogoutFilter extends ZuulFilter {

    private final String logoutMatcherUrlPath = "logout";

    @Autowired
    private LogoutService logoutService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        if(matchesLogout()){
            return true;
        }
        else return false;
    }

    @Override
    public Object run() {
        try{
            RequestContext context = RequestContext.getCurrentContext();
            if(logoutService.logoutUser()){
                context.setResponseStatusCode(200);
                context.setResponseBody("Logout successful");
            }
            else{
                context.setResponseStatusCode(400);
                context.setResponseBody("Logout not successful");
            }
        }
        catch(SsoUserLogoutException e){
            RequestContext context = RequestContext.getCurrentContext();
            context.setResponseStatusCode(400);
            context.setResponseBody(e.getMessage());
        }
        return null;
    }

    private boolean matchesLogout(){
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest req = ctx.getRequest();
        String logoutSegment = req.getRequestURI().replaceFirst(".*/([^/?]+).*", "$1");
        return logoutSegment.equals(logoutMatcherUrlPath);
    }
}
