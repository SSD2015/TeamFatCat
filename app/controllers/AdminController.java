package controllers;

import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.admin;

public class AdminController extends Controller{

    @Security.Authenticated(AdminSecured.class)
    public static Result toAdminPage() {
        return ok(admin.render(User.findByUsername(request().username())));
    }
}
