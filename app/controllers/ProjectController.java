package controllers;

import models.*;
import play.data.DynamicForm;
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
        response().setHeader("Cache-Control","no-cache");
        return ok(addproject.render(user, projects, teams, Form.form(Project.class)));
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
        response().setHeader("Cache-Control", "no-cache");
        return ok(views.html.project.render(user, project, members, avatarId, screenshots));
    }

    @Security.Authenticated(Secured.class)
    public static Result rate() {
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        return redirect(
                routes.RateController.toRatePage(Long.parseLong(form.data().get("pId")))
        );
    }
    @Security.Authenticated(AdminSecured.class)
    public static Result removeProjById() {
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        Project project = Project.findById(Long.parseLong(form.data().get("pId")));
        List<Rate>  allRates = Rate.getAllRates();
        for( Rate rate: allRates){
            if(project.getId() == rate.getProject().getId()){
                rate.delete();
            }

        }

        project.delete();
        return redirect(routes.ProjectController.toAddProjectPage());


    }

    public static  Result editProjectDesc(Long projectId){
        DynamicForm form = Form.form().bindFromRequest();
        Project project =  Project.findById(projectId);
        project.setProjectDesc(form.get("desc"));
        project.update();
        return redirect(routes.ImageController.toEditProjectPage(projectId));
    }

    public static  Result editProjectName(Long projectId){
        DynamicForm form = Form.form().bindFromRequest();
        Project project =  Project.findById(projectId);
        project.setProjectName(form.get("Pname"));
        project.update();
        return redirect(routes.ImageController.toEditProjectPage(projectId));
    }
}