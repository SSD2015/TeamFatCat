package controllers;

import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import views.html.*;

import java.util.List;

public class ProjectController extends Controller {

    public static Result index(){ return ok(addproject.render()); }

    public static Result addProject() {
        Project project = Form.form(Project.class).bindFromRequest().get();
        project.save();
        return redirect(routes.ProjectController.index());

    }


}
