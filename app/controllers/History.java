package controllers;

import models.User;
import models.Vote;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.voteLog;

public class History extends Controller {

    @Security.Authenticated(AdminSecured.class)
    public static Result toVoteLog() {
        User user = User.findByUsername(request().username());
        return ok(voteLog.render(user, Vote.getAllVotes()));
    }
}
