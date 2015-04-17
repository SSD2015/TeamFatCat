package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Image extends Model{
    @Id
    private long id;
    private String name;
    private String url;

    private long projectId;

    // Finder will help us easily query data from database.
    public static Finder< Long, Image> find = new Finder< Long, Image>( Long.class, Image.class);


    public void setProjectId(long id){
        this.projectId = id;
    }
    public long getProjectId(){
        return this.projectId;
    }

    public static List<Image> getByProjectId(long id){
        List<Image> images = getAllImage();
        List<Image> imagesSpec = new ArrayList<Image>();
        for(int i = 0 ; i < images.size() ; i++){
            if(images.get(i).getProjectId() == id){
                imagesSpec.add(images.get(i));
            }
        }

        return imagesSpec;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setUrl( String url ){
        this.url = url;
    }
    public long getId(){
        return this.id;
    }
    public static Image findByName(String name){
        List<Image> images = getAllImage();
        for(int i = 0 ; i < images.size() ; i++ ){
            if(images.get(i).getName().equals(name)){
                return images.get(i);
            }
        }
        return new Image();
    }
    public static Image findById(long id) {
        return find.byId(id);
    }

    public String getUrl(){
        return this.url;
    }

    public static List<Image> getAllImage(){
        return find.all();
    }


}