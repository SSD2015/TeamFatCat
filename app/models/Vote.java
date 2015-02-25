package models;

import javax.persistence.*;
import play.db.ebean.Model.Finder;

@Entity
public class Vote {
	@Id
	public Long id;
	public String projectName;
	
	// Finder will help us easily query data from database.
	public static Finder<Long, Vote> find =
                new Finder<Long, Vote>(Long.class, Vote.class);

}
