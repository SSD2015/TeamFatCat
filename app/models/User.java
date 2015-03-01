package models;

import play.db.ebean.Model;
import play.data.validation.Constraints;
import javax.persistence.*;
import java.sql.Timestamp;

import java.util.List;

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

//	public static void delete(String username) {
//		find.ref(username).delete();
//	}

	public Timestamp getLastUpdate() {  
		return lastUpdate;  
	}  

	public void setLastUpdate(Timestamp lastUpdate) {  
		this.lastUpdate = lastUpdate;  
	} 

	public static User authenticate(String username, String password) {
		return null;
	}
}
