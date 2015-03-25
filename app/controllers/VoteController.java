package controllers;

import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import play.data.DynamicForm;
import views.html.*;

import java.util.List;

public class VoteController extends Controller {

	public static Result manageVote() {
        Form<Object> form = Form.form(Object.class).bindFromRequest();
        User user = User.find.byId((long) 1);
        Project project = Project.find.byId((long) 1);
        List<VoteCategory> voteCategories = VoteCategory.find.all();
        List<Vote> votes = Vote.find.all();
        for( Vote v: votes ) {
            if( v.user.getId() != user.getId() )
                votes.remove(v);
            else {
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
	
    public static Result vote() {
        User user = User.find.byId((long) 1);
        Project project = Project.find.byId((long) 1);
        List<VoteCategory> voteCategories = VoteCategory.find.all();

        return ok(vote.render(user,project,voteCategories));
    }

    public static Result result() {
        List<Vote> voteList = Vote.find.all();
        return ok(views.html.result.render(voteList));
    }
}
