package sad.group3.incinfoapp;

import sad.group3.domain.Report;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.google.gson.Gson;

public class ReportShowActivity extends TimeBaseActivity {
	
	private static final String TAG = ReportShowActivity.class.getSimpleName();
	
	private Gson gson = new Gson();
	
	private Report reportDetail;
	private String reportType;
	
	private ImageView reportImageView;	
	
	private TextView dis1TextView,dis2TextView,dis3TextView,dis4TextView,
	dis5TextView,dis6TextView,dis7TextView,dis8TextView,dis9TextView,dis10TextView,
	dis11TextView,dis12TextView,dis13TextView,dis14TextView,dis15TextView,
	dis16TextView,dis17TextView,dis18TextView,dis19TextView,dis20TextView;
	private TextView sector1TextView,sector2TextView,sector3TextView,sector4TextView;
	
	private TextView month1Qty,month2Qty,month3Qty,month4Qty,month5Qty,
	month6Qty,month7Qty,month8Qty,month9Qty,month10Qty,month11Qty,month12Qty;
	private TextView monthTotalQty;
	
	private TextView time1Qty,time2Qty,time3Qty,time4Qty,time5Qty,
	time6Qty,time7Qty,time8Qty,time9Qty,time10Qty,time11Qty,time12Qty;
	private TextView timeTotalQty;
	
	
	private ZoomControls zoomControls;
	private Bitmap bitMap = null;
    private float scaleWidth = 1;   
    private float scaleHeight = 1;   
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sendMsgToHandler(3);
		Log.d(TAG, "report show activity started");
		
		init();
		
