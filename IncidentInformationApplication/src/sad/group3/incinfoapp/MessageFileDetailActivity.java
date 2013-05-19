package sad.group3.incinfoapp;
import sad.group3.domain.Message;
import com.google.gson.Gson;
import android.os.Bundle;
import android.widget.TextView;

public class MessageFileDetailActivity extends TimeBaseActivity {
	private Gson gson = new Gson();

	private Message messageDetail;

	private TextView msgIncIDTextView,msgDateTextView, msgSenderTextView, msgContentTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_detail_layout);
		sendMsgToHandler(3);
		init();
		uiInit();
		showInf();
	}

	private void init() {
		rebuildNarrativeDetail();
	}

	public void rebuildNarrativeDetail() {
		String json = this.getIntent().getStringExtra("DetailString");
		this.messageDetail = gson.fromJson(json, Message.class);
	}

	private void uiInit() {
		msgIncIDTextView = (TextView) findViewById(R.id.message_inc_no);
		msgDateTextView = (TextView) findViewById(R.id.message_date);
		msgSenderTextView = (TextView) findViewById(R.id.message_sender);
		msgContentTextView = (TextView) findViewById(R.id.message_content);
	}

	private void showInf() {
		msgIncIDTextView.setText(messageDetail.getIncID());
		msgDateTextView.setText(messageDetail.getMsgDate());
		msgSenderTextView.setText(messageDetail.getMsgOfficerName() + " ("
				+ messageDetail.getMsgOfficerID() + ")");
		msgContentTextView.setText(messageDetail.getMsgContent());
	}
}
