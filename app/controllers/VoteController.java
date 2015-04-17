package controllers;

import com.avaje.ebean.Ebean;
import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import play.data.DynamicForm;
import views.html.*;

import java.sql.Timestamp;
import java.util.*;

public class VoteController extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result manageVote(Long projectId) {
        DynamicForm form = Form.form().bindFromRequest();
        if (form.hasErrors()) {
            return redirect(routes.Application.toErrorPage());
        }

        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);

        List<VoteCategory> votecatList = VoteCategory.all();
        int size = votecatList.size();
        for (int i = 0 ; i < size ; i++) {
            if (form.get(votecatList.get(i).getName()) != null) {

                Vote vote = new Vote();
                vote.setScore(Integer.parseInt(form.get(votecatList.get(i).getName())));
                vote.setUser(user);
                vote.setProject(project);
                vote.setCategory(votecatList.get(i));
                vote.setTimestamp();
                vote.save();
            } else {
                redirect(routes.Application.toErrorPage());
            }
        }

        return redirect(routes.ProjectController.toProjectPage(project.getId()));
    }

    @Security.Authenticated(Secured.class)
    public static Result toVotePage(long projectId) {
        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);
        List<VoteCategory> voteCategories = VoteCategory.all();

        return ok(vote.render(user,project,voteCategories));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result toResultPage() {
        List<Vote> voteList = Vote.getAllVotes();
        List<VoteCategory> catList = VoteCategory.all();
        return ok(result.render(voteList,catList));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result addVoteCat() {
        Form<VoteCategory> voteCatForm = Form.form(VoteCategory.class).bindFromRequest();
        if (voteCatForm.hasErrors()) {
            return ok(views.html.error.render("Can't add Vote Category !!!"));
        }
        VoteCategory voteCat = voteCatForm.get();
        voteCat.save();
        return redirect(routes.VoteController.toAddVoteCatPage());
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result toAddVoteCatPage(){
        List<VoteCategory> votecatlist = VoteCategory.all();
        User user = User.findByUsername(request().username());
        return ok(addvotecat.render(user, votecatlist));
    }


}