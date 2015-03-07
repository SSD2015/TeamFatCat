package models;

import javax.persistence.*;
//import play.db.ebean.Model;
//import java.sql.Timestamp;
/**
 * Created by Guro on 3/7/2015.
 */
public class Team {
    @Id
    private Long id;
    @OneToOne
    private String teamName;
    private String teamMembers;


    public String getTeamName(){
        return teamName;
    }

    public String getTeamMembers() { return this.teamMembers; }

    public long[] getMembersList() {
        String[] list = this.teamMembers.split(",");
        long[] longLists = new long[list.length];
        for( int i=0;i<longLists.length;i++ )
            longLists[i] = Long.parseLong(list[i]);
        return longLists;
    }


}