package controllers;

import models.*;
import play.mvc.*;
import play.data.Form;
import views.html.*;

import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import java.util.ArrayList;
import java.util.List;

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
        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);

        long teamId = project.getTeamId();
        Team team = Team.findById(teamId);
        team.deleteNullMembers();

        List<Long> teamMembers = team.getMemberList();
        List<User> members = new ArrayList<User>();
        for(int i = 0 ; i < teamMembers.size() ; i++) {
            User member = User.findById(teamMembers.get(i));
            members.add(member);
        }

        Image avatar = Image.findAvatar(projectId);
        long avatarId;
        if (avatar == null) {
            avatarId = -1;
        } else {
            avatarId = avatar.getId();
        }
        List<Image> screenshots = Image.findImagesByProject(projectId);

        return ok(views.html.project.render(user, project, members, avatarId, screenshots));
    }

    @Security.Authenticated(Secured.class)
    public static Result toProjectListPage() {
        User user = User.findByUsername(request().username());
        List<Project> projects = Project.getAllProjects();
        return ok(projectlist.render(user, projects));
    }

    @Security.Authenticated(Secured.class)
    public static Result rate() {
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        return redirect(
                routes.RateController.toRatePage(Long.parseLong(form.data().get("pId")))
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
    public static Result toEditProjectPage(long projectId) {
        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);
        Team team = Team.findById(project.getTeamId());

        if (!team.isMember(user.getId()) && (user.getType() != User.ADMIN)) {
            return redirect(routes.ProjectController.toProjectListPage());
        }

        Form form = Form.form(Upload.class);
        List<Image> images = Image.findImagesByProject(projectId);
        return ok(editproject.render(user, project, images, form));
    }

    @Security.Authenticated(Secured.class)
    public static Result getImage(long imageId) {
        Image image = Image.findById(imageId);

        if (image != null) {
            return ok(image.getData()).as("image");
        } else {
            flash("error", "File not found");
            return redirect(routes.Application.toErrorPage());
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result upload(long projectId, String tag) {
        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);

        Form<Upload> form = Form.form(Upload.class).bindFromRequest();
        List<Image> images = Image.findImagesByProject(projectId);
        if (form.hasErrors()) {
            return badRequest(editproject.render(user, project, images, form));
        }

        Image.create(tag, form.get().file.getFile(), projectId);

        return redirect(routes.ProjectController.toEditProjectPage(projectId));
    }

    public static class Upload {
        public FilePart file;

        public String validate() {
            MultipartFormData form = request().body().asMultipartFormData();
            file = form.getFile("image");

            if (file == null) {
                return "No file";
            }

            return null;
        }
    }

}