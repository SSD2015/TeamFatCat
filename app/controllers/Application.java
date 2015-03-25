package controllers;

import models.Project;
import models.User;
import models.Team;
import models.VoteCategory;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

import views.html.*;

import static play.libs.Json.toJson;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("404 Happy Error"));
    }
    
    public static Result login() {
        return ok(login.render(Form.form(Login.class)));
    }

    public static Result user() {
        List<User> userList = User.find.all();
        return ok(user.render(userList));
    }

    public static Result addUser() {
        Form<User> userForm = Form.form(User.class).bindFromRequest();
        if (userForm.hasErrors()) {
            return redirect(routes.Application.index());
        }
        User user = userForm.get();
        user.save();
        return redirect(routes.Application.user());
    }

    public static Result clearUser() {
        List<User> users = new Model.Finder(Long.class, User.class).all();
        for (int i = 0 ; i < users.size() ; i++) {
            users.get(i).delete();
        }
        return redirect(routes.Application.user());
    }

    public static Result getUser() {
        List<User> users = new Model.Finder(Long.class, User.class).all();
        return ok(toJson(users));
    }

    public static Result authenticate() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("username", loginForm.get().username);
            User user = User.find.where().eq("username", loginForm.get().username).findUnique();
            return redirect(
                    routes.ProjectController.project(user.getId())
            );
        }
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


    public static Result team() {
        List<User> userList = User.find.all();
        List<Team> teamList = Team.find.all();
        return ok(team.render(userList , teamList));
    }

    public static Result addTeam() {
        Form<Team> teamForm = Form.form(Team.class).bindFromRequest();
        if (teamForm.hasErrors()) {
            return redirect(routes.Application.index());
        }
        Team team = teamForm.get();
        team.save();
        return redirect(routes.Application.team());
    }

    public static Result addMember(){
        DynamicForm form = Form.form().bindFromRequest();
        if (form.hasErrors()) {
            return redirect(routes.Application.index());
        }

        String teamName = form.get("teamName");
        String username = form.get("username");

        Team team = Team.find.where().eq("name", teamName).findUnique();
        User user = User.find.where().eq("username", username).findUnique();

        if(team != null & user != null){
            team.addMembers(user);
        } else {
            return redirect(routes.Application.index());
        }
        return redirect(routes.Application.team());
    }

}