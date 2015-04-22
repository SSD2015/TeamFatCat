package controllers;

import models.Team;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.team;

import java.util.List;

public class TeamController extends Controller {


    @Security.Authenticated(AdminSecured.class)
    public static Result toTeamPage() {
        User user = User.findByUsername(request().username());
        List<User> studentNotInTeamList = User.findStudentNotInTeam();
        List<Team> teamList = Team.findAll();

        response().setHeader("Cache-Control","no-cache");
        return ok(team.render(user, studentNotInTeamList, teamList, Form.form(Team.class)));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result toBadRequestTeamPage() {
        User user = User.findByUsername(request().username());
        List<User> studentNotInTeamList = User.findStudentNotInTeam();
        List<Team> teamList = Team.findAll();

        response().setHeader("Cache-Control","no-cache");
        return badRequest(team.render(user, studentNotInTeamList, teamList, Form.form(Team.class)));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result addTeam() {
        Form<Team> teamForm = Form.form(Team.class).bindFromRequest();
        if (teamForm.hasErrors()) {
            User user = User.findByUsername(request().username());
            List<User> studentNotInTeamList = User.findStudentNotInTeam();
            List<Team> teamList = Team.findAll();
            return badRequest(team.render(user, studentNotInTeamList, teamList, teamForm));
        }

        Team team = teamForm.get();
        team.save();
        return redirect(routes.TeamController.toTeamPage());
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result addMember() {
        DynamicForm form = Form.form().bindFromRequest();
        if (form.hasErrors()) {
            return toBadRequestTeamPage();
        }

        String teamName = form.get("teamName");
        String username = form.get("username");

        Team team = Team.findByName(teamName);
        User user = User.findByUsername(username);

        if (team != null && user != null && user.checkTeam(null)) {
            user.setTeam(team);
            user.update();
        }

        return toBadRequestTeamPage();
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result removeMemberFromTeam() {
        DynamicForm form = new DynamicForm().bindFromRequest();
        User user = User.findByUsername(form.get("username"));
        user.setTeam(null);
        user.update();

        return redirect(routes.TeamController.toTeamPage());
    }
}
