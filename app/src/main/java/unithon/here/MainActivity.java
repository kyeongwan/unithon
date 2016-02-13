package unithon.here;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
//import com.nhn.android.oauth.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "OAuthSampleActivity";

    /**
     * client 정보를 넣어준다.
     */
    private static String OAUTH_CLIENT_ID = "jyvqXeaVOVmV";
    private static String OAUTH_CLIENT_SECRET = "527300A0_COq1_XV33cf";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";


    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;

    /** UI 요소들 */
    private TextView mApiResultText;
    private static TextView mOauthAT;
    private static TextView mOauthRT;
    private static TextView mOauthExpires;
    private static TextView mOauthTokenType;
    private static TextView mOAuthState;

    private OAuthLoginButton mOAuthLoginButton;
    String mphoneNum;
    String maccessToken;
    String mtoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TelephonyManager telManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        mphoneNum = telManager.getLine1Number();
        maccessToken = OAuthLogin.getInstance().getAccessToken(this.getApplicationContext());
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        mtoken = pref.getString("gcmtoken", null);


        //로그인 시도
        String login = login(mphoneNum,mtoken,maccessToken,null,"http://unition.herokuapp.com/login");


        if(login.equals("Fail")){
            //이름 입력 창

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("설정");
            alert.setMessage("이름을 입력해주세요.");

            // Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    login(mphoneNum,mtoken,maccessToken,value,"http://unition.herokuapp.com/user");
                    // Do something with value!
                }
            });
            alert.show();

        }

        setSupportActionBar(toolbar);
        // 주석
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //로그아웃
      //  OAuthLogin.getInstance().logout(getApplicationContext());

    }

    public String login(String phoneNum,String gcmtoken,String NaccessToken, String name, String url){
        HTTP_Json json = new HTTP_Json();
        json.setServerURL(url);
        json.execute(fisrt_login_Json(phoneNum,gcmtoken,NaccessToken,name ));
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

    public JSONObject fisrt_login_Json(String phoneNum,String gcmtoken,String naveraccessToken, String name) {

        JSONObject jObj = new JSONObject();
        try {
            jObj.put("phoneNumber", phoneNum);
            jObj.put("gcmReg", gcmtoken);
            jObj.put("nToken", naveraccessToken);
            jObj.put("userName", name);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return jObj;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
