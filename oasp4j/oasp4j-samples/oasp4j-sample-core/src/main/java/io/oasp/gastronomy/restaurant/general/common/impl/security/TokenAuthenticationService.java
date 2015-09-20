package io.oasp.gastronomy.restaurant.general.common.impl.security;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;

import io.oasp.gastronomy.restaurant.general.common.api.UserProfile;
import io.oasp.gastronomy.restaurant.general.common.api.Usermanagement;
import io.oasp.gastronomy.restaurant.general.common.api.security.UserData;
import io.oasp.module.security.common.api.accesscontrol.AccessControlProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.Collection;
import java.util.ArrayList;

public class TokenAuthenticationService {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    private final MyTokenHandler tokenHandler;
    private AccessControlProvider accessControlProvider;

    public TokenAuthenticationService(AccessControlProvider provider, String secret) {
        tokenHandler = new MyTokenHandler(secret);
        this.accessControlProvider = provider;
    }

    public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
        final User user = authentication.getDetails();
        response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(user));
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null) {
            final UserData user = tokenHandler.parseUserFromToken(token);
            if (user != null) {
            	UserData permissionUser = expandPermissions(user);
            	UserProfile profile = tokenHandler.parseUserProfileFromToken(token);
                permissionUser.setUserProfile(profile);
                return new UserAuthentication(permissionUser);
            }
        }
        return null;
    }

    private UserData expandPermissions(UserData user) {
        Collection<GrantedAuthority> userRoles = user.getAuthorities();
        ArrayList<String> roles = new ArrayList<String>();
        for(GrantedAuthority auth : userRoles) {
          roles.add(auth.getAuthority());
        }
        HashSet<String> permissions = new HashSet<String>();
        for(String role : roles) {
          role = role.substring(0,1).toUpperCase() + role.substring(1);
          HashSet<String> temp = new HashSet<String>();
          accessControlProvider.collectAccessControlIds(role, temp);
          permissions.addAll(temp);
        }
        HashSet<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();
        for(String perm : permissions) {
          authorities.add(new SimpleGrantedAuthority(perm));
        }
        UserData authorized = new UserData(user.getUsername(), user.getPassword(), authorities);
        System.err.println("Users Permissions: " + user.getAuthorities());
        return authorized;
    }
}
