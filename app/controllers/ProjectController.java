package controllers;

import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class ProjectController extends Controller {


    public static Result addProjectPage() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        if (user != null) {
            List<Team> teams = Team.find.all();
            List<Project> projects = Project.find.all();
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
        } else {
            return ok(error.render("No user"));
        }

    }

    public static Result addProject() {

        Form<Project> projectForm = Form.form(Project.class).bindFromRequest();
        if (projectForm.hasErrors()) {
            return redirect(routes.Application.index());
        }
        Project project = projectForm.get();
        project.save();
        return redirect(routes.ProjectController.addProjectPage());
    }

    public static Result project(Long loginUser, Long projectId) {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        if (user != null) {
            Project pj = Project.find.byId(projectId);
            long teamId = pj.getId();
            Team team = Team.find.byId( teamId );
            List<Long> teamMembers = team.getMembersList();
            List<User> members = new ArrayList<User>();
            for( int i=0;i<teamMembers.size() ;i++ ) {
                User users = User.find.byId( teamMembers.get(i) );
                members.add( user );
            }
            User users = User.find.byId(loginUser);
            List<Vote> votes = Vote.find.all();
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
            return ok(views.html.project.render( users, pj, members, avg ));
        } else {
            return ok(error.render("No user"));
        }

    }

    public static Result projectlist(Long loginUser) {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        if (user != null) {
            List<Project> pj = Project.find.all();
            User users = User.find.byId(loginUser);
            return ok(views.html.projectlist.render( users, pj ));
        } else {
            return ok(error.render("No user"));
        }

    }

    public static Result makeVote() {
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        return redirect(
                routes.VoteController.vote( Long.parseLong(form.data().get("uId")),Long.parseLong(form.data().get("pId")) )
        );
    }
    public static Result select() {
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        return redirect(
                routes.ProjectController.project( Long.parseLong(form.data().get("uId")),Long.parseLong(form.data().get("pId")) )
        );
    }

}
