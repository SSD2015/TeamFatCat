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

	@Constraints.Required
	public String password;

	public String firstname;
	public String lastname;
	public String major;
	public int year;
	public String team;

	@JoinColumn(name="username", referencedColumnName="id")

	@Version  
	Timestamp lastUpdate;  


	public User(String username, String password, String firstname, String lastname,
			String major, int year, String team) {
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.major = major;
		this.year = year;
		this.team = team;
	}

	public static Finder<String, User> find = new Finder<String, User>(String.class, User.class);


	public static List<User> all() {
		return find.all();
	}

	public static void create(User user) {
		user.save();
	}

	public static void delete(String username) {
		find.ref(username).delete();
	}

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

	public Timestamp getLastUpdate() {  
		return lastUpdate;  
	}  

	public void setLastUpdate(Timestamp lastUpdate) {  
		this.lastUpdate = lastUpdate;  
	} 

}