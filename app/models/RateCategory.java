package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "vote_category")
public class RateCategory extends Model {

    @Id
    private Long id;

    @Constraints.Required
    private String name;

    private static Finder<Long, RateCategory> find = new Finder<Long, RateCategory>(
            Long.class, RateCategory.class);

    public RateCategory(String name){
        this.name = name;
    }
    public static List<RateCategory> all() {
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

    public static RateCategory findById(long id) {
        return find.byId(id);
    }

    public List<Project> getBestFromCat() {
        List<Project> Plist = Project.getAllProjects();
        List<Rate> Vlist = new ArrayList<Rate>();
        final RateCategory currentCat = this;

        Collections.sort(Plist, new Comparator<Project>() {
            public int compare(Project o1, Project o2) {
                if (o1.getAvgFromCat(currentCat) == o2.getAvgFromCat(currentCat))
                    return 0;
                return o1.getAvgFromCat(currentCat) > o2.getAvgFromCat(currentCat) ? -1 : 1;
            }
        });
        return Plist;
    }

    public static List<RateCategory> getAllCategories() {
        return find.all();
    }

    public String validate() {
        List<RateCategory> voteList = find.all();
        if (this.name == null || this.name.equals("")) {
            return "Category name is required";
        }

        if (this.name.length() < 1 || this.name.length() > 30) {
            return "Category Name must not exceed 30 characters";
        }

        for (RateCategory v: voteList) {
            if ((this.name).toLowerCase().equals(v.getName().toLowerCase())) {
                return "This category is already used";
            }
        }

        return null;
    }
    public static RateCategory create(String name) {
        RateCategory votecat = new RateCategory(name);
        votecat.save();
        return votecat;
    }

}
