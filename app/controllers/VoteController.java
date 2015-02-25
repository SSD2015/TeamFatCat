package controllers;

import models.VoteCategory;
import play.*;
import play.mvc.*;
import views.html.*;

public class VoteController extends Controller {
	
	public static Result manageVote(){
		/*
		VoteCategory c1 = new VoteCategory();
		c1.name = "type1";
		c1.save();
		
		VoteCategory c2 = new VoteCategory();
		c2.name = "type2";
		c2.save();
		
		VoteCategory c3 = new VoteCategory();
		c3.name = "type3";
		c3.save();
	
		*/
		return vote();			
	}
	
    public static Result vote() {
        return ok(vote.render());
    }
	
}
