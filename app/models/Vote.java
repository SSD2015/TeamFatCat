package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import play.db.ebean.Model;

import javax.persistence.Entity;

@Entity
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class Vote extends Model{

}