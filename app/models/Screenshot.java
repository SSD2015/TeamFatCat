package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Screenshot extends Model{
    @Id
    private long Screenshot_ID;

    private long projectId;

    // Finder will help us easily query data from database.
    public static Finder< Long, Screenshot> find = new Finder< Long, Screenshot>( Long.class, Screenshot.class);

    public void setProject( long id ){
        this.projectId = id;
    }

    public long getProjectId(){
        return this.projectId;
    }

}
