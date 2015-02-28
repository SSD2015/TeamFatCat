package models;

/**
 * Created by NAGISAMA on 2/28/15 AD.
 */

import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Result;

import controllers.*;

import javax.persistence.Entity;
import javax.persistence.Id;

import static play.data.Form.form;
import static play.mvc.Controller.session;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.redirect;

@Entity
public class User extends Model {

    @Id
    public String username;
    public String name;
    public String password;

    public User(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public static Finder<String, User> find = new Finder<String, User>(
            String.class, User.class
    );

    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(Login.render(loginForm));
        } else {
            session().clear();
            session("username", loginForm.get().username);
            return redirect(
                    routes.Application.index()
            );
        }
    }

}