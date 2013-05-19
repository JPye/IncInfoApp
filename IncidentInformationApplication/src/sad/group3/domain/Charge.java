package sad.group3.domain;

public class Charge {
	private String incID;
	private String chargeNum;
	private String chargeDesc;
	private String arresteeQty;

	public Charge() {
	}

	public Charge(String incID, String chargeNum, String chargeDesc) {
		this.incID = incID;
		this.chargeNum = chargeNum;
		this.chargeDesc = chargeDesc;
	}

	public String getArresteeQty() {
		return arresteeQty;
	}

	public void setArresteeQty(String arresteeQty) {
		this.arresteeQty = arresteeQty;
	}

	public String getIncID() {
		return incID;
	}

	public void setIncID(String incID) {
		this.incID = incID;
	}

	public String getChargeNum() {
		return chargeNum;
	}

	public void setChargeNum(String chargeNum) {
		this.chargeNum = chargeNum;
	}

	public String getChargeDesc() {
		return chargeDesc;
	}

	public void setChargeDesc(String chargeDesc) {
		this.chargeDesc = chargeDesc;
	}

}
