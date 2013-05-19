package sad.group3.domain;

import java.util.List;

public class Incident {
	private String id;
	private String incidentType;
	private String date;
	private String streetName;
	private String streetNum;
	private String apartmentNum;
	private String districtNum;
	private String sector;
	private String source;
	private String resolved;
	// For Message Write Use Only!
	private String officerInvNum;

	private List<Arrestee> arrestees;
	private List<Officer> officers;
	private List<Narrative> narratives;
	private List<Caller> callers;
	private List<Message> messages;

	public Incident() {
	}

	public Incident(String id, String incidentType, String date,
			String streetName, String streetNum, String apartmentNum,
			String districtNum, String sector, String source, String resolved,
			List<Arrestee> arrestees, List<Officer> officers,
			List<Narrative> narratives, List<Caller> callers,
			List<Message> messages) {
		this.id = id;
		this.incidentType = incidentType;
		this.date = date;
		this.streetName = streetName;
		this.streetNum = streetNum;
		this.apartmentNum = apartmentNum;
		this.districtNum = districtNum;
		this.sector = sector;
		this.source = source;
		this.resolved = resolved;
		this.arrestees = arrestees;
		this.officers = officers;
		this.narratives = narratives;
		this.callers = callers;
		this.messages = messages;
	}

	public String getOfficerInvNum() {
		return officerInvNum;
	}

	public void setOfficerInvNum(String officerInvNum) {
		this.officerInvNum = officerInvNum;
	}

	public List<Arrestee> getArrestees() {
		return arrestees;
	}

	public void setArrestees(List<Arrestee> arrestees) {
		this.arrestees = arrestees;
	}

	public List<Officer> getOfficers() {
		return officers;
	}

	public void setOfficers(List<Officer> officers) {
		this.officers = officers;
	}

	public List<Narrative> getNarratives() {
		return narratives;
	}

	public void setNarratives(List<Narrative> narratives) {
		this.narratives = narratives;
	}

	public List<Caller> getCallers() {
		return callers;
	}

	public void setCallers(List<Caller> callers) {
		this.callers = callers;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public String getIncidentType() {
		return incidentType;
	}

	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetNum() {
		return streetNum;
	}

	public void setStreetNum(String streetNum) {
		this.streetNum = streetNum;
	}

	public String getApartmentNum() {
		return apartmentNum;
	}

	public void setApartmentNum(String apartmentNum) {
		this.apartmentNum = apartmentNum;
	}

	public String getDistrictNum() {
		return districtNum;
	}

	public void setDistrictNum(String districtNum) {
		this.districtNum = districtNum;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getResolved() {
		return resolved;
	}

	public void setResolved(String resolved) {
		this.resolved = resolved;
	}

}
