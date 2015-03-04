package models;

import javax.persistence.*;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class Vote extends Model{
    @Id
    public Long id;
    public int score = 1;

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
    public static Finder<Long, Vote> find =
            new Finder<Long, Vote>(Long.class, Vote.class);

}
