package org.sixgems.service.api;

import com.iplanet.sso.SSOToken;
import org.sixgems.model.api.SsoAccessToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Julian on 30.06.2015.
 */
@Service
public interface SsoTokenExtractorService {

    /**
     * Returns the extracted SsoAccessToken of the HttpServletRequest
     * @param request
     * @return
     */
    SsoAccessToken extractToken(HttpServletRequest request);
}
