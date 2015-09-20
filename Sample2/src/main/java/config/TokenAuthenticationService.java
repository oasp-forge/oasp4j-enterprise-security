package config;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

public class TokenAuthenticationService {
	 
    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
 
    private final MyTokenHandler tokenHandler;
 
    public TokenAuthenticationService(PrivateKey privKey, PublicKey pubKey) {
        tokenHandler = new MyTokenHandler(privKey, pubKey);
    }
 
    public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
        final User user = authentication.getDetails();
        response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(user));
    }
 
    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null) {
            final User user = tokenHandler.parseUserFromToken(token);
            if (user != null) {
                return new UserAuthentication(user);
            }
        }
        return null;
    }
    
    public MyTokenHandler getTokenHandler() {
    	return tokenHandler;
    }
}