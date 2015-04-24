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

    public static List<Vote> findAll() {
        return find.all();
    }

    //could return null outside
    public static Vote findByUser(User user) {
        return find.where().eq("user", user).findUnique();
    }

    public static List<Vote> findByProject(Project project) {
        return find.where().eq("project", project).findList();
    }

    public static List<Project> findBestProject() {
        List<Project> projects = Project.findAll();

        Collections.sort(projects, new Comparator<Project>() {
            public int compare(Project o1, Project o2) {
                if (o1.getTotalVoteScores() == o2.getTotalVoteScores())
                    return 0;
                return o1.getTotalVoteScores() > o2.getTotalVoteScores() ? -1 : 1;
            }
        });

        return projects;
    }

    public static void deleteByUser(User user) {
        List<Vote> votes = find.where().eq("user", user).findList();
        for (Vote vote: votes) {
            vote.delete();
        }
    }

    public static void deleteByProject(Project project) {
        List<Vote> votes = find.where().eq("project", project).findList();
        for (Vote vote: votes) {
            vote.delete();
        }
    }

    public static Vote create(User user, Project project) {
        Vote vote = find.where().eq("user", user).findUnique();
        if (vote == null) {
            vote = new Vote(user, project);
        } else {
            vote.setProject(project);
            vote.updateTimestamp();
            vote.update();
        }

        return vote;
    }

    private Vote(User user, Project project) {
        this.user = user;
        this.project = project;
        this.timestamp = new Timestamp((new Date()).getTime());
        this.save();
    }

    public void setProject(Project project) {
        this.project = project;
        this.updateTimestamp();
        this.update();
    }

    public void updateTimestamp() {
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

    public boolean equals(Vote other) {
        if (this.id == other.getId()) {
            return true;
        } else {
            return false;
        }
    }
}