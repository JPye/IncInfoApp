package sad.group3.domain;

import java.util.List;

public class SectorIncQty {
	private String sectorName;
	private String quantity;
	private String year;
	
	private List<DistrictIncQty> districtIncQties;

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public List<DistrictIncQty> getDistrictIncQties() {
		return districtIncQties;
	}

	public void setDistrictIncQties(List<DistrictIncQty> districtIncQties) {
		this.districtIncQties = districtIncQties;
	}
	
	
}
