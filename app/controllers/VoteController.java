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
        //List<VoteCategory> voteCategories = VoteCategory.find.all();


        Vote vote = new Vote();
        //vote.category = voteCat;
        vote.user = user;
        vote.project = project;
        vote.save();

		return vote();			
	}
	
    public static Result vote() {
        User user = User.find.byId(1);
        Project project = Project.find.byId(1);
        List<VoteCategory> voteCategories = VoteCategory.find.all();

        return ok(vote.render(user,project,voteCategories));
    }
	
}
