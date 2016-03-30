package unithon.here;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

import unithon.here.Service.Request;
import unithon.here.Service.RequestUtil;
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
        edit_number.setHint("이메일, 핸드폰 번호 검색");
        adapter = new SampleAdapter(getApplication());
        m_ListView = (ListView) findViewById(R.id.list_friend);
        image = (ImageView) this.findViewById(R.id.no_find);
        btn_find_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                image.setImageResource(android.R.color.transparent);
                String number = edit_number.getText().toString();
                RequestUtil request = null;


                request.get("http://lemonlab.co.kr/ssu/php/here/hereUser/" + number, new Request() {
                    @Override
                    public void onSuccess(String receiveData) {
                        try {
                            JSONObject json = new JSONObject(receiveData);
                            if (!receiveData.contains("error")) {

                                String name = json.getString("name");
                                String email = json.getString("email");

                                Log.i("INFO","이름 : "+name);
                                Log.i("INFO","이메일 : "+email);

                                Message msg = handler.obtainMessage();
                                Bundle data = new Bundle();
                                data.putString("name", name);
                                data.putString("email", email);
                                msg.setData(data);
                                handler.sendMessage(msg);
                            } else {
                                String error = json.getString("error");
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e("MYAPP", "unexpected JSON exception", e);
                        }
                    }

                    @Override
                    public void onFail(String url, String error) {
                        try {
                            JSONObject json = new JSONObject(error);
                            String error_msg = json.getString("error");
                            Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                            image.setImageResource(R.drawable.no_find);
                        } catch (JSONException e) {
                            Log.e("MYAPP", "unexpected JSON exception", e);
                        }
                    }
                });
            }
        });
        m_ListView.setOnItemClickListener(listener);
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            String email = data.getString("email");
            String name = data.getString("name");
            adapter.add(new SampleItem(email, name, android.R.drawable.ic_menu_search));
            m_ListView.setAdapter(adapter);
        }
    };
    OnItemClickListener listener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub

            //DB insert
            String query = String.format("INSERT INTO friend (phoneNumber, userName) VALUES ('%s', '%s')", adapter.getItem(arg2).getEmail(), adapter.getItem(arg2).getName());
            DBManager.getInstance(getApplicationContext(), "Here.db", null, 1).write(query);


            adapter.remove(adapter.getItem(arg2));
            m_ListView.setAdapter(adapter);
        }
    };

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

    public void btn_back(View v) {
        finish();
    }

}
