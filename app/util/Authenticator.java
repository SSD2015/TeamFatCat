package util;

import models.User;

public abstract class Authenticator {


    public static Authenticator getInstance() {
        String authclass = play.Play.application().configuration().getString("exceed.authenticator");
        // if property is not specified then used a default
        if (authclass == null) authclass = DatabaseAuthenticator.class.getName();
        try {
            Authenticator auth = (Authenticator) Class.forName(authclass).newInstance();
            return auth;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public abstract User authenticate(String username, String password);
}
