package controllers;

import play.data.Form;
import play.mvc.Result;
import play.mvc.Controller;
import play.mvc.Security;

import models.*;
import views.html.addvotecat;

import java.util.List;

public class VoteController extends Controller {

    @Security.Authenticated(AdminSecured.class)
    public static Result vote(long projectId) {
        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);

        Vote.create(user, project);

        return redirect(routes.ProjectListController.toProjectListPage());
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result toAddVoteCatPage() {
        List<VoteCategory> voteCategories = VoteCategory.findAll();
        User user = User.findByUsername(request().username());

        response().setHeader("Cache-Control", "no-cache");
        return ok(addvotecat.render(user, voteCategories, Form.form(VoteCategory.class)));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result addVoteCat() {
        Form<VoteCategory> voteCatForm = Form.form(VoteCategory.class).bindFromRequest();
        if (voteCatForm.hasErrors()) {
            User user = User.findByUsername(request().username());
            List<VoteCategory> voteCatList = VoteCategory.findAll();
            return badRequest(addvotecat.render(user, voteCatList, voteCatForm));
        }
        VoteCategory voteCat = voteCatForm.get();
        voteCat.save();
        return redirect(routes.VoteController.toAddVoteCatPage());
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result removeVoteCategory() {
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        VoteCategory voteCategory = VoteCategory.findById(Long.parseLong(form.data().get("cId")));

        voteCategory.delete();

        return redirect(routes.VoteController.toAddVoteCatPage());
    }
}