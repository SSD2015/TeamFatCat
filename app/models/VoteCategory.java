package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class VoteCategory extends Model {

    @Id
    private Long id;

    @Constraints.Required
    private String name;

    private static Finder<Long, VoteCategory> find = new Finder<Long, VoteCategory>(
            Long.class, VoteCategory.class);

    public static List<VoteCategory> findAll() {
        return find.all();
    }

    public static VoteCategory findById(long id) { return find.byId(id); }

    public static VoteCategory create(String name) {
        VoteCategory voteCat = new VoteCategory(name);
        voteCat.save();
        return voteCat;
    }

    public VoteCategory(String name){
        this.name = name;
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

    public boolean equals(VoteCategory other) {
        if (this.id == other.getId()) {
            return true;
        } else {
            return false;
        }
    }

    public String validate() {
        List<VoteCategory> voteList = find.all();
        if (this.name == null || this.name.equals("")) {
            return "Category name is required";
        }

        if (this.name.length() < 1 || this.name.length() > 30) {
            return "Category Name must not exceed 30 characters";
        }

        for (VoteCategory v: voteList) {
            if ((this.name).toLowerCase().equals(v.getName().toLowerCase())) {
                return "This category name is already used";
            }
        }

        return null;
    }

}
