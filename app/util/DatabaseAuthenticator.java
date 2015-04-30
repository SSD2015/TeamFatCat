package util;

import models.User;
import org.mindrot.jbcrypt.BCrypt;

public class DatabaseAuthenticator extends Authenticator {


    @Override
    public User authenticate(String username, String password) {
        User user = User.findByUsername(username);
        if (user == null) return null;
        if ( BCrypt.checkpw(password, user.getPassword()) ) return user;
        return null;

    }
}