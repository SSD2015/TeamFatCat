package controllers;

import models.Project;
import models.User;
import models.VoteCategory;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

import views.html.*;

import static play.libs.Json.toJson;

public class Application extends Controller {

    public static Result index() {
        return ok(views.html.index.render("hello"));
    }
    
    public static Result login() {
        return ok(login.render(Form.form(Login.class)));
    }

    public static Result user() {
        return ok(user.render());
    }

    public static Result project() {
        return ok(views.html.project.render());
    }

    public static Result addUser() {
        User user = Form.form(User.class).bindFromRequest().get();
        user.save();
        return redirect(routes.Application.user());
    }

    public static Result getUser() {
        List<User> users = new Model.Finder(Integer.class, User.class).all();
        return ok(toJson(users));
    }

    public static Result authenticate() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("username", loginForm.get().username);
            return redirect(
                    routes.Application.index()
            );
        }
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


    } 
}