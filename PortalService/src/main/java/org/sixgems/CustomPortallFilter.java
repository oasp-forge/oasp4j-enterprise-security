package org.sixgems;

import com.netflix.util.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Julian on 21.06.2015.
 */
@Component
public class CustomPortallFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "post";
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
        /*
        Map<String, String> requestHeaders = RequestContext.getCurrentContext().getZuulRequestHeaders();
        for(Map.Entry<String, String> header: requestHeaders.entrySet()){

            System.out.println(header.getKey() + " --- " + header.getValue());

        }*/
        List<Pair<String, String>> responseHeaders = RequestContext.getCurrentContext().getZuulResponseHeaders();
        for (Pair<String, String> entry: responseHeaders){
            if (entry.first().equals("Location")){
                System.out.println(entry.second());
            }
        }

        return new Object();
    }
}