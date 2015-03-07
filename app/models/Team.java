package models;

import javax.persistence.*;
//import play.db.ebean.Model;
//import java.sql.Timestamp;
/**
 * Created by Guro on 3/7/2015.
 */
public class Team {
    @Id
    private Long id;
    @OneToOne
    private Project project;
    private String teamName;


    public String getTeamName(){
        return teamName;
    }

    public Project getProject(){
        return project;
    }


}
