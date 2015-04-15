package controllers;

import models.User;
import play.mvc.Http;
import play.mvc.Result;

public class AdminSecured extends Secured {
    @Override
    public String getUsername(Http.Context context) {
        String username = super.getUsername(context);

        if (username == null || User.findByUsername(username).getType() != User.ADMIN) {
            return null;
        }

        return username;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return notFound("<h1>Page not found</h1>").as("text/html");
    }
}