package controllers;

import models.*;
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
            User.create("inont", "inont", "Natcha", "Charoen", 1);
            User.create("yanagi", "yanagi", "Chonni", "Kitti", 1);
            User.create("maxmi", "maxmi", "Kitti", "Promdi", 1);
            User.create("nichy", "nichy", "Nicha", "Han", 1);
            User.create("gurokung", "gurokung", "Jirat", "Inta", 1);
            Team.create("Maxmi and Friends","2,3,4");
            Team.create("Nichy and Friends", "5,6");
            Project.create("Fast 7", "For paul", 1);
            Project.create("Avenger 2","For Iron man",2);
            VoteCategory.create("Security");
            VoteCategory.create("Performance");


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