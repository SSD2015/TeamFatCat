package controllers;

import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class ProjectController extends Controller {

    @Security.Authenticated(AdminSecured.class)
    public static Result toAddProjectPage() {
        List<Team> teams = Team.getAllTeams();
        List<Project> projects = Project.getAllProjects();
        for( int i=0;i<projects.size();i++ ) {
            long teamId = projects.get(i).getId();
            for( int j=0;j<teams.size();j++ ) {
                if( teams.get(j).getId() == teamId ) {
                    teams.remove(j);
                    break;
                }
            }
        }
        return ok(views.html.addproject.render( projects, teams ));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result addProject() {
        Form<Project> projectForm = Form.form(Project.class).bindFromRequest();
        if (projectForm.hasErrors()) {
            return redirect(routes.Application.toIndexPage());
        }
        Project project = projectForm.get();
        project.save();
        return redirect(routes.ProjectController.toAddProjectPage());
    }

    @Security.Authenticated(Secured.class)
    public static Result toProjectPage(Long projectId) {
        Project pj = Project.findById(projectId);
        long teamId = pj.getId();
        Team team = Team.findById(teamId);
        team.deleteNullMembers();
        List<Long> teamMembers = team.getMemberList();
        List<User> members = new ArrayList<User>();
        for( int i=0;i<teamMembers.size() ;i++ ) {
            User user = User.findById(teamMembers.get(i));
            members.add( user );
        }
        User user = User.findByUsername(request().username());
        List<Vote> votes = Vote.getAllVotes();
        double avg = 0.0;
        int count = 0;
        for( int i=0;i<votes.size();i++ ) {
            if( votes.get(i).user.getId() == user.getId() && votes.get(i).project.getId() == pj.getId() ) {
                avg += votes.get(i).score;
                count++;
            }
        }
        if( count != 0 )
            avg /= count;
        avg = Math.round(avg*100)/100.0;
        return ok(views.html.project.render( user, pj, members, avg ));
    }

    @Security.Authenticated(Secured.class)
    public static Result toProjectListPage() {
        List<Project> pj = Project.getAllProjects();
        return ok(views.html.projectlist.render(User.findByUsername(request().username()), pj));
    }

    @Security.Authenticated(Secured.class)
    public static Result makeVote() {
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        return redirect(
                routes.VoteController.toVotePage(Long.parseLong(form.data().get("pId")))
        );
    }

    @Security.Authenticated(Secured.class)
    public static Result select() {
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        return redirect(
                routes.ProjectController.toProjectPage(Long.parseLong(form.data().get("pId")))
        );
    }

}
