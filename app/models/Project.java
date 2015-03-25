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

    private long teamId;

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
    public void setTeamId(long id){
        this.teamId = id;
    }
    //get
    public String getProjectDesc() { return this.projectDesc; }
    public String getBriefDesc() {
        if( this.projectDesc.length()>20 )
            return this.projectDesc.substring(0,21)+"..";
        return this.projectDesc;
    }

    public long getId() { return id; }
    public long getTeamId() { return teamId; }

}
