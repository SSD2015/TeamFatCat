package controllers;

import models.Project;
import models.User;
import models.VoteCategory;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.projectlist;
import java.util.List;
import static play.libs.Json.toJson;

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
        List<VoteCategory> voteCategories = VoteCategory.findAll();

        response().setHeader("Cache-Control","no-cache");
        return ok(projectlist.render(user, projects, voteCategories));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result projectsJson() {
        List<Project> projects = Project.findAll();
        return ok(toJson(projects));
    }
}
