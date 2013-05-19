package sad.group3.domain;

public class IncidentSearch {
	private String inc_id;
	private String from_inc_date;
	private String to_inc_date;
	private String incType_Num;
	private String inc_StName;
	private String inc_StNum;
	private String inc_AptNum;
	private String dist_Num;
	private String inc_Source;
	private String inc_Resolved;

	public IncidentSearch() {
	}

	public IncidentSearch(String incId, String fromIncDate, String toIncDate,
			String incTypeNum, String incStName, String incStNum,
			String incAptNum, String distNum, String incSource,
			String incResolved) {
		super();
		inc_id = incId;
		from_inc_date = fromIncDate;
		to_inc_date = toIncDate;
		incType_Num = incTypeNum;
		inc_StName = incStName;
		inc_StNum = incStNum;
		inc_AptNum = incAptNum;
		dist_Num = distNum;
		inc_Source = incSource;
		inc_Resolved = incResolved;
	}

	public String getFrom_inc_date() {
		return from_inc_date;
	}

	public void setFrom_inc_date(String fromIncDate) {
		from_inc_date = fromIncDate;
	}

	public String getTo_inc_date() {
		return to_inc_date;
	}

	public void setTo_inc_date(String toIncDate) {
		to_inc_date = toIncDate;
	}

	public String getInc_id() {
		return inc_id;
	}

	public void setInc_id(String incId) {
		inc_id = incId;
	}

	public String getIncType_Num() {
		return incType_Num;
	}

	public void setIncType_Num(String incTypeNum) {
		incType_Num = incTypeNum;
	}

	public String getInc_StName() {
		return inc_StName;
	}

	public void setInc_StName(String incStName) {
		inc_StName = incStName;
	}

	public String getInc_StNum() {
		return inc_StNum;
	}

	public void setInc_StNum(String incStNum) {
		inc_StNum = incStNum;
	}

	public String getInc_AptNum() {
		return inc_AptNum;
	}

	public void setInc_AptNum(String incAptNum) {
		inc_AptNum = incAptNum;
	}

	public String getDist_Num() {
		return dist_Num;
	}

	public void setDist_Num(String distNum) {
		dist_Num = distNum;
	}

	public String getInc_Source() {
		return inc_Source;
	}

	public void setInc_Source(String incSource) {
		inc_Source = incSource;
	}

	public String getInc_Resolved() {
		return inc_Resolved;
	}

	public void setInc_Resolved(String incResolved) {
		inc_Resolved = incResolved;
	}

}
