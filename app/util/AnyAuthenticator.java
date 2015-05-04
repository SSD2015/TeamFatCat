package util;

import models.User;

public class AnyAuthenticator extends Authenticator {


    @Override
    public User authenticate(String username, String password) {
        User user = User.findByUsername(username);
        return user;
    }
}