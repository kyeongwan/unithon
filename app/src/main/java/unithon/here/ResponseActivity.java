package unithon.here;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by qhsk0_000 on 2016-02-14.
 */
public class ResponseActivity extends Activity{

    private TextView _sender_name;
    private TextView _date;
    private TextView _contents;
    private ImageButton _back;
    private ImageButton _delete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        _sender_name = (TextView)findViewById(R.id.txt_sender_name);
        _contents = (TextView)findViewById(R.id.txt_contents);
        _date = (TextView)findViewById(R.id.txt_date);
        _back = (ImageButton)findViewById(R.id.btn_back);
        _delete = (ImageButton)findViewById(R.id.msg_delete);

        Intent intent = getIntent();
        String user =intent.getStringExtra("userName");
        String msg =intent.getStringExtra("Msg");

        _sender_name.setText(user);
        _contents.setText(msg);


        _back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //삭제 기능 구현
        _delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void goBack(View v){
        finish();
    }
}
