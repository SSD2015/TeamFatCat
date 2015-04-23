package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.List;

@Entity
public class Project extends Model{
    @Id
    private long id;

    private String projectName;
    private String projectDesc;

    private long teamId;

    // Finder will help us easily query data from database.
    private static Finder<Long, Project> find = new Finder<Long, Project>(Long.class, Project.class);


    public Project(String projectName, String projectDesc, long teamId){
        this.projectName = projectName;
        this.projectDesc = projectDesc;
        this.teamId = teamId;
    }

    //Project Name
    //set
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    //get
    public String getProjectName(){
        return this.projectName;
    }

    //Project Desc
    //set
    public void setProjectDesc(String description){
        this.projectDesc = description;
    }
    public void setTeamId(long id){
        this.teamId = id;
    }
    //get
    public String getProjectDesc() { return this.projectDesc; }
    public String getBriefDesc() {
        if (this.projectDesc == null) {
            return "IT'S NULL SUS!";
        }
        if( this.projectDesc.length()>20 )
            return this.projectDesc.substring(0,21)+"..";
        return this.projectDesc;
    }

    public long getId() { return id; }
    public long getTeamId() { return teamId; }

    public static List<Project> getAllProjects() {
        return find.all();
    }

    public static Project findById(long id) {
        return find.byId(id);
    }

    public double getToalVoteScores() {
        List<Vote> voteList = Vote.getVoteFromProject(this);
        double count = 0;
        for (int i = 0 ; i < voteList.size() ; i++) {
            if (voteList.get(i).getUser().getId() <= 42 && voteList.get(i).getUser().getId() >= 2) {
                count++;
            }
        }

        return voteList.size();
    }

    public double getPercentVoteScores() {
        List<Project> projects = find.all();
        double total = 0;
        for(int i = 0 ; i < projects.size() ; i++) {
            total += projects.get(i).getToalVoteScores();
        }

        double percent =  getToalVoteScores() / total * 10000;
        percent = Math.round(percent);
        percent = percent / 100;
        return percent;
    }

    public double getAvgFromCat(RateCategory cat) {
        List<Rate> rateList = Rate.getProjectAndCatRate(this,cat);
        double total = 0;
        for(int i = 0 ; i < rateList.size() ; i++) {
            if (rateList.get(i).getScore() != -1 && rateList.get(i).getUser().getId() <= 42 && rateList.get(i).getUser().getId() >= 2) {
                total += rateList.get(i).getScore();
                count++;
            }
        }
        
        total = total / rateList.size();
        total = total * 100;
        total = Math.round(total);
        total = total / 100;
        return total;
    }

    public double getAvg(){
        List<Rate> rateList = Rate.getProjectRate(this);
        double total=0;
        for(int i=0 ; i<rateList.size() ; i++){
            total += rateList.get(i).getScore();
        }
        total = total / rateList.size();
        total = total *100;
        total = Math.round(total);
        total = total /100;
        return total;
    }

    public double getPercentFromCat(RateCategory cat) {
        List<Project> projects = find.all();
        double totalScores = 0;
        for(int i = 0 ; i < projects.size() ; i++) {
           totalScores += projects.get(i).getAvgFromCat(cat);
        }

        double percent = getAvgFromCat(cat) / totalScores * 10000;
        percent = Math.round(percent);
        percent = percent / 100;

        return percent;
    }

    public String validate() {
        List<Project> proList = find.all();
        if (this.projectName == null) {
            return "Project Name is required";
        }

        if (this.projectName.length() < 1 || this.projectName.length() > 20 ) {
            return "Project name must not exceed 20 characters";
        }

        if (this.projectDesc.length() < 1 ) {
            return "Description is required";
        }

        if (this.projectDesc.length() > 300){
            return "Maxmimum size is 300 characters";
        }

        for (Project t: proList) {
            if ((this.projectName).toLowerCase().equals(t.getProjectName().toLowerCase())) {
                return "This name is already used";
            }
        }

        return null;
    }
    public static Project create(String projectName, String projectDesc, long teamId) {
        Project project = new Project(projectName, projectDesc,teamId);
        project.save();
        return project;
    }

    public long getAvatarId() {
        return Image.findAvatarId(this.id);
    }

}