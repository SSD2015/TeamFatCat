package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
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

    public List<Long> getMemberList() {
        List<Long> memberLists = new ArrayList<Long>();
        if( this.members.length() <= 0 )
            return memberLists;
        String[] list = this.members.split(",");
        for (int i = 0; i < list.length ; i++ )
            memberLists.add( Long.parseLong( list[i] ) );
        return memberLists;
    }

    public List<User> getMembers(){
        List<User> users = new ArrayList<User>();
        if (this.members == null)
            return users;
        List<Long> list = this.getMemberList();
        for(int i = 0 ; i < list.size() ; i++) {
            users.add(User.findById(list.get(i)));
        }
        return users;
    }

    public void removeMember( long id ) {
        String[] list = this.members.split(",");
        String newMember = "";
        for (int i = 0 ; i < list.length ; i++) {
            if (!list[i].equals(String.valueOf(id))) {
                newMember += list[i];
            }
        }
        this.members = newMember;

//        List<User> lists = this.getMembers();
//        List<User> newLists = new ArrayList<User>();
//        for( User u: lists ) {
//            if( u.getId() != id ) {
//                newLists.add(u);
//            }
//        }
//        this.setMembers( newLists );
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

    public boolean addMember(User member) {

        
        if(count() == 0) {
            this.members = String.valueOf(member.getId());
        } else if (count() > 0) {
            String[] membersId = members.split(",");
            for (int i = 0 ; i < membersId.length ; i++) {
                if (String.valueOf(member.getId()).equals(membersId[i])) {
                    return false;
                }
            }

            this.members += "," + member.getId();
        }

        this.update();

        return true;
    }

    public void deleteNullMembers() {
        List<Long> list = getMemberList();
        for (int i = 0 ; i < list.size() ; i++) {
            if (User.findById(list.get(i)) == null) {
                removeMember(list.get(i));
            }
        }
    }

    public static List<Team> getAllTeams() {
        return find.all();
    }

    public static Team findById(long id) {
        return find.byId(id);
    }

    public static Team findByName(String name) {
        return find.where().eq("name", name).findUnique();
    }

    private static Finder<Long, Team> find = new Finder<Long, Team>(Long.class, Team.class);
}