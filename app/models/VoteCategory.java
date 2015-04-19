package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "vote_category")
public class VoteCategory extends Model{

}
