package models;

import org.mindrot.jbcrypt.BCrypt;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
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

    public void clearAll() {
        find.all().clear();
    }

    public boolean checkPassword(String candidate) {
        return BCrypt.checkpw(candidate, this.password);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());;
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

    public String getPassword() {
        return this.password;
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
        User user = find.where().eq("username", username).findUnique();
        if (user != null && BCrypt.checkpw(password, user.password)) {
            return user;
        }

        return null;
    }

    public String validate() {
        List<User> userList = find.all();
        for (User u: userList) {
            if (username.equals(u.getUsername())) {
                return "This username is already used";
            }
        }

        return null;
    }
}