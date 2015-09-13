package nhuocquy.com.quanlychitieu.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import nhuocquy.com.quanlychitieu.R;

public class AddNewRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private Date chooseDate = new Date();
    private Calendar calendar = new GregorianCalendar();


    private TextView tvDate;
    private ImageButton bntDate;
    private EditText txtReason;
    private EditText txtAmount;
    private Button btnAddRecord;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2015-09-12 20:48:43 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        tvDate = (TextView) findViewById(R.id.tvDate);
        bntDate = (ImageButton) findViewById(R.id.bntDate);
        txtReason = (EditText) findViewById(R.id.txtReason);
        txtAmount = (EditText) findViewById(R.id.txtAmount);
        btnAddRecord = (Button) findViewById(R.id.btnAddRecord);

        bntDate.setOnClickListener(this);
        btnAddRecord.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2015-09-12 20:48:43 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == bntDate) {
            openDateAndTimeDialog();
        } else if (v == btnAddRecord) {
            // Handle clicks for btnAddRecord
        }
    }
    private void openDateAndTimeDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_time_and_date);
        dialog.setTitle("Choose date and time");

        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        Button btnFinish = (Button) dialog.findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(datePicker.getYear(),datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(),timePicker.getCurrentMinute());
                chooseDate = calendar.getTime();
                tvDate.setText(dateFormat.format(chooseDate));
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_new_record);

        findViews();



    }

    @Override
    protected void onResume() {
        super.onResume();
        tvDate.setText(dateFormat.format(chooseDate));
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
        return super.onOptionsItemSelected(item);
    }
}
