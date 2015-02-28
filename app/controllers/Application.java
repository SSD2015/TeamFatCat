package controllers;

import models.User;
import play.api.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import static play.data.Form.form;

public class Application extends Controller {

    public static Result index() {
        return ok(views.html.index.render("hello"));
    }

    public static Result login() {
        return ok(views.html.login.render(Form<Login> form(Login.class)));
    }

    public static class Login {

        public String username;
        public String password;

        public String validate() {
            if(User.authenticate(username,password) == null) {
                return "Invalid user or password";
            }

            return null;
        }

        public static Result authenticate() {
            Form<Login> loginForm = form(Login.class).bindFromRequest();
            if (loginForm.hasErrors()) {
                return badRequest(views.html.login.render(loginForm));
            } else {
                session().clear();
                session("username", loginForm.get().username);
                return redirect(
                        routes.Application.index()
                );
            }
        }
    }
}