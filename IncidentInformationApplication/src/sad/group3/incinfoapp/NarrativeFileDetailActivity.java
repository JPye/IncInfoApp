package sad.group3.incinfoapp;

import sad.group3.domain.Narrative;
import com.google.gson.Gson;
import android.os.Bundle;
import android.widget.TextView;

public class NarrativeFileDetailActivity extends TimeBaseActivity {
	private Gson gson = new Gson();

	private Narrative narrativeDetail;

	private TextView incIDTextView,narrDateTextView, narrAuthorTextView, narrContentTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.narrative_detail_layout);
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
		this.narrativeDetail = gson.fromJson(json, Narrative.class);
	}

	private void uiInit() {
		incIDTextView= (TextView) findViewById(R.id.narrative_inc_id);
		narrDateTextView = (TextView) findViewById(R.id.narrative_date);
		narrAuthorTextView = (TextView) findViewById(R.id.narrative_author);
		narrContentTextView = (TextView) findViewById(R.id.narrative_content);
	}

	private void showInf() {
		incIDTextView.setText(this.getIntent().getStringExtra("inc_id"));
		narrDateTextView.setText(narrativeDetail.getNarrDate());
		narrAuthorTextView.setText(narrativeDetail.getNarrOfficerName() + " ("
				+ narrativeDetail.getNarrOfficerID() + ")");
		narrContentTextView.setText(narrativeDetail.getNarrContent());
	}
}
