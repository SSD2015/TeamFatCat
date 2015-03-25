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
        List<Team> teams = Team.find.all();
        List<Project> projects = Project.find.all();
        for( int i=0;i<projects.size();i++ ) {
            long teamId = projects.get(i).getTeamId();
            for( int j=0;j<teams.size();j++ ) {
                if( teams.get(j).getId() == teamId ) {
                    teams.remove(j);
                    break;
                }
            }
        }
        return ok(views.html.addproject.render( projects, teams ));
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

    public static Result project() {
        Project pj = Project.find.byId( (long) 1 );
        long teamId = pj.getTeamId();
        Team team = Team.find.byId( teamId );
        long[] teamMembers = team.getMembersList();
        List<User> members = new ArrayList<User>();
        for( int i=0;i<teamMembers.length;i++ ) {
            User user = User.find.byId( teamMembers[i] );
            members.add( user );
        }
        User user = User.find.byId( (long) 1 );
        List<Vote> votes = Vote.find.all();
        double avg = 0.0;
        int count = 0;
        for( int i=0;i<votes.size();i++ ) {
            if( votes.get(i).user.getId() == user.getId() ) {
                avg += votes.get(i).score;
                count++;
            }
        }
        if( count != 0 )
            avg /= count;
        avg = Math.round(avg*100)/100.0;
        return ok(views.html.project.render( user, pj, members, avg ));
    }

}
