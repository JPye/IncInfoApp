package sad.group3.domain;

public class PeriodCallQty {
	private String timePeriod;
	private String quantity;

	public PeriodCallQty() {
	}

	public PeriodCallQty(String timePeriod, String quantity) {
		this.timePeriod = timePeriod;
		this.quantity = quantity;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

}
