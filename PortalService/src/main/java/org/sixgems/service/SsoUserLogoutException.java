package org.sixgems.service;

/**
 * Created by Julian on 15.09.2015.
 */
public class SsoUserLogoutException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new {@link SsoUserDetailsCreationException}
     */
    public SsoUserLogoutException(){}

    /**
     * Creates a new {@link SsoUserDetailsCreationException} with the given message
     * @param message
     */
    public SsoUserLogoutException(String message){
        super(message);
    }
    /**
     * Creates a new {@link SsoUserDetailsCreationException} with the given message and the given cause
     * @param message
     * @param inner
     */
    public SsoUserLogoutException(String message, Throwable inner){
        super(message, inner);
    }
}
