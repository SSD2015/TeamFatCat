package controllers;

import play.data.*;
import play.mvc.*;
import views.html.*;

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

    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(Login.render(loginForm));
        } else {
            session().clear();
            session("ku", loginForm.get().ku);
            return redirect(
                    routes.Application.index()
            );
        }
    }
}

public static class Login {

    public String ku;
    public String password;

    public String validate() {
        if(User.authenticate(ku, password) == null) {
            return "Invalid user or password";
        }

        return null;
    }
}