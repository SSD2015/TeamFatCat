package controllers;

import models.*;
import play.mvc.*;
import play.data.Form;
import views.html.*;

import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import java.util.ArrayList;
import java.util.List;

import java.io.File;

public class ProjectController extends Controller {

    @Security.Authenticated(AdminSecured.class)
    public static Result toAddProjectPage() {
        List<Team> teams = Team.getAllTeams();
        List<Project> projects = Project.getAllProjects();
        for( int i=0;i<projects.size();i++ ) {
            long teamId = projects.get(i).getTeamId();
            for( int j=0;j<teams.size();j++ ) {
                if( teams.get(j).getId() == teamId ) {
                    teams.remove(j);
                    break;
                }
            }
        }

        User user = User.findByUsername(request().username());
        return ok(views.html.addproject.render(user, projects, teams));
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
            if( votes.get(i).getUser().getId() == user.getId() && votes.get(i).getProject().getId() == pj.getId() ) {
                avg += votes.get(i).getScore();
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

    @Security.Authenticated(Secured.class)
    public static Result toUploadPage() {
        User user = User.findByUsername(request().username());
        return ok(uploadimage.render(user));
    }

//    @Security.Authenticated(Secured.class)
//    public static Result upload() {
//        MultipartFormData body = request().body().asMultipartFormData();
//        FilePart picture = body.getFile("picture");
//        if (picture != null) {
//            String fileName = picture.getFilename();
//            String contentType = picture.getContentType();
//            File file = picture.getFile();
//
//            picture
//            Picture pic = Picture.create(file, fileName, contentType);
//            if (pic == null) {
//                return redirect(routes.ProjectController.toUploadPage());
//            }
//            //return ok("File uploaded");
//            return redirect(routes.ProjectController.getPicture(pic.getId()));
//        } else {
//            flash("error", "Missing file");
//            return redirect(routes.ProjectController.toUploadPage());
//        }
//    }
//
//    @Security.Authenticated(Secured.class)
//    public static Result getPicture(Long pictureId) {
//        return ok(picture.render(Picture.findById(pictureId)));
//    }

}
