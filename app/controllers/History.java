package controllers;

import models.User;
import models.Rate;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.rateLog;

public class History extends Controller {

    @Security.Authenticated(AdminSecured.class)
    public static Result toRateLog() {
        User user = User.findByUsername(request().username());
        return ok(rateLog.render(user, Rate.getAllRates()));
    }
}
