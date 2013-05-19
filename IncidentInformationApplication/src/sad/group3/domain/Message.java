package sad.group3.domain;

public class Message {
	private String msgNum;
	private String msgDate;
	private String msgContent;

	private String msgOfficerName;
	private String msgOfficerID;
	private String incID;
	private String officerInvNum;

	public String getOfficerInvNum() {
		return officerInvNum;
	}

	public void setOfficerInvNum(String officerInvNum) {
		this.officerInvNum = officerInvNum;
	}

	public Message() {
	}

	public Message(String msgNum, String msgDate, String msgContent) {
		this.msgNum = msgNum;
		this.msgDate = msgDate;
		this.msgContent = msgContent;
	}

	public String getIncID() {
		return incID;
	}

	public void setIncID(String incID) {
		this.incID = incID;
	}

	public String getMsgOfficerName() {
		return msgOfficerName;
	}

	public void setMsgOfficerName(String msgOfficerName) {
		this.msgOfficerName = msgOfficerName;
	}

	public String getMsgOfficerID() {
		return msgOfficerID;
	}

	public void setMsgOfficerID(String msgOfficerID) {
		this.msgOfficerID = msgOfficerID;
	}

	public String getMsgNum() {
		return msgNum;
	}

	public void setMsgNum(String msgNum) {
		this.msgNum = msgNum;
	}

	public String getMsgDate() {
		return msgDate;
	}

	public void setMsgDate(String msgDate) {
		this.msgDate = msgDate;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

}
