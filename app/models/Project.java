package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Project extends Model{
    @Id
    public int id;

    public String ProjectName;
    public String ProjectDesc;

    // Finder will help us easily query data from database.
    public static Finder<Integer, Project> find = new Finder<Integer, Project>(Integer.class, Project.class);

    //Project Name
    //set
    public void setProjectName(String projectName) {
        this.ProjectName = projectName;
    }
    //get
    public String getProjectName(){
        return this.ProjectName;
    }

    //Project Desc
    //set
    public void setProjectDesc(String description){
        this.ProjectDesc = description;
    }
    //get
    public String getProjectDesc() { return this.ProjectDesc; }
}
