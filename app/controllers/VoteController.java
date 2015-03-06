package controllers;

import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import views.html.*;

import java.util.List;

public class VoteController extends Controller {
	
	public static Result manageVote(){
        User user = User.find.byId(1);
        Project project = Project.find.byId(1);
        List<VoteCategory> voteCategories = VoteCategory.find.all();


        Vote vote;
        for(int i = 0 ; i < voteCategories.size() ; i++) {
            vote = new Vote();
            vote.category = voteCategories.get(i);
            vote.user = user;
            vote.project = project;
            vote.save();
        }

		return ok(index.render("Thanks for voting !"));
	}
	
    public static Result vote() {
        User user = User.find.byId(2);
        Project project = Project.find.byId(1);
        List<VoteCategory> voteCategories = VoteCategory.find.all();

        return ok(vote.render(user,project,voteCategories));
    }

    public static Result result() {
        List<Vote> voteList = Vote.find.all();
        return ok(views.html.result.render(voteList));
    }
}
