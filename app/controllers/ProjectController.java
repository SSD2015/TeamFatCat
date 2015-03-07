package controllers;

import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import views.html.*;

import java.util.List;

public class ProjectController extends Controller {

    public static Result index(){ return ok(addproject.render()); }

    public Result addProject() {
        //List<Project> projectList = Project.find.all();
        return ok(addproject.render());
    }

}
