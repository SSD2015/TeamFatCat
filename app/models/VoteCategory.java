package models;

import javax.persistence.*;

import play.db.ebean.Model.Finder;

@Entity
@Table(name = "vote_category")
public class VoteCategory {
	@Id
	public Long id;
	public String name;
	public String userID;

	public static Finder<Long, VoteCategory> find = new Finder<Long, VoteCategory>(
			Long.class, VoteCategory.class);

}
