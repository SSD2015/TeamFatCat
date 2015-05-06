package controllers;

import models.*;
import play.data.DynamicForm;
import play.mvc.*;
import play.data.Form;
import views.html.*;

import java.util.List;

public class ProjectController extends Controller {

    @Security.Authenticated(AdminSecured.class)
    public static Result toAddProjectPage() {
        List<Project> projects = Project.findAll();
        List<Team> teamWithNoProject = Team.findTeamWithNoProject();

        User user = User.findByUsername(request().username());
        response().setHeader("Cache-Control","no-cache");
        return ok(addproject.render(user, projects, teamWithNoProject, ""));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result addProject() {
        DynamicForm form = new DynamicForm().bindFromRequest();
        Project project = Project.create(form.get("name"), form.get("description"));

        if (project == null) {
            User user = User.findByUsername(request().username());
            List<Team> teams = Team.findTeamWithNoProject();
            List<Project> projects = Project.findAll();
            return badRequest(addproject.render(user, projects, teams, "Cannot create project"));
        }

        Team team = Team.findById(Long.parseLong(form.get("teamId")));
        team.setProject(project);
        team.update();

        return redirect(routes.ProjectController.toAddProjectPage());
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result removeProjectById() {
        DynamicForm form = new DynamicForm().bindFromRequest();

        try {
            Project.findById(Long.parseLong(form.data().get("projectId"))).delete();
        } catch (NumberFormatException e) {

        }

        return redirect(routes.ProjectController.toAddProjectPage());
    }


    @Security.Authenticated(Secured.class)
    public static Result toProjectPage(Long projectId) {
        User user = User.findByUsername(request().username());
        Project project_model = Project.findById(projectId);
        Team team = Team.findByProject(project_model);
        List<User> members = User.findByTeam(team);

        Image avatar = Image.findAvatar(projectId);
        long avatarId;
        if (avatar == null) {
            avatarId = -1;
        } else {
            avatarId = avatar.getId();
        }
        List<Image> screenshots = Image.findImagesByProject(projectId);

        response().setHeader("Cache-Control", "no-cache");
        return ok(project.render(user, project_model, members, avatarId, screenshots));
    }

    @Security.Authenticated(Secured.class)
    public static Result rate() {
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        return redirect(
                routes.RateController.toRatePage(Long.parseLong(form.data().get("pId")))
        );
    }

    @Security.Authenticated(Secured.class)
    public static  Result editProjectDescription(Long projectId) {
        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);
        if (project == null) {
            return redirect(routes.ProjectListController.toProjectListPage());
        }

        Team team = Team.findByProject(project);
        if (!user.checkTeam(team) && user.getType() != User.ADMIN) {
            return redirect(routes.ProjectListController.toProjectListPage());
        }

        DynamicForm form = Form.form().bindFromRequest();
        String description = form.get("projectDescription");
        if (description == null || description.length() < 1) {
            description = "";
        }

        project.setDescription(description);
        project.update();
        return redirect(routes.ImageController.toEditProjectPage(projectId));
    }

    @Security.Authenticated(Secured.class)
    public static  Result editProjectName(Long projectId) {
        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);
        if (project == null) {
            return redirect(routes.ProjectListController.toProjectListPage());
        }

        Team team = Team.findByProject(project);
        if (!user.checkTeam(team) && user.getType() != User.ADMIN) {
            return redirect(routes.ProjectListController.toProjectListPage());
        }

        DynamicForm form = Form.form().bindFromRequest();
        String name = form.get("projectName");
        if (name == null || name.length() < 1) {
            List<Image> images = Image.findImagesByProject(projectId);
            return badRequest(editproject.render(user, project, images));
        }

        List<Project> projects = Project.findAll();
        for (Project p: projects) {
            if (name.equals(p.getName())) {
                List<Image> images = Image.findImagesByProject(projectId);
                return badRequest(editproject.render(user, project, images));
            }
        }

        project.setName(name);
        project.update();
        return redirect(routes.ImageController.toEditProjectPage(projectId));
    }
}