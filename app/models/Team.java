package models;

import play.db.ebean.Model;

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

    public String getTeamMembers() { return members; }

    public Long getId() { return id; }

    public long[] getMembers() {
        String[] list = this.members.split(",");
        long[] memberLists = new long[list.length];
        for( int i = 0 ; i < memberLists.length ; i++ )
            memberLists[i] = Long.parseLong(list[i]);
        return memberLists;
    }

    public void setMembers( long[] list ) {
        if (list.length <= 0) {
            return;
        }
        this.members += list[0];
        for( int i = 1 ; i < list.length ; i++ ) {
            this.members += "," + list[i];
        }
    }

    public void addMembers(User member) {
        this.members += "," + member.getId();
    }

    public static Finder<Long, Team> find = new Finder<Long, Team>(Long.class, Team.class);
}