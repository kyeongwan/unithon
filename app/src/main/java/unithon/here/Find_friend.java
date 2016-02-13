package unithon.here;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import unithon.here.Util.DBManager;

public class Find_friend extends Activity {
	Button btn_find_friend;
	EditText edit_number;
	SampleAdapter adapter;
	ListView m_ListView;
	ImageView image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_friend);

		btn_find_friend = (Button) findViewById(R.id.btn_find);
		edit_number = (EditText) findViewById(R.id.edit_phonenumber);
		adapter = new SampleAdapter(getApplication());
		m_ListView = (ListView) findViewById(R.id.list_friend);
		image =(ImageView)this.findViewById(R.id.no_find);

		btn_find_friend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image.setImageResource(android.R.color.transparent);
				String number = edit_number.getText().toString();
				HTTP_Json json = new HTTP_Json();
				json.setServerURL("http://unition.herokuapp.com/find");
				json.execute(findFriend_Json(number));
				// String request = json.findFriend_Json(number);
				String request = null;
				try {
					request = json.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (request.equals("False")) {
					image.setImageResource(R.drawable.no_find);
				} else {
					adapter.add(new SampleItem(request, android.R.drawable.ic_menu_search));
					m_ListView.setAdapter(adapter);
				}
			}
		});
		m_ListView.setOnItemClickListener(listener);
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub

			Toast.makeText(getApplicationContext(), adapter.getItem(arg2).getTag() + "" + edit_number.getText(), Toast.LENGTH_SHORT).show();
			String query = String.format("INSERT INTO app_info (phoneNumber, userName) VALUES ('%s', '%s')", adapter.getItem(arg2).getTag(), edit_number.getText());
			DBManager.getInstance().write(query);


			adapter.remove(adapter.getItem(arg2));
			m_ListView.setAdapter(adapter);
		}
	};

	public JSONObject findFriend_Json(String number) {
		JSONObject jObj = new JSONObject();
		try {
			jObj.put("phoneNumber", number);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return jObj;
	}

	private class SampleItem {
		public String tag;
		public int iconRes;

		public SampleItem(String tag, int iconRes) {
			this.tag = tag;
			this.iconRes = iconRes;
		}

		public String getTag(){
			return tag;
		}
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);

			return convertView;
		}
	}

}
