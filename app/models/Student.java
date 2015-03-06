package models;

import play.data.validation.Constraints;

import javax.persistence.*;
//import play.db.ebean.Model;
//import java.sql.Timestamp;

import javax.persistence.*;

/**
 * Created by vince_000 on 7/3/2558.
 */
public class Student extends User{

    // Id from user is Foreign key in this table
    public String major;
    public int year;
    public String team;

    public String getTeamName(){
        return team;
    }

    public void setTeamName(String team){
        this.team = team;
    }

}
