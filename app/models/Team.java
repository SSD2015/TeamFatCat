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

    public String getName(){
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
        List<User> users = new ArrayList<User>();
        if (this.members == null)
            return users;
        long[] list = this.getMembersList();
        for(int i = 0 ; i < list.length ; i++) {
            users.add(User.find.byId(list[i]));
        }

        return users;
    }

    public void removeMember( long id ) {
        List<User> lists = this.getMembers();
        List<User> newLists = new ArrayList<User>();
        for( User u: lists ) {
            if( u.getId() != id ) {
                newLists.add(u);
            }
        }
        this.setMembers( newLists );
    }

    public void setMembers( List<User> list ) {
        this.members = "";
        if (list.size() <= 0) {
            return;
        }
        this.members += list.get(0).getId();
        for( int i = 1 ; i < list.size() ; i++ ) {
            this.members += "," + list.get(i).getId();
        }

    }

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