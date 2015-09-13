package nhuocquy.com.quanlychitieu.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import nhuocquy.com.quanlychitieu.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static int REQUEST_ADD_NEW_RECORE = 1;
    public static int RESULT_ADD_NEW_RECORE_SUCCESS = 2;
    public static int RESULT_ADD_NEW_RECORE_UNSUCCESS = 3;
    private Button button;


    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2015-09-12 20:37:20 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        button = (Button)findViewById( R.id.button );

        button.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2015-09-12 20:37:20 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == button ) {
            Intent intent = new Intent(MainActivity.this, AddNewRecordActivity.class);
            startActivityForResult(intent,REQUEST_ADD_NEW_RECORE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
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
        if (id == R.id.mnBtnAdd) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
