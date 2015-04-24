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

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

    @ManyToOne
    @JoinColumn(name="project_id", referencedColumnName="id")
    private Project project;

    @ManyToOne
    @JoinColumn(name="category_id", referencedColumnName="id")
    private RateCategory category;

    @Constraints.Required
    private int score;

    @Version
    private Timestamp timestamp;

    private static Finder<Long, Rate> find = new Finder<Long, Rate>(Long.class, Rate.class);

    public static List<Rate> findAll() {
        return find.all();
    }

    public static Rate findById(long id) {
        return find.byId(id);
    }

    public static List<Rate> findByUser(User user) {
        return find.where().eq("user_id", user.getId()).findList();
    }

    public static List<Rate> findByProject(Project project) {
        return find.where().eq("project_id", project.getId()).findList();
    }

    public static List<Rate> findByRateCategory(RateCategory rateCategory) {
        return find.where().eq("category_id", rateCategory.getId()).findList();
    }

    public static List<Rate> findByProjectRateCategory(Project project, RateCategory rateCategory){
        return find.where().eq("project_id", project).eq("category_id", rateCategory.getId()).findList();
    }

    public static Rate findUnique(User user, Project project, RateCategory rateCategory) {
        return find.where().eq("user_id", user.getId()).eq("category_id", rateCategory.getId()).eq("project_id", project.getId()).findUnique();
    }

    public static void deleteByUser(User user) {
        List<Rate> rates = findByUser(user);
        for (Rate rate: rates) {
            rate.delete();
        }
    }

    public static void deleteByProject(Project project) {
        List<Rate> rates = findByProject(project);
        for (Rate rate: rates) {
            rate.delete();
        }
    }

    public static void deleteByRateCategory(RateCategory rateCategory) {
        List<Rate> rates = findByRateCategory(rateCategory);
        for (Rate rate: rates) {
            rate.delete();
        }
    }

    public static Rate create(User user, Project project, RateCategory rateCategory, int score) {
        if (score > 5 || score < -1) { return null; }

        Rate rate = findUnique(user, project, rateCategory);
        if (rate == null) {
            rate = new Rate(user, project, rateCategory, score);
        } else {
            rate.setScore(score);
        }

        return rate;
    }

    private Rate(User user, Project project, RateCategory rateCategory, int score) {
        this.user = user;
        this.project = project;
        this.category = rateCategory;
        this.score = score;
        this.timestamp = new Timestamp((new Date()).getTime());
        this.save();
    }

    public void setScore(int score) {
        this.score = score;
        this.updateTimestamp();
        this.update();
    }

    public void updateTimestamp() {
        timestamp = new Timestamp((new Date()).getTime());
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

    public RateCategory getCategory() { return category; }

    public int getScore() {
        return score;
    }

    public int getPercentScore() {
        return score*20;
    }

    public String getTimestamp() {
        return this.timestamp.toString();
    }

    public boolean equals(Rate other) {
        if (this.id == other.getId()) {
            return true;
        } else {
            return false;
        }
    }
}