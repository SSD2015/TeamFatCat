package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import java.sql.Timestamp;

import java.util.List;
import java.util.Date;

@Entity
public class Vote extends Model{
    @Id
    private Long id;

    @Constraints.Required
    private int score;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

    @ManyToOne
    @JoinColumn(name="category_id", referencedColumnName="id")
    private VoteCategory category;

    @ManyToOne
    @JoinColumn(name="project_id", referencedColumnName="id")
    private Project project;

    @Version
    private Timestamp timestamp;

    // Finder will help us easily query data from database.
    private static Finder<Long, Vote> find = new Finder<Long, Vote>(Long.class, Vote.class);

    public static List<Vote> getAllVotes() {
        return find.all();
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCategory(VoteCategory category) {
        this.category = category;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setTimestamp() {
        timestamp = new Timestamp((new Date()).getTime());
    }

    public Long getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public User getUser() {
        return user;
    }

    public VoteCategory getCategory() {
        return category;
    }

    public Project getProject() {
        return project;
    }

    public String getTimestamp() {
        return this.timestamp.toString();
    }
}