package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "vote_category")
public class VoteCategory extends Model{
    @Id
    private Long id;

    @Constraints.Required
    private String name;

    private static Finder<Long, VoteCategory> find = new Finder<Long, VoteCategory>(
            Long.class, VoteCategory.class);

    public static List<VoteCategory> all() {
        return find.all();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

}
