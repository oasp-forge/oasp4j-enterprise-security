package org.sixgems.service;

/**
 * Created by Julian on 16.07.2015.
 */

/**
 * Exception that signals an Error while constructing SsoUserDetails from
 * the SsoToken
 */
public class SsoUserDetailsCreationException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new {@link SsoUserDetailsCreationException}
     */
    public SsoUserDetailsCreationException(){}

    /**
     * Creates a new {@link SsoUserDetailsCreationException} with the given message
     * @param message
     */
    public SsoUserDetailsCreationException(String message){
        super(message);
    }
    /**
     * Creates a new {@link SsoUserDetailsCreationException} with the given message and the given cause
     * @param message
     * @param inner
     */
    public SsoUserDetailsCreationException(String message, Throwable inner){
        super(message, inner);
    }
}
