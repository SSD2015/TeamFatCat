package models;

import javax.persistence.*;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
@Table(name = "vote_category")
public class VoteCategory extends Model{
	@Id
	public Long id;
	public int score = 1;
	
	@Constraints.Required
	public String name;
	 
	public static Finder<Long, VoteCategory> find = new Finder<Long, VoteCategory>(
			Long.class, VoteCategory.class);

}
