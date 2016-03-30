package unithon.here;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Created by user1 on 2016-03-11.
 */
public class AgreementActivity extends Activity{
    private CheckBox checkbox1;
    private Button btnDisagree;
    private Button btnAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        checkbox1 = (CheckBox)findViewById(R.id.checkbox1);
        int id = Resources.getSystem().getIdentifier("#29ABE2", "drawable", "android");
        checkbox1.setButtonDrawable(id);

        btnDisagree = (Button)findViewById(R.id.btn_disagree);
        btnDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnAgree = (Button)findViewById(R.id.btn_agree);
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///체크박스 체크했는지 확인 후에
                ////////////////////
                Intent intent = new Intent(AgreementActivity.this, JoiningActivity.class);
                startActivity(intent);
            }
        });
    }
}
