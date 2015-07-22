package org.sixgems.service.api;

import com.iplanet.sso.SSOToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Julian on 30.06.2015.
 */
@Service
public interface SsoTokenExtractorService {

    /**
     * Returns the extracted SSOToken of the HttpServletRequest
     * @param request
     * @return
     */
    SSOToken extractToken(HttpServletRequest request);
}