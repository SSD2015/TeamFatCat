package models;

import javax.persistence.*;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import java.util.List;

@Entity
public class Project extends Model{
    @Id
    public int ProjectID;

    public String ProjectName;
    public String ProjectDesc;
    public List<String> ProjectMember;
    public List<String> ProjectScreenshot;

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
    //Project Member
    //set
    public void setProjectMember(List<String> projectMember) {
        this.ProjectMember = projectMember;
    }
    //add
    public void addProjectMember(String member){
        this.ProjectMember.add(member);
    }
    //remove
    public void removeProjectMember(String member){
        for(int i = 0 ; i < this.ProjectMember.size() ; i++){
            if((this.ProjectMember.get(i)).equals(member)){
                this.ProjectMember.remove(i);
            }
        }
    }
    //get
    public List<String> getProjectMember(){
        return this.ProjectMember;
    }
    //Project Screen shot
    //set
    public void setProjectScreenshot(List<String> projectScreenshot) {
        this.ProjectScreenshot = projectScreenshot;
    }
    //add
    public void addProjectScreenshot(String projectScreenshot){
        this.ProjectScreenshot.add(projectScreenshot);
    }
    //remove
    public void removeProjectScreenshot(String projectScreenshot){
        for(int i = 0 ; i < this.ProjectScreenshot.size() ; i++){
            if((this.ProjectScreenshot.get(i)).equals(projectScreenshot)){
                this.ProjectScreenshot.remove(i);
            }
        }
    }
    //get
    public List<String> getProjectScreenshot(){
        return this.ProjectScreenshot;
    }

}
