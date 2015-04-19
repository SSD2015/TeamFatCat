package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class Rate extends Model{
    @Id
    private Long id;

    @Constraints.Required
    private int score;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

    @ManyToOne
    @JoinColumn(name="category_id", referencedColumnName="id")
    private RateCategory category;

    @ManyToOne
    @JoinColumn(name="project_id", referencedColumnName="id")
    private Project project;

    @Version
    private Timestamp timestamp;

    // Finder will help us easily query data from database.
    private static Finder<Long, Rate> find = new Finder<Long, Rate>(Long.class, Rate.class);

    public static List<Rate> getAllRates() {
        return find.all();
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCategory(RateCategory category) {
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

    public int getPercentScore() {
        return score*20;
    }

    public User getUser() {
        return user;
    }

    public RateCategory getCategory() {
        return category;
    }

    public Project getProject() {
        return project;
    }

    public String getTimestamp() {
        return this.timestamp.toString();
    }

    public static List<Rate> getProjectAndCatRate(Project proj,RateCategory cat){
        return Rate.find.where().eq("category",cat).eq("project",proj).findList();
    }

    public static Rate getUniqueRate(String userName, Project project, RateCategory ratecat){
        User user = User.findByUsername(userName);
        Rate rate = Rate.find.where().eq("category",ratecat).eq("project",project).eq("user",user).findUnique();
        return rate;
    }

    public static List<Rate> getProjectRate(Project proj){
        return Rate.find.where().eq("project",proj).findList();
    }
}