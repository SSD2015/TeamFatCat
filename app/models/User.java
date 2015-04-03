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
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
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
        if (this.username == null) {
            return "Username is required";
        }
        if (this.password == null) {
            return "Password is required";
        }
        if (this.firstName == null) {
            return "First Name is required";
        }
        if (this.lastName == null) {
            return "Last Name is required";
        }

        char x;

        for (int i = 0 ; i < this.username.length() ; i++) {
            x = username.charAt(i);
            if (!((x >= 'A' && x <= 'Z') || (x >= 'a' && x <= 'z') || (x >= '0' && x <= '9'))) {
                return "Username should not contain special characters";
            }
        }

        for (int i = 0 ; i < this.firstName.length() ; i++) {
            x = firstName.charAt(i);
            if (!((x >= 'A' && x <= 'Z') || (x >= 'a' && x <= 'z') || (x >= '0' && x <= '9'))) {
                return "First name should not contain special characters";
            }
        }

        for (int i = 0 ; i < this.lastName.length() ; i++) {
            x = lastName.charAt(i);
            if (!((x >= 'A' && x <= 'Z') || (x >= 'a' && x <= 'z') || (x >= '0' && x <= '9'))) {
                return "Last name should not contain special characters";
            }
        }


        if (this.username.length() < 6 || this.username.length() > 20) {
            return "Username should have 6-20 characters";
        }

        for (User u: userList) {
            if ((this.username).toLowerCase().equals(u.getUsername().toLowerCase())) {
                return "This username is already used";
            }
        }

        return null;
    }
}