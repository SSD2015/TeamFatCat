package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import play.db.ebean.Model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class Vote extends Model{

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

    @ManyToOne
    @JoinColumn(name="project_id", referencedColumnName="id")
    private Project project;

    @Version
    private Timestamp timestamp;

    private static Finder<Long, Vote> find = new Finder<Long, Vote>(Long.class, Vote.class);

    private Vote(User user, Project project) {
        this.user = user;
        this.project = project;
        this.timestamp = new Timestamp((new Date()).getTime());
        this.save();
    }

    public static Vote create(User user, Project project) {
        Vote vote = find.where().eq("user", user).findUnique();
        if (vote == null) {
            vote = new Vote(user, project);
        } else {
            vote.setProject(project);
            vote.update();
        }

        return vote;
    }

    public void setProject(Project project) {
        this.project = project;
        this.timestamp = new Timestamp((new Date()).getTime());
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Project getProject() {
        return project;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public static Project findSelectProject(User user) {
        if (find.where().eq("user", user).findUnique() == null) {
            return null;
        }
        return find.where().eq("user", user).findUnique().getProject();
    }

    public static List<Vote> getVoteFromProject(Project project) {
        return find.where().eq("project_id", project.getId()).findList();
    }

    public static List<Project> getBestProject() {
        List<Project> projects = Project.getAllProjects();

        Collections.sort(projects, new Comparator<Project>() {
            public int compare(Project o1, Project o2) {
                if (o1.getToalVoteScores() == o2.getToalVoteScores())
                    return 0;
                return o1.getToalVoteScores() > o2.getToalVoteScores() ? -1 : 1;
            }
        });
        return projects;
    }

    public static List<Vote> getAllVotes() {
        return find.all();
    }
}