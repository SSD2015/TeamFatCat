package controllers;

import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class ProjectController extends Controller {

    public static Result addProject() {
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
        return ok(views.html.project.render( pj, members ));
    }

}
