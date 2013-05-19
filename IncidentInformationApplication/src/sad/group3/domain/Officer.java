package sad.group3.domain;

public class Officer {
	private String id;
	private String lastName;
	private String firstName;
	private String pwd;

	private String primary;

	public Officer() {
	}

	public Officer(String id, String lastName, String firstName, String pwd) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.pwd = pwd;
	}

	public String getPrimary() {
		return primary;
	}

	public void setPrimary(String primary) {
		this.primary = primary;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
