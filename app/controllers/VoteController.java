package controllers;

import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import views.html.*;

import java.util.List;

public class VoteController extends Controller {
	private User currentUser;
    private Project currentProject;

    public Project getCurrentProject(){
        return this.currentProject;
    }

    public void setCurrentProject(Project project){
        this.currentProject = project;
    }

    public User getCurrentUser(){
        return this.currentUser;
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
    }

	public static Result manageVote(){
        User user = User.find.byId(3);
        Project project = Project.find.byId(2);
        List<VoteCategory> voteCategories = VoteCategory.find.all();


        Vote vote;
        for(int i = 0 ; i < voteCategories.size() ; i++) {
            vote = new Vote();
            vote.category = voteCategories.get(i);
            vote.user = user;
            vote.project = project;
            vote.save();
        }

        return ok(views.html.index.render("Thanks for voting !"));

	}
	
    public static Result vote() {
        User user = User.find.byId(3);
        Project project = Project.find.byId(2);
        List<VoteCategory> voteCategories = VoteCategory.find.all();

        return ok(vote.render(user,project,voteCategories));
    }

    public static Result result() {
        List<Vote> voteList = Vote.find.all();
        return ok(views.html.result.render(voteList));
    }
}
