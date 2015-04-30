package util;

import models.User;

public class OpenIdAuthenticator extends Authenticator {
    @Override
    public User authenticate(String username, String password) {
        return User.findById(1);
    }
}