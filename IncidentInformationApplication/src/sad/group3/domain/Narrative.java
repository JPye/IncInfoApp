package sad.group3.domain;

public class Narrative {
	private String narrNum;
	private String narrDate;
	private String narrContent;

	private String narrOfficerName;
	private String narrOfficerID;

	public Narrative() {
	}

	public Narrative(String narrNum, String narrDate, String narrContent) {
		this.narrNum = narrNum;
		this.narrDate = narrDate;
		this.narrContent = narrContent;
	}

	public String getNarrOfficerName() {
		return narrOfficerName;
	}

	public void setNarrOfficerName(String narrOfficerName) {
		this.narrOfficerName = narrOfficerName;
	}

	public String getNarrOfficerID() {
		return narrOfficerID;
	}

	public void setNarrOfficerID(String narrOfficerID) {
		this.narrOfficerID = narrOfficerID;
	}

	public String getNarrNum() {
		return narrNum;
	}

	public void setNarrNum(String narrNum) {
		this.narrNum = narrNum;
	}

	public String getNarrDate() {
		return narrDate;
	}

	public void setNarrDate(String narrDate) {
		this.narrDate = narrDate;
	}

	public String getNarrContent() {
		return narrContent;
	}

	public void setNarrContent(String narrContent) {
		this.narrContent = narrContent;
	}

}
