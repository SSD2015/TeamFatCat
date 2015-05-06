package controllers;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Controller;
import play.mvc.Security;

import models.*;
import views.html.*;

import java.util.List;

public class VoteController extends Controller {

    @Security.Authenticated(AdminSecured.class)
    public static Result vote(long voteCategoryId) {
        User user = User.findByUsername(request().username());
        VoteCategory voteCategory = VoteCategory.findById(voteCategoryId);

        DynamicForm form = new DynamicForm().bindFromRequest();
        Project project;
        try {
            project = Project.findById(Long.parseLong(form.get("project")));
        } catch (NumberFormatException e) {
            return badRequest(vote.render(user, voteCategory, Project.findAll()));
        }

        if (project == null) {
            return badRequest(vote.render(user, voteCategory, Project.findAll()));
        }

        Vote.create(user, voteCategory, project);

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

        try {
            VoteCategory.findById(Long.parseLong(form.data().get("categoryId"))).delete();
        } catch (NumberFormatException e) {

        }

        return redirect(routes.VoteController.toAddVoteCatPage());
    }

    @Security.Authenticated(Secured.class)
    public static Result toVotePage(long voteCategoryId) {
        User user = User.findByUsername(request().username());
        VoteCategory voteCategory = VoteCategory.findById(voteCategoryId);
        List<Project> projects = Project.findAll();

        response().setHeader("Cache-Control", "no-cache");
        return ok(vote.render(user, voteCategory, projects));
    }

}