package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
public class RateCategory extends Model {

    @Id
    private Long id;

    @Constraints.Required
    private String name;

    private static Finder<Long, RateCategory> find = new Finder<Long, RateCategory>(
            Long.class, RateCategory.class);


    public static List<RateCategory> findAll() {
        return find.all();
    }

    public List<Project> getBestFromCat() {
        List<Project> projects = Project.findAll();
        final RateCategory currentCat = this;

        Collections.sort(projects, new Comparator<Project>() {
            public int compare(Project o1, Project o2) {
                if (o1.getTotalScoresFromCat(currentCat) == o2.getTotalScoresFromCat(currentCat))
                    return 0;
                return o1.getTotalScoresFromCat(currentCat) > o2.getTotalScoresFromCat(currentCat) ? -1 : 1;
            }
        });
        return projects;
    }

    public static RateCategory create(String name) {
        RateCategory voteCat = new RateCategory(name);
        voteCat.save();
        return voteCat;
    }

    public RateCategory(String name){
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

    public boolean equals(RateCategory other) {
        if (this.id == other.getId()) {
            return true;
        } else {
            return false;
        }
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

}
