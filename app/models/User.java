package models;

import play.db.ebean.Model;
import play.data.validation.Constraints;
import javax.persistence.*;
import java.sql.Timestamp;

import java.util.List;

@Entity
public class User extends Model {

    @Id
    private Long id;

    @Constraints.Required
    private String username;
    @Constraints.Required
    private String password;

    private String firstName;
    private String lastName;

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
    public Long getId(){ return id; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getUsername(){
        return username;
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