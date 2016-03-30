package unithon.here;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import unithon.here.Util.DBManager;

/**
 * Created by lk on 16. 2. 14..
 */
public class FriendListActivity extends AppCompatActivity {

    ListView lv;
    SampleAdapter adapter;
    HashMap<String, String> map = new HashMap<>();
    Intent intent;

    public void onCreate(Bundle saved){
        super.onCreate(saved);
        setContentView(R.layout.activity_friend_list);
        setTitle("친구목록");

        intent = getIntent();


        lv = (ListView) findViewById(R.id.lv_list_friend);
        adapter = new SampleAdapter(getApplication());
        DBManager.getInstance(getApplicationContext(), "Here.db", null, 1).select("SELECT * FROM friend;", new DBManager.OnSelect() {
            @Override
            public void onSelect(Cursor cursor) {
                Log.i("cursor", cursor.getString(0));
                adapter.add(new SampleItem(cursor.getString(0),cursor.getString(1),android.R.drawable.ic_menu_search));
                map.put(cursor.getString(0), cursor.getString(1));
            }

            @Override
            public void onComplete(int cnt) {

            }

            @Override
            public void onErrorHandler(Exception e) {

            }
        });
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(listener);
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub


            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            String name = pref.getString("name", null);//발신 이름

            String latitude = String.valueOf(((Globals) getApplication()).getLongitude()); //위도
            String longitude = String.valueOf(((Globals) getApplication()).getLongitude()); //경도

            String toName = map.get(adapter.getItem(arg2).getName()); // 수신 이름
            String email = map.get(adapter.getItem(arg2).getEmail()); // 수신 이메일
            String msg = intent.getStringExtra("message"); //메시지


            // 결과 값.
            Log.i("INFO", name);
            Log.i("INFO", latitude);
            Log.i("INFO", longitude);
            Log.i("INFO", toName);
            Log.i("INFO", email);
            Log.i("INFO", msg);
            Toast.makeText(getApplicationContext(), "메시지가 전송되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    };




    public JSONObject sendMsg_Json(String number,String longitude,String latitude,String name,String msg) {

        JSONObject jObj = new JSONObject();
        try {
            jObj.put("phoneNumber",number);
            jObj.put("Iot",longitude);
            jObj.put("Iat", latitude);
            jObj.put("to", name);
            jObj.put("message", msg);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return jObj;
    }


    private class SampleItem {
        private String email;
        private String name;
        private int iconRes;

        public SampleItem(String email, String name, int iconRes) {
            this.email = email;
            this.name = name;
            this.iconRes = iconRes;
        }

        public String getEmail() {
            return this.email;
        }

        public String getName() {
            return this.name;
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
            title.setText(getItem(position).getName());

            return convertView;
        }
    }
}
