package models;

import javax.persistence.*;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class Vote extends Model{
	@Id
	public Long id;
	
	@Constraints.Required
	public String userID;
	
	 @ManyToOne
	 @JoinColumn(name="category_id", referencedColumnName="id")
	 public VoteCategory category;
	 
	// Finder will help us easily query data from database.
	public static Finder<Long, Vote> find =
                new Finder<Long, Vote>(Long.class, Vote.class);

}
