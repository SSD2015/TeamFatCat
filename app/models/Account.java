package models;

import javax.persistence.*;

@Entity
@Table(name="account)  
public class Account extends Model{
	@Id
	// id = username
	public String id;

	@Constraints.Required
	public String password;

	@Version  
	Timestamp lastUpdate;  


	public static Finder<String, Account> find =
			new Finder<String, Account>(String.class, Account.class);


	public Timestamp getLastUpdate() {  
		return lastUpdate;  
	}  

	public void setLastUpdate(Timestamp lastUpdate) {  
		this.lastUpdate = lastUpdate;  
	}  
}
