package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Team extends Model {

    @Id
    private Long id;

    @Constraints.Required
    private String name;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="project_id", referencedColumnName="id", nullable = true)
    public Project project;

    @OneToMany(mappedBy = "team")
    public List<User> users;

    private static Finder<Long, Team> find = new Finder<Long, Team>(Long.class, Team.class);

    public static List<Team> findAll() {
        return find.all();
    }

    public static List<Team> findTeamWithNoProject() { return find.where().eq("project", null).findList(); }

    public static Team findById(long id) {
        return find.byId(id);
    }

    public static Team findByName(String name) {
        return find.where().eq("name", name).findUnique();
    }

    public static Team findByProject(Project project) {
        return find.where().eq("project", project).findUnique();
    }

    public Team(String name) {
        this.name = name;
    }

    public static Team create(String name) {
        Team team = new Team(name);
        team.save();
        return team;
    }

    @Override
    public void delete() {
        for (User user: users) {
            user.setTeam(null);
            user.update();
        }
        this.update();

        super.delete();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Project getProject() {
        return project;
    }

    public boolean checkProject(Project project) {
        if (project == this.project) { return true; }
        return false;
    }

    public boolean equals(Team other) {
        if (this.id == other.getId()) {
            return true;
        } else {
            return false;
        }
    }

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