		if(this.reportType.equals("IncQtySector"))
		{
			setContentView(R.layout.report_sector_layout);
			
			incQtySectorUiInit();
			
			incQtySectorShowInf();
		}else if(this.reportType.equals("IncQtyMonth"))
		{
			setContentView(R.layout.report_month_layout);
			
			incQtyMonthUiInit();
			
			incQtyMonthShowInf();
		}else if(this.reportType.equals("CallQtyPeriod"))
		{
			setContentView(R.layout.report_timeperiod_layout);
			
			callQtyUiInit();
			
			callQtyShowInf();
		}
	}
	
	//
	private void init()
	{
		this.reportDetail = new Report(); 
		Log.d(TAG, " starte to receive report file");
		rebuildReportDetail();
		Log.d(TAG, " finish to receive report file" );
	}
	
	public void rebuildReportDetail()
	{
		String json = this.getIntent().getStringExtra("resultString");
		Log.d(TAG, " receive report detail string : " + json);
		this.reportDetail = gson.fromJson(json, Report.class);
		this.reportType = this.getIntent().getStringExtra("reportType");
	}
	
	// 初始化sector界面widget
	private void incQtySectorUiInit() 
	{
		Log.d(TAG, "start initiate UI");
		
		zoomControls=(ZoomControls) findViewById(R.id.zoomControls1);
		zoomControls.setIsZoomInEnabled(true);   
		zoomControls.setIsZoomOutEnabled(true);  
		
		reportImageView = (ImageView) findViewById(R.id.report_image);;
		
		this.sector1TextView=(TextView) findViewById(R.id.sector1_qty);
		this.dis1TextView=(TextView) findViewById(R.id.district1_qty);
		this.dis2TextView=(TextView) findViewById(R.id.district2_qty);
		this.dis3TextView=(TextView) findViewById(R.id.district3_qty);
		this.dis4TextView=(TextView) findViewById(R.id.district4_qty);
		this.dis5TextView=(TextView) findViewById(R.id.district5_qty);
		
		this.sector2TextView=(TextView) findViewById(R.id.sector2_qty);
		this.dis6TextView=(TextView) findViewById(R.id.district6_qty);
		this.dis7TextView=(TextView) findViewById(R.id.district7_qty);
		this.dis8TextView=(TextView) findViewById(R.id.district8_qty);
		this.dis9TextView=(TextView) findViewById(R.id.district9_qty);
		this.dis10TextView=(TextView) findViewById(R.id.district10_qty);
		
		this.sector3TextView=(TextView) findViewById(R.id.sector3_qty);
		this.dis11TextView=(TextView) findViewById(R.id.district11_qty);
		this.dis12TextView=(TextView) findViewById(R.id.district12_qty);
		this.dis13TextView=(TextView) findViewById(R.id.district13_qty);
		this.dis14TextView=(TextView) findViewById(R.id.district14_qty);
		this.dis15TextView=(TextView) findViewById(R.id.district15_qty);
		
		this.sector4TextView=(TextView) findViewById(R.id.sector4_qty);
		this.dis16TextView=(TextView) findViewById(R.id.district16_qty);
		this.dis17TextView=(TextView) findViewById(R.id.district17_qty);
		this.dis18TextView=(TextView) findViewById(R.id.district18_qty);
		this.dis19TextView=(TextView) findViewById(R.id.district19_qty);
		this.dis20TextView=(TextView) findViewById(R.id.district20_qty);
		
		Log.d(TAG, "finish initiate sector UI");
	}
	
	private void incQtySectorShowInf()
	{
		
		Log.d(TAG, "imageView set start");
		
		bitMap = BitmapFactory.decodeByteArray(reportDetail.getChartPic(), 0, reportDetail.getChartPic().length,null); 
		
		reportImageView.setImageBitmap(bitMap);
		
		zoomControls.setOnZoomInClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int bmpWidth = bitMap.getWidth();   
                int bmpHeight = bitMap.getHeight();   
                //设置图片放大的比例   
                double scale = 1.25;   
                //计算这次要放大的比例
                scaleWidth = (float)(scaleWidth*scale);   
                scaleHeight = (float)(scaleHeight*scale);
                //产生新的大小的Bitmap对象   
                Matrix matrix = new Matrix();   
                matrix.postScale(scaleWidth, scaleHeight);   
                Bitmap resizeBmp = Bitmap.createBitmap(bitMap,0,0,bmpWidth,bmpHeight,matrix,true);   
                reportImageView.setImageBitmap(resizeBmp);   
			}
		});
		
		zoomControls.setOnZoomOutClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int bmpWidth = bitMap.getWidth();   
                int bmpHeight = bitMap.getHeight();   
                //设置图片缩小比例   
                double scale = 0.8;   
                //计算这次要缩小比例   
                scaleWidth = (float)(scaleWidth*scale);   
                scaleHeight = (float)(scaleHeight*scale);   
                //产生新的大小的Bitmap对象   
                Matrix matrix = new Matrix();   
                matrix.postScale(scaleWidth, scaleHeight);   
                Bitmap resizeBmp = Bitmap.createBitmap(bitMap,0,0,bmpWidth,bmpHeight,matrix,true);   
                reportImageView.setImageBitmap(resizeBmp);  
			}
		});
		
		Log.d(TAG, "imageView set ended");
		
		Log.d(TAG, "textView set started");
		
		
		this.dis1TextView.setText(this.reportDetail.getSectorIncQties().get(0).getDistrictIncQties().get(0).getQuantity().toString());
		this.dis2TextView.setText(this.reportDetail.getSectorIncQties().get(0).getDistrictIncQties().get(1).getQuantity().toString());
		this.dis3TextView.setText(this.reportDetail.getSectorIncQties().get(0).getDistrictIncQties().get(2).getQuantity().toString());
		this.dis4TextView.setText(this.reportDetail.getSectorIncQties().get(0).getDistrictIncQties().get(3).getQuantity().toString());
		this.dis5TextView.setText(this.reportDetail.getSectorIncQties().get(0).getDistrictIncQties().get(4).getQuantity().toString());
		this.sector1TextView.setText(this.reportDetail.getSectorIncQties().get(0).getQuantity());
		
		this.dis6TextView.setText(this.reportDetail.getSectorIncQties().get(1).getDistrictIncQties().get(0).getQuantity().toString());
		this.dis7TextView.setText(this.reportDetail.getSectorIncQties().get(1).getDistrictIncQties().get(1).getQuantity().toString());
		this.dis8TextView.setText(this.reportDetail.getSectorIncQties().get(1).getDistrictIncQties().get(2).getQuantity().toString());
		this.dis9TextView.setText(this.reportDetail.getSectorIncQties().get(1).getDistrictIncQties().get(3).getQuantity().toString());
		this.dis10TextView.setText(this.reportDetail.getSectorIncQties().get(1).getDistrictIncQties().get(4).getQuantity().toString());
		this.sector2TextView.setText(this.reportDetail.getSectorIncQties().get(1).getQuantity());
		
		this.dis11TextView.setText(this.reportDetail.getSectorIncQties().get(2).getDistrictIncQties().get(0).getQuantity().toString());
		this.dis12TextView.setText(this.reportDetail.getSectorIncQties().get(2).getDistrictIncQties().get(1).getQuantity().toString());
		this.dis13TextView.setText(this.reportDetail.getSectorIncQties().get(2).getDistrictIncQties().get(2).getQuantity().toString());
		this.dis14TextView.setText(this.reportDetail.getSectorIncQties().get(2).getDistrictIncQties().get(3).getQuantity().toString());
		this.dis15TextView.setText(this.reportDetail.getSectorIncQties().get(2).getDistrictIncQties().get(4).getQuantity().toString());
		this.sector3TextView.setText(this.reportDetail.getSectorIncQties().get(2).getQuantity());
		
		this.dis16TextView.setText(this.reportDetail.getSectorIncQties().get(3).getDistrictIncQties().get(0).getQuantity().toString());
		this.dis17TextView.setText(this.reportDetail.getSectorIncQties().get(3).getDistrictIncQties().get(1).getQuantity().toString());
		this.dis18TextView.setText(this.reportDetail.getSectorIncQties().get(3).getDistrictIncQties().get(2).getQuantity().toString());
		this.dis19TextView.setText(this.reportDetail.getSectorIncQties().get(3).getDistrictIncQties().get(3).getQuantity().toString());
		this.dis20TextView.setText(this.reportDetail.getSectorIncQties().get(3).getDistrictIncQties().get(4).getQuantity().toString());
		this.sector4TextView.setText(this.reportDetail.getSectorIncQties().get(3).getQuantity());
		
		Log.d(TAG, "textView set ended");
	}

	// 初始化month界面widget
	private void incQtyMonthUiInit()
	{
		Log.d(TAG, "start initiate UI");
		
		zoomControls=(ZoomControls) findViewById(R.id.zoomControls_month);
		zoomControls.setIsZoomInEnabled(true);   
		zoomControls.setIsZoomOutEnabled(true);  
		
		reportImageView = (ImageView) findViewById(R.id.report_month_image);
		
		month1Qty = (TextView) findViewById(R.id.month1_qty);
		month2Qty = (TextView) findViewById(R.id.month2_qty);
		month3Qty = (TextView) findViewById(R.id.month3_qty);
		month4Qty = (TextView) findViewById(R.id.month4_qty);
		month5Qty = (TextView) findViewById(R.id.month5_qty);
		month6Qty = (TextView) findViewById(R.id.month6_qty);
		month7Qty = (TextView) findViewById(R.id.month7_qty);
		month8Qty = (TextView) findViewById(R.id.month8_qty);
		month9Qty = (TextView) findViewById(R.id.month9_qty);
		month10Qty = (TextView) findViewById(R.id.month10_qty);
		month11Qty = (TextView) findViewById(R.id.month11_qty);
		month12Qty = (TextView) findViewById(R.id.month12_qty);
		
		monthTotalQty = (TextView) findViewById(R.id.month_total_qty);
		
		Log.d(TAG, "finish initiate month UI");
	}
	
	private void incQtyMonthShowInf()
	{
		
		Log.d(TAG, "imageView set start");
		
		bitMap = BitmapFactory.decodeByteArray(reportDetail.getChartPic(), 0, reportDetail.getChartPic().length,null); 
		
		reportImageView.setImageBitmap(bitMap);
		
		zoomControls.setOnZoomInClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int bmpWidth = bitMap.getWidth();   
                int bmpHeight = bitMap.getHeight();   
                //设置图片放大的比例   
                double scale = 1.25;   
                //计算这次要放大的比例
                scaleWidth = (float)(scaleWidth*scale);   
                scaleHeight = (float)(scaleHeight*scale);
                //产生新的大小的Bitmap对象   
                Matrix matrix = new Matrix();   
                matrix.postScale(scaleWidth, scaleHeight);   
                Bitmap resizeBmp = Bitmap.createBitmap(bitMap,0,0,bmpWidth,bmpHeight,matrix,true);   
                reportImageView.setImageBitmap(resizeBmp);   
			}
		});
		
		zoomControls.setOnZoomOutClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int bmpWidth = bitMap.getWidth();   
                int bmpHeight = bitMap.getHeight();   
                //设置图片缩小比例   
                double scale = 0.8;   
                //计算这次要缩小比例   
                scaleWidth = (float)(scaleWidth*scale);   
                scaleHeight = (float)(scaleHeight*scale);   
                //产生新的大小的Bitmap对象   
                Matrix matrix = new Matrix();   
                matrix.postScale(scaleWidth, scaleHeight);   
                Bitmap resizeBmp = Bitmap.createBitmap(bitMap,0,0,bmpWidth,bmpHeight,matrix,true);   
                reportImageView.setImageBitmap(resizeBmp);  
			}
		});
		
		Log.d(TAG, "imageView set ended");
		
		Log.d(TAG, "textView set started");
		
		int monthTotal = 0;
		
		this.month1Qty.setText(this.reportDetail.getMonthIncQties().get(0).getQuantity().toString());
		monthTotal = monthTotal + Integer.parseInt(reportDetail.getMonthIncQties().get(0).getQuantity().toString());
		this.month2Qty.setText(this.reportDetail.getMonthIncQties().get(1).getQuantity().toString());
		monthTotal = monthTotal + Integer.parseInt(reportDetail.getMonthIncQties().get(1).getQuantity().toString());
		this.month3Qty.setText(this.reportDetail.getMonthIncQties().get(2).getQuantity().toString());
		monthTotal = monthTotal + Integer.parseInt(reportDetail.getMonthIncQties().get(2).getQuantity().toString());
		this.month4Qty.setText(this.reportDetail.getMonthIncQties().get(3).getQuantity().toString());
		monthTotal = monthTotal + Integer.parseInt(reportDetail.getMonthIncQties().get(3).getQuantity().toString());
		this.month5Qty.setText(this.reportDetail.getMonthIncQties().get(4).getQuantity().toString());
		monthTotal = monthTotal + Integer.parseInt(reportDetail.getMonthIncQties().get(4).getQuantity().toString());
		this.month6Qty.setText(this.reportDetail.getMonthIncQties().get(5).getQuantity().toString());
		monthTotal = monthTotal + Integer.parseInt(reportDetail.getMonthIncQties().get(5).getQuantity().toString());
		this.month7Qty.setText(this.reportDetail.getMonthIncQties().get(6).getQuantity().toString());
		monthTotal = monthTotal + Integer.parseInt(reportDetail.getMonthIncQties().get(6).getQuantity().toString());
		this.month8Qty.setText(this.reportDetail.getMonthIncQties().get(7).getQuantity().toString());
		monthTotal = monthTotal + Integer.parseInt(reportDetail.getMonthIncQties().get(7).getQuantity().toString());
		this.month9Qty.setText(this.reportDetail.getMonthIncQties().get(8).getQuantity().toString());
		monthTotal = monthTotal + Integer.parseInt(reportDetail.getMonthIncQties().get(8).getQuantity().toString());
		this.month10Qty.setText(this.reportDetail.getMonthIncQties().get(9).getQuantity().toString());
		monthTotal = monthTotal + Integer.parseInt(reportDetail.getMonthIncQties().get(9).getQuantity().toString());
		this.month11Qty.setText(this.reportDetail.getMonthIncQties().get(10).getQuantity().toString());
		monthTotal = monthTotal + Integer.parseInt(reportDetail.getMonthIncQties().get(10).getQuantity().toString());
		this.month12Qty.setText(this.reportDetail.getMonthIncQties().get(11).getQuantity().toString());
		monthTotal = monthTotal + Integer.parseInt(reportDetail.getMonthIncQties().get(11).getQuantity().toString());
		
		
		this.monthTotalQty.setText("" + monthTotal);
		

		Log.d(TAG, "textView set ended");
	}
	
	// 初始化CallQty界面widget
	private void callQtyUiInit()
	{
		Log.d(TAG, "start initiate UI");
		
		zoomControls=(ZoomControls) findViewById(R.id.zoomControls_timeperoid);
		zoomControls.setIsZoomInEnabled(true);   
		zoomControls.setIsZoomOutEnabled(true);  
		
		reportImageView = (ImageView) findViewById(R.id.report_timeperiod_image);
		
		time1Qty = (TextView) findViewById(R.id.time1_qty);
		time2Qty = (TextView) findViewById(R.id.time2_qty);
		time3Qty = (TextView) findViewById(R.id.time3_qty);
		time4Qty = (TextView) findViewById(R.id.time4_qty);
		time5Qty = (TextView) findViewById(R.id.time5_qty);
		time6Qty = (TextView) findViewById(R.id.time6_qty);
		time7Qty = (TextView) findViewById(R.id.time7_qty);
		time8Qty = (TextView) findViewById(R.id.time8_qty);
		time9Qty = (TextView) findViewById(R.id.time9_qty);
		time10Qty = (TextView) findViewById(R.id.time10_qty);
		time11Qty = (TextView) findViewById(R.id.time11_qty);
		time12Qty = (TextView) findViewById(R.id.time12_qty);
		
		timeTotalQty = (TextView) findViewById(R.id.timeperiod_total_qty);
		
		Log.d(TAG, "finish initiate time period UI");
	}
	
	private void callQtyShowInf()
	{
		
		Log.d(TAG, "imageView set start");
		
		bitMap = BitmapFactory.decodeByteArray(reportDetail.getChartPic(), 0, reportDetail.getChartPic().length,null); 
		
		reportImageView.setImageBitmap(bitMap);
		
		zoomControls.setOnZoomInClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int bmpWidth = bitMap.getWidth();   
                int bmpHeight = bitMap.getHeight();   
                //设置图片放大的比例   
                double scale = 1.25;   
                //计算这次要放大的比例
                scaleWidth = (float)(scaleWidth*scale);   
                scaleHeight = (float)(scaleHeight*scale);
                //产生新的大小的Bitmap对象   
                Matrix matrix = new Matrix();   
                matrix.postScale(scaleWidth, scaleHeight);   
                Bitmap resizeBmp = Bitmap.createBitmap(bitMap,0,0,bmpWidth,bmpHeight,matrix,true);   
                reportImageView.setImageBitmap(resizeBmp);   
			}
		});
		
		zoomControls.setOnZoomOutClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int bmpWidth = bitMap.getWidth();   
                int bmpHeight = bitMap.getHeight();   
                //设置图片缩小比例   
                double scale = 0.8;   
                //计算这次要缩小比例   
                scaleWidth = (float)(scaleWidth*scale);   
                scaleHeight = (float)(scaleHeight*scale);   
                //产生新的大小的Bitmap对象   
                Matrix matrix = new Matrix();   
                matrix.postScale(scaleWidth, scaleHeight);   
                Bitmap resizeBmp = Bitmap.createBitmap(bitMap,0,0,bmpWidth,bmpHeight,matrix,true);   
                reportImageView.setImageBitmap(resizeBmp);  
			}
		});
		
		Log.d(TAG, "imageView set ended");
		
		Log.d(TAG, "textView set started");
		
		int timeTotal = 0;
		
		this.time1Qty.setText(this.reportDetail.getPeriodCallQties().get(0).getQuantity().toString());
		timeTotal = timeTotal + Integer.parseInt(reportDetail.getPeriodCallQties().get(0).getQuantity().toString());
		this.time2Qty.setText(this.reportDetail.getPeriodCallQties().get(1).getQuantity().toString());
		timeTotal = timeTotal + Integer.parseInt(reportDetail.getPeriodCallQties().get(1).getQuantity().toString());
		this.time3Qty.setText(this.reportDetail.getPeriodCallQties().get(2).getQuantity().toString());
		timeTotal = timeTotal + Integer.parseInt(reportDetail.getPeriodCallQties().get(2).getQuantity().toString());
		this.time4Qty.setText(this.reportDetail.getPeriodCallQties().get(3).getQuantity().toString());
		timeTotal = timeTotal + Integer.parseInt(reportDetail.getPeriodCallQties().get(3).getQuantity().toString());
		this.time5Qty.setText(this.reportDetail.getPeriodCallQties().get(4).getQuantity().toString());
		timeTotal = timeTotal + Integer.parseInt(reportDetail.getPeriodCallQties().get(4).getQuantity().toString());
		this.time6Qty.setText(this.reportDetail.getPeriodCallQties().get(5).getQuantity().toString());
		timeTotal = timeTotal + Integer.parseInt(reportDetail.getPeriodCallQties().get(5).getQuantity().toString());
		this.time7Qty.setText(this.reportDetail.getPeriodCallQties().get(6).getQuantity().toString());
		timeTotal = timeTotal + Integer.parseInt(reportDetail.getPeriodCallQties().get(6).getQuantity().toString());
		this.time8Qty.setText(this.reportDetail.getPeriodCallQties().get(7).getQuantity().toString());
		timeTotal = timeTotal + Integer.parseInt(reportDetail.getPeriodCallQties().get(7).getQuantity().toString());
		this.time9Qty.setText(this.reportDetail.getPeriodCallQties().get(8).getQuantity().toString());
		timeTotal = timeTotal + Integer.parseInt(reportDetail.getPeriodCallQties().get(8).getQuantity().toString());
		this.time10Qty.setText(this.reportDetail.getPeriodCallQties().get(9).getQuantity().toString());
		timeTotal = timeTotal + Integer.parseInt(reportDetail.getPeriodCallQties().get(9).getQuantity().toString());
		this.time11Qty.setText(this.reportDetail.getPeriodCallQties().get(10).getQuantity().toString());
		timeTotal = timeTotal + Integer.parseInt(reportDetail.getPeriodCallQties().get(10).getQuantity().toString());
		this.time12Qty.setText(this.reportDetail.getPeriodCallQties().get(11).getQuantity().toString());
		timeTotal = timeTotal + Integer.parseInt(reportDetail.getPeriodCallQties().get(11).getQuantity().toString());
		
		
		this.timeTotalQty.setText("" + timeTotal);
		

		Log.d(TAG, "textView set ended");
	}
}

