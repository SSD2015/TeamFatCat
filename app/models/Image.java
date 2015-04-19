package models;

import play.db.ebean.*;

import play.data.validation.Constraints;
import javax.persistence.*;
import java.io.*;

import java.util.List;

@Entity
public class Image extends Model {
    @Id
    private long id;

    @Constraints.Required
    private String name;
    //private String url;

    @Lob
    private byte[] data;


    private long projectId;

    private static Finder< Long, Image> find = new Finder< Long, Image>( Long.class, Image.class);

    private Image(String name, File img, long projectId) {
        this.name = name;
        this.data = new byte[(int)img.length()];
        this.projectId = projectId;

        InputStream inStream = null;
        try {
            inStream = new BufferedInputStream(new FileInputStream(img));
            inStream.read(this.data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //this.save();
    }

    public static Image create(String name, File img, Long projectId) {
        if (name.equals("avatar")) {
            Image image = find.where().eq("name", "avatar").eq("projectId", projectId).findUnique();
            if (image == null) {
                image = new Image("avatar", img, projectId);
            } else {
                image.setData(img);
            }
        }

        Image image = new Image("screenshot", img, projectId);
        image.save();
        image.setName(image.getName() + image.getId());
        image.update();
        return image;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(File img) {
        this.data = new byte[(int)img.length()];;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }

    public static Image findAvatar(long projectId) {
        return find.where().eq("name", "avatar").eq("projectId", projectId).findUnique();
    }

    public static long findAvatarId(long projectId) {
        Image image = find.where().eq("name", "avatar").eq("projectId", projectId).findUnique();
        if (image == null) {
            return -1;
        } else {
            return image.getId();
        }
    }

    public long getProjectId() {
        return projectId;
    }

    public static Image findById(long id) {
        return find.byId(id);
    }

    public static List<Image> findImagesByProject(long projectId) {
        return find.where().eq("projectId", projectId).findList();
    }
}