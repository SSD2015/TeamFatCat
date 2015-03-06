package models;

import play.db.ebean.Model;
import play.data.validation.Constraints;
import javax.persistence.*;
import java.sql.Timestamp;

import java.util.List;

@Entity
public class User extends Model {

    @Id
    public int id;

    @Constraints.Required
    public String username;
    public String password;

    public String firstName;
    public String lastName;
    public String major;
    public int year;
    public String team;

    @Version
    Timestamp lastUpdate;

    public static Finder<Integer, User> find = new Finder<Integer, User>(Integer.class, User.class);


    public static List<User> all() {
        return find.all();
    }

    public static void create(User user) {
        user.save();
    }

//	public static void delete(String username) {
//		find.ref(username).delete();
//	}
    
    public String getUsername(){
    	return username;
    }
    
    
    
    public String getTeamName(){
    	return team;
    }
    
    public void setTeamName(String team){
    	this.team = team;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public static User authenticate(String username, String password) {
        return find.where().eq("username", username).eq("password", password).findUnique();
    }
}