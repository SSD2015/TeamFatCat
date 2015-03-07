package models;

import play.data.validation.Constraints;
//import play.db.ebean.Model;
import javax.persistence.*;
//import java.sql.Timestamp;

import javax.persistence.Id;

/**
 * Created by vince_000 on 7/3/2558.
 */
@Entity
public class Organizer extends User{

    // Id from user is Foreign key in this table

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private Long id = getId();

    private String major;
    private int year;

}
