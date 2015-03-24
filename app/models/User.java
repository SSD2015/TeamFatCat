package models;

import play.db.ebean.Model;
import play.data.validation.Constraints;
import javax.persistence.*;
import java.sql.Timestamp;

import java.util.List;

@Entity
public class User extends Model {

    @Id
    private long id;

    @Constraints.Required
    private String username;
    @Constraints.Required
    private String password;

    private String firstName;
    private String lastName;

    private int type;

    @Version
    Timestamp lastUpdate;

    public static Finder<Long, User> find = new Finder<Long, User>(Long.class, User.class);

    public static List<User> all() {
        return find.all();
    }

//	public static void delete(String username) {
//		find.ref(username).delete();
//	}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId(){ return id; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getUsername(){
        return username;
    }

    public String getPassword(int pw) {
        if (pw == 1234) {
            return this.password;
        } else {
            return "no permission";
        }
    }

    public int getType() {
        return this.type;
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