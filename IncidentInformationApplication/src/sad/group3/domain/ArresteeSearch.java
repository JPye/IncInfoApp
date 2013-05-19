package sad.group3.domain;

public class ArresteeSearch {
	private String ap_num;
	private String ap_firstname;
	private String ap_lastname;
	private String ap_gender;
	private String race_num;
	private String ap_phonenum;

	public ArresteeSearch() {
	}

	public ArresteeSearch(String apNum, String apFirstname, String apLastname,
			String apGender, String raceNum, String apPhonenum) {
		ap_num = apNum;
		ap_firstname = apFirstname;
		ap_lastname = apLastname;
		ap_gender = apGender;
		race_num = raceNum;
		ap_phonenum = apPhonenum;
	}

	public String getAp_phonenum() {
		return ap_phonenum;
	}

	public void setAp_phonenum(String apPhonenum) {
		ap_phonenum = apPhonenum;
	}

	public String getAp_num() {
		return ap_num;
	}

	public void setAp_num(String apNum) {
		ap_num = apNum;
	}

	public String getAp_firstname() {
		return ap_firstname;
	}

	public void setAp_firstname(String apFirstname) {
		ap_firstname = apFirstname;
	}

	public String getAp_lastname() {
		return ap_lastname;
	}

	public void setAp_lastname(String apLastname) {
		ap_lastname = apLastname;
	}

	public String getAp_gender() {
		return ap_gender;
	}

	public void setAp_gender(String apGender) {
		ap_gender = apGender;
	}

	public String getRace_num() {
		return race_num;
	}

	public void setRace_num(String raceNum) {
		race_num = raceNum;
	}

}
