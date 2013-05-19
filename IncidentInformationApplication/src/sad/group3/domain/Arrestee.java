package sad.group3.domain;

import java.util.List;

public class Arrestee {
	private String arresteeNum;
	private String firstName;
	private String lastName;
	private String gender;
	private String birthdate;
	private String phoneNum;
	private String height;
	private String weight;
	private String race;
	private String hairColor;
	private byte[] photo;
	private String otherCharacteristics;
	private String licenseID;

	private List<Incident> incidents;
	private List<Charge> charges;

	public Arrestee() {
	}

	public Arrestee(String arresteeNum, String firstName, String lastName,
			String gender, String birthdate, String phoneNum, String height,
			String weight, String race, String hairColor, byte[] photo,
			String otherCharacteristics, String licenseID,
			List<Incident> incidents, List<Charge> charges) {
		this.arresteeNum = arresteeNum;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthdate = birthdate;
		this.phoneNum = phoneNum;
		this.height = height;
		this.weight = weight;
		this.race = race;
		this.hairColor = hairColor;
		this.photo = photo;
		this.otherCharacteristics = otherCharacteristics;
		this.licenseID = licenseID;
		this.incidents = incidents;
		this.charges = charges;
	}

	public String getLicenseID() {
		return licenseID;
	}

	public void setLicenseID(String licenseID) {
		this.licenseID = licenseID;
	}

	public List<Charge> getCharges() {
		return charges;
	}

	public void setCharges(List<Charge> charges) {
		this.charges = charges;
	}

	public List<Incident> getIncidents() {
		return incidents;
	}

	public void setIncidents(List<Incident> incidents) {
		this.incidents = incidents;
	}

	public String getArresteeNum() {
		return arresteeNum;
	}

	public void setArresteeNum(String arresteeNum) {
		this.arresteeNum = arresteeNum;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getHairColor() {
		return hairColor;
	}

	public void setHairColor(String hairColor) {
		this.hairColor = hairColor;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getOtherCharacteristics() {
		return otherCharacteristics;
	}

	public void setOtherCharacteristics(String otherCharacteristics) {
		this.otherCharacteristics = otherCharacteristics;
	}

}
