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
    private long id = getId();

    private String major;
    private int year;
    private long teamId;
    private long projectId;

    public String getMajor() {
        return major;
    }

    public int getYear() {
        return year;
    }

    public long getTeamId() {
        return teamId;
    }

    public long getProjectId() {
        return projectId;
    }
}
