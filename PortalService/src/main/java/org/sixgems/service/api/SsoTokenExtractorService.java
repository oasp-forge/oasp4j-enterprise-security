package org.sixgems.service.api;

import com.iplanet.sso.SSOToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Julian on 30.06.2015.
 */
@Service
public interface SsoTokenExtractorService {

    SSOToken extractToken(HttpServletRequest request);
}
