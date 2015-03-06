package models;

import play.data.validation.Constraints;
//import play.db.ebean.Model;
//import javax.persistence.*;
//import java.sql.Timestamp;

import javax.persistence.Id;

/**
 * Created by vince_000 on 7/3/2558.
 */
public class Organizer extends User{

    // Id from user is Foreign key in this table

    public String major;
    public int year;

}
