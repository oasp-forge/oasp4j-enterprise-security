package org.sixgems.service.api;

import org.sixgems.model.api.SsoUserDetails;
import org.springframework.stereotype.Service;

/**
 * Created by Julian on 30.06.2015.
 */
@Service
public interface JwtTokenService {

    /**
     * Returns a Json Web Token generated out of the provided userDetails
     * @param uerDetails
     * @return a JWT string
     */
    String convertToJwt (SsoUserDetails uerDetails);

    /**
     * Checks whether the provided Json Web Token is a valid Token and if it is not expired
     * @param jwt
     * @return true if JWT is a valid Json Web Token and the expiration time is not reached yet
     */
    boolean isValid(String jwt);
}
