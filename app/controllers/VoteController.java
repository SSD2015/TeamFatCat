package controllers;

import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import play.data.DynamicForm;
import views.html.*;

import java.util.*;

public class VoteController extends Controller {

    @Security.Authenticated(Secured.class)
	public static Result manageVote() {
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        User user = User.findById(Long.parseLong(form.data().get("uId")));
        Project project = Project.findById(Long.parseLong(form.data().get("pId")));
        List<VoteCategory> voteCategories = VoteCategory.find.all();
        List<Vote> allVotes = Vote.getAllVotes();
        List<Vote> votes = new ArrayList<Vote>();
        for( Vote v: allVotes ) {
            if( v.user.getId() == user.getId() && v.project.getId() == project.getId() ) {
                votes.add( v );
                VoteCategory voteCat = v.category;
                v.score = Integer.parseInt( form.data().get( voteCat.name ) );
                voteCategories.remove( voteCat );
                v.update();
            }
        }

        Vote vote;
        for(int i = 0 ; i < voteCategories.size() ; i++) {
            vote = new Vote();
            vote.category = voteCategories.get(i);
            vote.score = Integer.parseInt( form.data().get( vote.category.name ) );
            vote.user = user;
            vote.project = project;
            vote.save();
        }

        return redirect(routes.ProjectController.toProjectListPage());

	}

    @Security.Authenticated(Secured.class)
    public static Result toVotePage(long projectId ) {
        User user = User.findByUsername(request().username());
        Project project = Project.findById(projectId);
        List<VoteCategory> voteCategories = VoteCategory.find.all();

        return ok(vote.render(user,project,voteCategories));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result toResultPage() {
        List<Vote> voteList = Vote.getAllVotes();
        return ok(views.html.result.render(voteList));
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
        List<VoteCategory> votecatlist = VoteCategory.find.all();
        return ok(views.html.addvotecat.render( votecatlist ));
    }


}
