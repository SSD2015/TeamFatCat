package controllers;

import play.mvc.Result;
import play.mvc.Controller;
import play.mvc.Security;

import models.*;

public class VoteController extends Controller {

    @Security.Authenticated(AdminSecured.class)
    public static Result vote(long projectId) {
        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);

        Vote.create(user, project);

        return redirect(routes.ProjectListController.toProjectListPage());
    }
}