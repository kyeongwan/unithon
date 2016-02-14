package unithon.here;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

/**
 * Created by qhsk0_000 on 2016-02-14.
 */
public class ResponseActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

    }

    public void goBack(View v){
        finish();
    }
}
