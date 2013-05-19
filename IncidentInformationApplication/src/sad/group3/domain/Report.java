package sad.group3.domain;

import java.util.List;

public class Report {
	private String reportType;
	private String year;

	private byte[] chartPic;
	private List<SectorIncQty> sectorIncQties;
	private List<MonthIncQty> monthIncQties;
	private List<PeriodCallQty> periodCallQties;

	public Report() {
	}

	public List<PeriodCallQty> getPeriodCallQties() {
		return periodCallQties;
	}

	public void setPeriodCallQties(List<PeriodCallQty> periodCallQties) {
		this.periodCallQties = periodCallQties;
	}

	public List<MonthIncQty> getMonthIncQties() {
		return monthIncQties;
	}

	public void setMonthIncQties(List<MonthIncQty> monthIncQties) {
		this.monthIncQties = monthIncQties;
	}

	public byte[] getChartPic() {
		return chartPic;
	}

	public void setChartPic(byte[] chartPic) {
		this.chartPic = chartPic;
	}

	public List<SectorIncQty> getSectorIncQties() {
		return sectorIncQties;
	}

	public void setSectorIncQties(List<SectorIncQty> sectorIncQties) {
		this.sectorIncQties = sectorIncQties;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

}
