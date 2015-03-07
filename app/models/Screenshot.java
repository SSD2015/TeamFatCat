package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Screenshot extends Model{
    @Id
    public int Screenshot_ID;

    public String TeamName;

    // Finder will help us easily query data from database.
    public static Finder<Integer, Project> find = new Finder<Integer, Project>(Integer.class, Project.class);

    public void setTeamName(String TeamName){
        this.TeamName = TeamName;
    }

    public String getTeamName(){
        return this.TeamName;
    }

}
