package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import java.util.List;

@Entity
public class Project extends Model {

    @Id
    private long id;

    @Constraints.Required
    private String name;
    private String description;

    @OneToOne(mappedBy = "project", cascade = CascadeType.REMOVE)
    private Team team;

    private static Finder<Long, Project> find = new Finder<Long, Project>(Long.class, Project.class);

    public static List<Project> findAll() {
        return find.all();
    }

    public static Project findById(long id) {
        return find.byId(id);
    }

    public static Project findByName(String name) {
        return find.where().eq("name", name).findUnique();
    }

    public static Project create(String name) {
        Project project = new Project(name);
        project.save();
        return project;
    }

    public static Project create(String name, String description) {
        Project project = new Project(name, description);
        project.save();
        return project;
    }

    public Project(String name) {
        this.name = name;
    }

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBriefDesc() {
        if (this.description == null) {
            return "";
        }
        if( this.description.length()>20 )
            return this.description.substring(0,21)+"..";
        return this.description;
    }

    public boolean equals(Project other) {
        if (this.id == other.getId()) {
            return true;
        } else {
            return false;
        }
    }

    //==========================================================================================
    // NOT CLEAN
    //==========================================================================================
    public int getTotalVoteScores() {
        List<Vote> voteList = Vote.findByProject(this);
        int count = 0;
        for (int i = 0 ; i < voteList.size() ; i++) {
            if (voteList.get(i).getUser().getId() <= 42 && voteList.get(i).getUser().getId() >= 22) {
                count++;
            }
        }

        return count;
    }

    public double getPercentVoteScores() {
        List<Project> projects = find.all();
        double total = 0;
        for(int i = 0 ; i < projects.size() ; i++) {
            total += projects.get(i).getTotalVoteScores();
        }

        double percent =  getTotalVoteScores() / total * 10000;
        percent = Math.round(percent);
        percent = percent / 100;
        return percent;
    }

    public double getTotalScoresFromCat(RateCategory cat) {
        List<Rate> rateList = Rate.findByProjectRateCategory(this,cat);
        double total = 0;
        //int count = 0;
        for(int i = 0 ; i < rateList.size() ; i++) {
            if (rateList.get(i).getScore() != -1 && rateList.get(i).getUser().getId() <= 42 && rateList.get(i).getUser().getId() >= 22) {
                total += rateList.get(i).getScore();
                //count++;
            }
        }
        
//        total = total / count;
//        total = total * 100;
//        total = Math.round(total);
//        total = total / 100;
        return total;
    }

    public double getPercentFromCat(RateCategory cat) {
        List<Project> projects = find.all();
        double totalScores = 0;
        for(int i = 0 ; i < projects.size() ; i++) {
           totalScores += projects.get(i).getTotalScoresFromCat(cat);
        }

        double percent = getTotalScoresFromCat(cat) / totalScores * 10000;
        percent = Math.round(percent);
        percent = percent / 100;

        return percent;
    }

    public String validate() {
        if (this.name == null) {
            return "Project Name is required";
        }

        if (this.name.length() < 1 || this.name.length() > 20 ) {
            return "Project name must not exceed 20 characters";
        }

        if (this.description.length() < 1 ) {
            return "Description is required";
        }

        if (this.description.length() > 300){
            return "Maxmimum size is 300 characters";
        }

        if (find.where().eq("name", this.name).findUnique() != null) {
            return "This name has already used";
        }

        return null;
    }

    public long getAvatarId() {
        return Image.findAvatarId(this.id);
    }

}