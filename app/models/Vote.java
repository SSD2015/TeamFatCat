package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.util.List;

@Entity
public class Vote extends Model{
    @Id
    public Long id;

    @Constraints.Required
    public int score;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    public User user;

    @ManyToOne
    @JoinColumn(name="category_id", referencedColumnName="id")
    public VoteCategory category;

    @ManyToOne
    @JoinColumn(name="project_id", referencedColumnName="id")
    public Project project;

    // Finder will help us easily query data from database.
    private static Finder<Long, Vote> find = new Finder<Long, Vote>(Long.class, Vote.class);

    public static List<Vote> getAllVotes() {
        return find.all();
    }

}
