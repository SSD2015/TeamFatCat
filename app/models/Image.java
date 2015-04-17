package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Image extends Model{
    @Id
    private long id;

    private String url;

    // Finder will help us easily query data from database.
    public static Finder< Long, Image> find = new Finder< Long, Image>( Long.class, Image.class);

    public void setUrl( String url ){
        this.url = url;
    }
    public long getId(){
        return this.id;
    }
    public static Image findById(long id) {
        return find.byId(id);
    }

    public String getUrl(){
        return this.url;
    }


}