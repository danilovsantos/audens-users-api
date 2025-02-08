package com.audens.user.api.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{

    public EmailAlreadyExistsException() {
        super("Este e-mail já está cadastrado.");
    }

}
