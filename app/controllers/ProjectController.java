package controllers;

import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class ProjectController extends Controller {

    public static Result index(){ return ok(addproject.render()); }

    public static Result addProject() {
        //List<Project> projectList = Project.find.all();
        return ok(addproject.render());
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
