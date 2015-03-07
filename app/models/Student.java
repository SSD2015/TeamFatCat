package models;

import play.data.validation.Constraints;

import javax.persistence.*;
//import play.db.ebean.Model;
//import java.sql.Timestamp;

import javax.persistence.*;

/**
 * Created by vince_000 on 7/3/2558.
 */

@Entity
public class Student extends User{

    // Id from user is Foreign key in this table

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private Long id = getId();

    private String major;
    private int year;
    private Team team;

    public Team getTeamName(){
        return team;
    }


}
