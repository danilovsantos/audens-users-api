package com.audens.user.api.exceptions;

public class MissingTokenException extends RuntimeException{

    public MissingTokenException() {
        super("Token ausente.");
    }

}
