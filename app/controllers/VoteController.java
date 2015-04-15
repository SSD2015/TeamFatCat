package controllers;

import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import play.data.DynamicForm;
import views.html.*;

import java.util.*;

public class VoteController extends Controller {

	public static Result manageVote() {
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        User user = User.find.byId(Long.parseLong(form.data().get("uId")));
        Project project = Project.find.byId( Long.parseLong( form.data().get("pId") ) );
        List<VoteCategory> voteCategories = VoteCategory.find.all();
        List<Vote> allVotes = Vote.find.all();
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

        return redirect(routes.VoteController.result());

	}
	
    public static Result vote( long userId, long projectId ) {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        if (user != null) {
            User users = User.find.byId( userId );
            Project project = Project.find.byId( projectId );
            List<VoteCategory> voteCategories = VoteCategory.find.all();

            return ok(vote.render(users,project,voteCategories));
        } else {
            return ok(error.render("No user"));
        }

    }

    public static Result result() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        if (user != null) {
            List<Vote> voteList = Vote.find.all();
            return ok(views.html.result.render(voteList));
        } else {
            return ok(error.render("No user"));
        }

    }

    public static Result addVoteCat(){
        Form<VoteCategory> voteCatForm = Form.form(VoteCategory.class).bindFromRequest();
        if (voteCatForm.hasErrors()) {
            return ok(views.html.error.render("Can't add Vote Category !!!"));
        }
        VoteCategory voteCat = voteCatForm.get();
        voteCat.save();
        return redirect(routes.VoteController.addVoteCatPage());
    }

    public static Result addVoteCatPage(){
        List<VoteCategory> votecatlist = VoteCategory.find.all();
        return ok(views.html.addvotecat.render( votecatlist ));
    }


}
