package org.sixgems.model.api;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Julian on 16.07.2015.
 */
public interface SsoUserDetails{

    /**
     * Returns the username of the Single sign on User.
     * The username cannot be null
     * @return the username
     */
    String getSsoUserName();

    /**
     * Returns the principalName of the user
     * PrincipalName cannot be null
     * @return the principalName
     */
    String getSsoPrincipalName();

    /**
     * Returns the groups the SsoUser is associated with
     * Keys of the map represent an application name or 'general' (for groups which are valid across all apps)
     * Values of the Map represent a collection of group-names associated with the application name
     * @return a Collection of groupNames
     */
    Map<String, Collection<String>> getSsoUserGroups();

    /**
     * Returns additional Attributes of the Single Sign On User
     * SsoUserAttributes can be null, if no further Attributes should be relayed to the backend-Services
     * @return
     */
    Map<String, Object> getSsoUserAttributes();

}
