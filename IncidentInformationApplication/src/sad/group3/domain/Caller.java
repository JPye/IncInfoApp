package sad.group3.domain;

public class Caller {
	private String id;
	private String fullName;
	private String phoneNum;
	private String callDate;

	public Caller() {
	}

	public Caller(String id, String fullName, String phoneNum, String callDate) {
		this.id = id;
		this.fullName = fullName;
		this.phoneNum = phoneNum;
		this.callDate = callDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCallDate() {
		return callDate;
	}

	public void setCallDate(String callDate) {
		this.callDate = callDate;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

}
