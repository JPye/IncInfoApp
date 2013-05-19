package sad.group3.domain;

public class MonthIncQty {
	private String month;
	private String quantity;

	public MonthIncQty() {
	}

	public MonthIncQty(String month, String quantity) {
		this.month = month;
		this.quantity = quantity;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

}
