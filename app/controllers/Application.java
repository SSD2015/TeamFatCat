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

    public static Result error() {
        return ok(error.render(""));
    }

    public static Result login() {
        return ok(login.render(Form.form(Login.class)));
    }

    public static Result test() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        if (user != null) {
            return ok(test.render(user));
        } else {
            return ok(error.render("No user"));
        }
    }

    public static Result user() {
        User users = User.find.where().eq("username", session().get("username")).findUnique();
        if (users != null) {
            List<User> userList = User.find.all();
            return ok(user.render(Form.form(User.class), userList));
        } else {
            return ok(error.render("No user"));
        }

    }

    public static Result addUser() {
        Form<User> userForm = Form.form(User.class).bindFromRequest();
        if (userForm.hasErrors()) {
            List<User> userList = User.find.all();
            return badRequest(user.render(userForm, userList));
        }
        User user = userForm.get();
        user.save();
        return redirect(routes.Application.user());
    }

    public static Result clearUsers() {
        List<User> users = new Model.Finder(Long.class, User.class).all();
        for (int i = 0 ; i < users.size() ; i++) {
            User.find.ref(users.get(i).getId()).delete();
        }
        return redirect(routes.Application.user());
    }

//    public static Result getUser() {
//        session().get("username");
//        return ok(toJson(users));
//    }

    public static Result authenticate() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("username", loginForm.get().username);
            User user = User.find.where().eq("username", loginForm.get().username).findUnique();
            return redirect(
                    routes.ProjectController.projectlist(user.getId())
                    //routes.Application.test()
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

        User user = User.find.where().eq("username", session().get("username")).findUnique();
        if (user != null) {
            List<User> userList = User.find.all();
            List<Team> teamList = Team.find.all();
            for( int i=0; i<teamList.size();i++ ) {
                List<User> members = teamList.get(i).getMembers();
                userList.removeAll(members);
            }
            return ok(team.render(userList , teamList));
        } else {
            return ok(error.render("No user"));
        }

    }

    public static Result addTeam() {
        Form<Team> teamForm = Form.form(Team.class).bindFromRequest();
        if (teamForm.hasErrors()) {
            return redirect(routes.Application.error());
        }
        Team team = teamForm.get();
        team.save();
        return redirect(routes.Application.team());
    }

    public static Result addMember(){
        DynamicForm form = Form.form().bindFromRequest();
        if (form.hasErrors()) {
            return redirect(routes.Application.error());
        }

        String teamName = form.get("teamName");
        String username = form.get("username");

        Team team = Team.find.where().eq("name", teamName).findUnique();
        User user = User.find.where().eq("username", username).findUnique();

        if(team != null & user != null){
            team.addMembers(user);
        } else {
            return redirect(routes.Application.error());
        }
        return redirect(routes.Application.team());
    }

    public static Result removeMemberFromTeam() {
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        Team team = Team.find.byId( Long.parseLong(form.data().get("tId")) );
        team.removeMember( Long.parseLong(form.data().get("uId")) );
        team.update();
        return redirect(routes.Application.team());
    }
}