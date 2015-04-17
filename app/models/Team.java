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
        if(this.members == null || this.members.length() <= 0)
            return memberLists;
        String[] list = this.members.split(",");
        for (int i = 0; i < list.length ; i++ )
            try {
                memberLists.add(Long.parseLong(list[i]));
            } catch(Exception e) {

            }
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


    public String validate() {
        List<Team> teamList = find.all();
        if (this.name == null) {
            return "Team Name is required";
        }

        if (this.name.length() < 1 || this.name.length() > 20) {
            return "Team name must not exceed 20 characters";
        }

        for (Team t: teamList) {
            if ((this.name).toLowerCase().equals(t.getName().toLowerCase())) {
                return "This name is already used";
            }
        }

        return null;
    }
}