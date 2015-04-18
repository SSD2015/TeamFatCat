package controllers;

import models.Deadline;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Date;
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

    public static Result toClockPage() {
        return ok(testclock.render());
    }

    public static Result authenticate() {
        DynamicForm dy = new DynamicForm().bindFromRequest();
        String username = dy.get("username");
        String password = dy.get("password");
        User user = User.findByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            session().clear();
            session("username", username);
            return redirect(routes.ProjectController.toProjectListPage());
        }else{
            return redirect(routes.Application.toLoginPage());
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
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