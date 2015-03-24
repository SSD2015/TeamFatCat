package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
//import play.db.ebean.Model;
//import java.sql.Timestamp;

@Entity
public class Team extends Model {

    @Id
    private Long id;

    private String teamName;
    private String teamMembers;


    public String getTeamName(){
        return teamName;
    }

    public String getTeamMembers() { return this.teamMembers; }

    public Long getId() { return id; }

    public long[] getMembersList() {
        String[] list = this.teamMembers.split(",");
        long[] longLists = new long[list.length];
        for( int i=0;i<longLists.length;i++ )
            longLists[i] = Long.parseLong(list[i]);
        return longLists;
    }

    public void setMembers( long[] list ) {
        String teamMembers = "";
        for( int i=0;i<list.length-1;i++ )
            teamMembers += list[i]+",";
        teamMembers += list[list.length-1]+"";
        this.teamMembers = teamMembers;
    }

    public static Finder<Long, Team> find = new Finder<Long, Team>(Long.class, Team.class);

}