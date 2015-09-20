package org.sixgems.service.api;

import org.sixgems.service.SsoUserLogoutException;
import org.springframework.stereotype.Component;

/**
 * Created by Julian on 15.09.2015.
 */
@Component
public interface LogoutService {
    /**
     * Logs out the current user
     * @return true if the logout was successful, false otherwise
     */
    public boolean logoutUser() throws SsoUserLogoutException;
}
