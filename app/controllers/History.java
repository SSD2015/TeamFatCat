package controllers;

import models.*;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

public class History extends Controller {

    @Security.Authenticated(AdminSecured.class)
    public static Result toRateLog() {
        Logger.info("[ " + request().username() + " ] arrive at rate log page.");
        User user = User.findByUsername(request().username());
        response().setHeader("Cache-Control","no-cache");
        return ok(rateLog.render(user, Rate.getAllRates()));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result toVoteLog() {
        Logger.info("[ " + request().username() + " ] arrive at vote log page.");
        User user = User.findByUsername(request().username());
        response().setHeader("Cache-Control","no-cache");
        return ok(voteLog.render(user, Vote.getAllVotes()));
    }
}
