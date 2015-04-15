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
        List<User> userList = User.getStudents();
        List<Team> teamList = Team.getAllTeams();
        for( int i=0; i<teamList.size();i++ ) {
            List<User> members = teamList.get(i).getMembers();
            userList.removeAll(members);
        }

        User user = User.findByUsername(request().username());
        return ok(team.render(user, userList, teamList));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result addTeam() {
        Form<Team> teamForm = Form.form(Team.class).bindFromRequest();
        if (teamForm.hasErrors()) {
            return redirect(routes.Application.toErrorPage());
        }
        Team team = teamForm.get();
        team.save();
        return redirect(routes.TeamController.toTeamPage());
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result addMember() {
        DynamicForm form = Form.form().bindFromRequest();
        if (form.hasErrors()) {
            return redirect(routes.Application.toErrorPage());
        }

        String teamName = form.get("teamName");
        String username = form.get("username");

        Team team = Team.findByName(teamName);
        User user = User.findByUsername(username);

        if(team != null & user != null){
            team.addMember(user);
        } else {
            return redirect(routes.Application.toErrorPage());
        }
        return redirect(routes.TeamController.toTeamPage());
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result removeMemberFromTeam() {
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        Team team = Team.findById( Long.parseLong(form.data().get("tId")) );
        team.removeMember( Long.parseLong(form.data().get("uId")) );
        team.update();
        return redirect(routes.TeamController.toTeamPage());
    }
}
