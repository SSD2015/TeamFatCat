package models;

import play.db.ebean.Model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
//import play.db.ebean.Model;
//import java.sql.Timestamp;

@Entity
public class Team extends Model {

    @Id
    private Long id;

    private String name;
    private String members;


    public String getTeamName(){
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getTeamMembers() { return members; }

    public void setTeamMembers(String members) {
        this.members = "";
    }

    public int count(){
        if (members == null) {
            return 0;
        }
        return this.members.split(",").length;
    }

    public Long getId() { return id; }

    public long[] getMembersList() {
        String[] list = this.members.split(",");
        long[] memberLists = new long[list.length];
        for( int i = 0 ; i < memberLists.length ; i++ )
            memberLists[i] = Long.parseLong(list[i]);
        return memberLists;
    }

    public List<User> getMembers(){
        if (this.members == null) {
            return null;
        }
        List<User> users = new ArrayList<User>();
        long[] list = this.getMembersList();
        for(int i = 0 ; i < list.length ; i++) {
            users.add(User.find.byId(list[i]));
        }

        return users;
    }
/*
    public void setMembers( long[] list ) {
        if (list.length <= 0) {
            return;
        }
        this.members += list[0];
        for( int i = 1 ; i < list.length ; i++ ) {
            this.members += "," + list[i];
        }
    }
*/
    public void addMembers(User member) {
        if(count() > 0) {
            this.members += ",";
        }
        if(count() == 0) {
            this.members = member.getId() + "";
        }
        else
            this.members += member.getId();
        this.update();
    }

    public static Finder<Long, Team> find = new Finder<Long, Team>(Long.class, Team.class);
}