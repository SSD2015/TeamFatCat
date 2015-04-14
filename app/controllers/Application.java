package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.List;

import views.html.*;

public class Application extends Controller {

    public static Result toIndexPage() {
        return ok(index.render("404 Happy Error"));
    }

    public static Result toErrorPage() {
        return ok(error.render(""));
    }

    public static Result toLoginPage() {
        List<User> userList = User.getAllUsers();
        int count = 0;
        for (int i = 0 ; i < userList.size() ; i++) {
            if (userList.get(i).getType() == User.ADMIN) {
                count++;
            }
        }
        if (count == 0) {
            User.create("admin99", "admin", "Auto", "Created", 99);
        }

        if (session().get("username") != null) {
            routes.ProjectController.toProjectListPage();
        }
        return ok(login.render(Form.form(Login.class)));
    }

    @Security.Authenticated(Secured.class)
    public static Result toTestPage() {
        return ok(test.render(User.findByUsername(request().username())));
    }

    public static Result authenticate() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("username", loginForm.get().username);
            User user = User.findByUsername(loginForm.get().username);
            return redirect(
                    routes.ProjectController.toProjectListPage()
            );
        }
    }

    public static Result logout() {
        session().clear();
        return redirect(routes.Application.toLoginPage());
    }

    public static class Login {
        public String username;
        public String password;

        public String validate() {
            if(User.authenticate(username, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }
    }

}