package controllers;

import models.Project;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.projectlist;

import java.util.List;

public class ProjectListController extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result select() {
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        return redirect(
                routes.ProjectController.toProjectPage(Long.parseLong(form.data().get("pId")))
        );
    }

    @Security.Authenticated(Secured.class)
    public static Result toProjectListPage() {
        User user = User.findByUsername(request().username());
        List<Project> projects = Project.findAll();

        response().setHeader("Cache-Control","no-cache");
        return ok(projectlist.render(user, projects));
    }

}
