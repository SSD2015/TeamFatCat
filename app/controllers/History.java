package controllers;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

public class History extends Controller {

    @Security.Authenticated(AdminSecured.class)
    public static Result toRateLog() {
        User user = User.findByUsername(request().username());
        response().setHeader("Cache-Control","no-cache");
        return ok(rateLog.render(user, Rate.findAll()));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result toVoteLog() {
        User user = User.findByUsername(request().username());
        response().setHeader("Cache-Control","no-cache");
        return ok(voteLog.render(user, Vote.findAll()));
    }
}
