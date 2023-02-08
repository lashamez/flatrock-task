package com.flatrock.common.errors;

public class UsernameAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public UsernameAlreadyUsedException() {
        super("Login name already used!", "user", "userexists");
    }
}
