package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Project extends Model{
    @Id
    public long id;

    private String projectName;
    private String projectDesc;

    @ManyToOne
    @JoinColumn(name="team_id", referencedColumnName="id")
    public Team team;

    // Finder will help us easily query data from database.
    public static Finder<Long, Project> find = new Finder<Long, Project>(Long.class, Project.class);

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
    //get
    public String getProjectDesc() { return this.projectDesc; }

    public long getTeamId() { return team.getId(); }

}
