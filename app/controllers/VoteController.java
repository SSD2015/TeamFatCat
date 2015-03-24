package controllers;

import models.*;
import play.*;
import play.mvc.*;
import play.data.Form;
import play.data.DynamicForm;
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
        User user = User.find.byId((long) 1);
        Project project = Project.find.byId((long) 1);
        List<VoteCategory> voteCategories = VoteCategory.find.all();

        Vote vote;

        for(int i = 0 ; i < voteCategories.size() ; i++) {
            vote = new Vote();
            vote.category = voteCategories.get(i);
            vote.score = 1;
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
