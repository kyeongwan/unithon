package unithon.here;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

/**
 * Created by user1 on 2016-03-09.
 */

public class JoiningActivity extends Activity {
    final private static Pattern PAT_COUNTRY_CODE_KOREA = Pattern.compile("^(\\+|\\-)?82\\-?");

    private String memberName;
    private String memberEmail;
    private String memberPassword;
    private String memberPasswordAgain;
    private String memberPhoneNumber;

    private EditText editName;
    private EditText editEmail;
    private EditText editPW;
    private EditText editPWagain;
    private EditText editPhoneNum;
    private Button btnJoin;
    private String mtoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining);

        editName = (EditText)findViewById(R.id.edit_name);
        editEmail = (EditText)findViewById(R.id.edit_email);
        editPW = (EditText)findViewById(R.id.edit_pw);
        editPWagain = (EditText)findViewById(R.id.edit_pw_again);
        editPhoneNum = (EditText)findViewById(R.id.edit_join_phone);
        btnJoin = (Button)findViewById(R.id.btn_join);

        //전화번호 자동입력됨
        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        memberPhoneNumber = tManager.getLine1Number();
        editPhoneNum.setText(NormalizePhoneNumber(memberPhoneNumber));
        editPhoneNum.setEnabled(false);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMemberInfoToDB();
            }
        });

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        mtoken = pref.getString("gcmtoken", null);
    }

    private void saveMemberInfoToDB(){
        memberName = editName.getText().toString();
        memberEmail = editEmail.getText().toString();
        memberPassword = editPW.getText().toString();
        memberPasswordAgain = editPWagain.getText().toString();


//        Toast.makeText(this, memberName , Toast.LENGTH_LONG).show();

        if (!memberPassword.equals(memberPasswordAgain)) {
            Toast.makeText(this, "비밀번호를 확인해 주세요.", Toast.LENGTH_LONG).show();
            return;
        }

        Log.i("Response",join(mtoken, memberName, memberEmail, memberPassword, memberPhoneNumber, "http://lemonlab.co.kr/ssu/php/here/hereUser"));
    }

    public String join(String gcmtoken, String memberName, String memberEmail,  String memberPassword, String memberPhoneNumber, String url){
        HTTP_Json json = new HTTP_Json();
        json.setServerURL(url);
        json.execute(join_Json(gcmtoken, memberName, memberEmail, memberPassword, memberPhoneNumber));
        String request = null;
        try {
            request = json.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return request;
    }

    public JSONObject join_Json(String gcmtoken, String memberName, String memberEmail,  String memberPassword, String memberPhoneNumber) {

        JSONObject jObj = new JSONObject();
        try {
            jObj.put("gcmtoken", gcmtoken);
            jObj.put("name", memberName);
            jObj.put("email", memberEmail);
            jObj.put("password", memberPassword);
            jObj.put("phonenumber", memberPhoneNumber);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return jObj;
    }


    private String NormalizePhoneNumber(String phoneNumber){
        final String first = "010";
        StringBuilder sb = new StringBuilder(first);

        // 국가번호를 제거하고 정규화함.
        if (!(phoneNumber = PAT_COUNTRY_CODE_KOREA.matcher(phoneNumber).replaceFirst("")).startsWith("0")) {
            phoneNumber = '0' + phoneNumber;
            return phoneNumber;
        }

        phoneNumber = phoneNumber.substring(phoneNumber.length() - 8, phoneNumber.length());
        sb.append(phoneNumber);

        return sb.toString();
    }
}

