package sad.group3.domain;

public class DistrictIncQty {
	private String distNum;
	private String quantity;

	public DistrictIncQty() {
		super();
	}

	public DistrictIncQty(String distNum, String quantity) {
		super();
		this.distNum = distNum;
		this.quantity = quantity;
	}

	public String getDistNum() {
		return distNum;
	}

	public void setDistNum(String distNum) {
		this.distNum = distNum;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

}
