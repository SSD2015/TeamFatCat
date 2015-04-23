package controllers;

import models.User;
import play.Logger;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.user;

import java.util.List;

import static play.libs.Json.toJson;

public class UserController extends Controller {

    @Security.Authenticated(AdminSecured.class)
    public static Result toAddUserPage() {
        List<User> userList = User.getAllUsers();

        response().setHeader("Cache-Control","no-cache");
        return ok(user.render(Form.form(User.class), userList, User.findByUsername(request().username())));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result addUser() {
        Form<User> userForm = Form.form(User.class).bindFromRequest();
        if (userForm.hasErrors()) {
            List<User> userList = User.getAllUsers();

            Logger.error("[ " + request().username() + " ] fail to add user.");
            response().setHeader("Cache-Control", "no-cache");
            return badRequest(user.render(userForm, userList, User.findByUsername(request().username())));
        }
        User user = userForm.get();
        user.save();
        Logger.info("[ " + request().username() + " ] has add user [ #" + user.getId() + " ]");
        return redirect(routes.UserController.toAddUserPage());
    }
}
