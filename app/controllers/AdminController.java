package controllers;

import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.admin;

public class AdminController extends Controller{

    @Security.Authenticated(AdminSecured.class)
    public static Result toAdminPage() {
        Logger.info("[ " + request().username() + " ] arrive at admin page.");
        response().setHeader("Cache-Control","no-cache");
        return ok(admin.render(User.findByUsername(request().username())));
    }
}