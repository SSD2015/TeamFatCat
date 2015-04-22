package models;

import org.mindrot.jbcrypt.BCrypt;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
public class User extends Model {

    public static final int ADMIN = 99;
    public static final int STUDENT = 1;
    public static final int ORGANIZER = 2;
    public static final int INSTRUCTOR = 3;

    @Id
    private long id;

    @Constraints.Required
    private String username;
    @Constraints.Required
    private String password;

    @Constraints.Required
    private String firstName;
    @Constraints.Required
    private String lastName;

    @Constraints.Required
    private int type;

    @ManyToOne
    @JoinColumn(name="team_id", referencedColumnName="id")
    private Team team;

    @Version
    Timestamp lastUpdate;

    private static Finder<Long, User> find = new Finder<Long, User>(Long.class, User.class);

    public static List<User> findAll() {
        return find.all();
    }

    public static List<User> findAdmin() {
        return find.where().eq("type", User.ADMIN).findList();
    }

    public static List<User> findStudent() {
        return find.where().eq("type", User.STUDENT).findList();
    }

    public static List<User> findOrganizer() {
        return find.where().eq("type", User.ORGANIZER).findList();
    }

    public static List<User> findInstructor() {
        return find.where().eq("type", User.INSTRUCTOR).findList();
    }

    public static List<User> findStudentNotInTeam() { return find.where().eq("type", User.STUDENT).eq("team", null).findList(); }

    public static User findById(long id) {
        return find.byId(id);
    }

    public static User findByUsername(String username) {
        return find.where().eq("username", username).findUnique();
    }

    public static List<User> findByTeam(Team team) {
        return find.where().eq("team_id", team.getId()).findList();
    }

    public static User authenticate(String username, String password) {
        User user = find.where().eq("username", username).findUnique();
        if (user != null && BCrypt.checkpw(password, user.password)) {
            return user;
        }

        return null;
    }

    public static User create(String username, String password, String firstName, String lastName, int type) {
        if (User.findByUsername(username) == null) {
            User user = new User(username, password, firstName, lastName, type);
            user.save();
            return user;
        }

        return null;
    }

    private User(String username, String password, String firstName, String lastName, int type) {
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
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

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setLastUpdate() {
        this.lastUpdate = new Timestamp((new Date()).getTime());
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

    public String getTypeName() {
        if (this.type == ADMIN) {
            return "Admin";
        } else if (this.type == STUDENT) {
            return "Student";
        } else if (this.type == ORGANIZER) {
            return "Organizer";
        } else if (this.type == INSTRUCTOR) {
            return "Instructor";
        } else {
            return "Error";
        }
    }

    public int getType() {
        return this.type;
    }

    public boolean checkTeam(Team team) {
        if (team == this.team) { return true; }

        return false;
    }

    public boolean checkProject(Project project) {
        if (this.team != null) { return team.checkProject(project); }

        return false;
    }

    public boolean equals(User other) {
        if (this.id == other.getId()) {
            return true;
        } else {
            return false;
        }
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

        if (this.username.length() < 5 || this.username.length() > 20) {
            return "Username should have 5-20 characters";
        }

        for (User u: userList) {
            if ((this.username).toLowerCase().equals(u.getUsername().toLowerCase())) {
                return "This username is already used";
            }
        }

        return null;
    }
}