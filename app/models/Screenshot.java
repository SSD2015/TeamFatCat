package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Screenshot extends Model{
    @Id
    public Long Screenshot_ID;

    private String TeamName;

    // Finder will help us easily query data from database.
    public static Finder< Long, Screenshot> find = new Finder< Long, Screenshot>( Long.class, Screenshot.class);

    public void setTeamName(String TeamName){
        this.TeamName = TeamName;
    }

    public String getTeamName(){
        return this.TeamName;
    }

}
