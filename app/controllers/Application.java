package controllers;

import play.data.*;
import play.mvc.*;
import views.html.*;
import models.User;

import static play.data.Form.form;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result login() {
        return ok(
                login.render(form(Login.class))
        );
    }
}

public static class Login {

    public String username;
    public String password;

    public String validate() {
        if(User.authenticate() == null) {
            return "Invalid user or password";
        }

        return null;
    }

}