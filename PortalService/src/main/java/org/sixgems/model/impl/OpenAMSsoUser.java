package org.sixgems.model.impl;

import org.sixgems.model.api.SsoUserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Julian on 16.07.2015.
 */
public class OpenAMSsoUser implements SsoUserDetails {

    private String ssoPrincipalName;

    private String ssoUserName;

    private Map<String, Collection<String>> ssoUserGroups;

    private Map<String, Object> ssoUserAttributes;

    public OpenAMSsoUser(String ssoPrincipalName, String ssoUserName, Map<String, Collection<String>> ssoUserGroups, Map<String, Object> ssoUserAttributes) {

        if (ssoPrincipalName == null || ssoPrincipalName.equals("") || ssoUserName == null || ssoUserName.equals("")){
            throw new IllegalArgumentException("The username and the principalName must not be null or empty");
        }

        this.ssoPrincipalName = ssoPrincipalName;
        this.ssoUserName = ssoUserName;
        this.ssoUserGroups = ssoUserGroups;
        this.ssoUserAttributes = ssoUserAttributes;
    }

    @Override
    public String getSsoUserName() {
        return ssoUserName;
    }

    @Override
    public String getSsoPrincipalName() {
        return ssoPrincipalName;
    }

    @Override
    public Map<String, Collection<String>> getSsoUserGroups() {
        return ssoUserGroups;
    }

    @Override
    public Map<String, Object> getSsoUserAttributes() {
        return ssoUserAttributes;
    }
}
