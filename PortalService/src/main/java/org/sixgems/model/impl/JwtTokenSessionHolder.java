package org.sixgems.model.impl;


import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * This component is responsible for storing and retrieving the generated Json Web Token
 * and a unique identifier for the SSOToken per user Session
 *
 * Created by Julian on 22.07.2015.
 */
@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JwtTokenSessionHolder implements Serializable{

    private static final long serialVersionUID = 7526472295622776147L;

    private String jwt;
    private String ssoTokenId;

    public JwtTokenSessionHolder() {
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getSsoTokenId() {
        return ssoTokenId;
    }

    public void setSsoTokenId(String ssoTokenId) {
        this.ssoTokenId = ssoTokenId;
    }
}
