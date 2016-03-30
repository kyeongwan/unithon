package unithon.here;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import unithon.here.Service.Request;
import unithon.here.Service.RequestUtil;

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

        editName = (EditText) findViewById(R.id.edit_name);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPW = (EditText) findViewById(R.id.edit_pw);
        editPWagain = (EditText) findViewById(R.id.edit_pw_again);
        editPhoneNum = (EditText) findViewById(R.id.edit_join_phone);
        btnJoin = (Button) findViewById(R.id.btn_join);

        //전화번호 자동입력됨
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
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

    private void saveMemberInfoToDB() {
        memberName = editName.getText().toString();
        memberEmail = editEmail.getText().toString();
        memberPassword = editPW.getText().toString();
        memberPasswordAgain = editPWagain.getText().toString();

        RequestUtil request = null;

//        Toast.makeText(this, memberName , Toast.LENGTH_LONG).show();

        if (memberName.length() < 1) {
            Toast.makeText(this, "이름을 입력 해 주세요.", Toast.LENGTH_LONG).show();
        }
        if (memberEmail.length() < 1) {
            Toast.makeText(this, "이메일을 입력 해 주세요.", Toast.LENGTH_LONG).show();
        }
        if (!memberPassword.equals(memberPasswordAgain)) {
            Toast.makeText(this, "비밀번호를 확인 해 주세요.", Toast.LENGTH_LONG).show();
            return;
        }

        String body = "name=" + memberName + "&email=" + memberEmail + "&password=" + memberPassword + "&phonenumber=" + memberPhoneNumber + "&gcmtoken=" + mtoken;


        request.post("http://lemonlab.co.kr/ssu/php/here/hereUser", body, new Request() {
            @Override
            public void onSuccess(String receiveData) {
                try {
                    JSONObject json = new JSONObject(receiveData);
                    if (!receiveData.contains("error")) {
                        Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                        startActivity(intent);
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
                } catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                }
            }
        });
    }

    private String NormalizePhoneNumber(String phoneNumber) {
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

