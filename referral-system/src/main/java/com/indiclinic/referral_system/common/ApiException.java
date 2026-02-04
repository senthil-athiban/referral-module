package com.indiclinic.referral_system.common;

public class ApiException extends RuntimeException{
    public ApiException(String message) {
        super(message);
    }
}
