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
        return ok(views.html.addproject.render(user, projects, teams, Form.form(Project.class)));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result addProject() {
        Form<Project> projectForm = Form.form(Project.class).bindFromRequest();
        if (projectForm.hasErrors()) {
            User user = User.findByUsername(request().username());
            List<Team> teamList = Team.getAllTeams();
            List<Project> proList = Project.getAllProjects();
            return badRequest(addproject.render(user, proList, teamList, projectForm));
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
        List<Image> images = Image.getByProjectId(projectId);
        Image avatar = images.get(0);
        images.remove(0);
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
        return ok(views.html.project.render( user, pj, members, avg, images , avatar));
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
        List<Project> projects = Project.getAllProjects();
        List<Image> images = Image.getAllImage();
        return ok(uploadimage.render(user,images, projects));
    }

    @Security.Authenticated(Secured.class)
    public static Result upload() {
        List<Image> images = Image.getAllImage();
        Form<Image> imageForm = Form.form(Image.class).bindFromRequest();
        Image image = imageForm.get();
        String url = image.getUrl();
        url = url.substring(url.lastIndexOf(".")+1);
        for(int i = 0 ; i < images.size() ; i++){
            if(image.getName().equals(images.get(i).getName())){
                return redirect(routes.ProjectController.toUploadPage());
            }
        }
        if(url.equals("jpg")) {
            image.save();
        }

        return redirect(routes.ProjectController.toUploadPage());

    }

}