package nhuocquy.com.quanlychitieu.activity;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import nhuocquy.com.quanlychitieu.R;
import nhuocquy.com.quanlychitieu.adapter.MainRCVAdapter;
import nhuocquy.com.quanlychitieu.dao.ARecordDAOImpl;
import nhuocquy.com.quanlychitieu.model.ARecord;

public class AddNewRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private Date chooseDate = new Date();
    private Calendar calendar = new GregorianCalendar();

    private TextView tvDate;
    private ImageButton bntDate;
    private EditText txtReason;
    private EditText txtAmount;
    private Button btnAddRecord, btnHuy;
    Intent intent;
    public static final String TYPE = "type";
    public static final int TYPE_ADD = 1;
    public static final int TYPE_UPDATE = 2;
    private int type = 0;
    private int id;


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
        btnHuy = (Button) findViewById(R.id.btnHuy);

        bntDate.setOnClickListener(this);
        btnAddRecord.setOnClickListener(this);
        btnHuy.setOnClickListener(this);

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

            addRecord();
        }else if(v == btnHuy){
            finish();
        }
    }

    private void openDateAndTimeDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_time_and_date);
        dialog.setTitle("Choose date and time");

        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        Button btnFinish = (Button) dialog.findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                chooseDate = calendar.getTime();
                tvDate.setText(dateFormat.format(chooseDate));
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //bth thêm addRecord
    private void addRecord() {
        intent = new Intent();
        intent.putExtra(ARecordDAOImpl.C_DATE, getDateAndTime());
        intent.putExtra(ARecordDAOImpl.C_REASON, txtReason.getText().toString());
        intent.putExtra(ARecordDAOImpl.C_AMOUNT, Integer.parseInt(txtAmount.getText().toString()));
        if (type == TYPE_ADD) {
            setResult(MainActivity.RESULT_ADD_NEW_RECORE_SUCCESS, intent);
        } else if (type == TYPE_UPDATE) {
            intent.putExtra(ARecordDAOImpl.C_ID, id);
            setResult(MainActivity.RESULT_UPDATE_RECORE_SUCCESS, intent);
        }
        finish();
    }

//    private void addRecord(){
//        Intent intent=new Intent();
//        intent.putExtra(ARecordDAOImpl.C_DATE, getDateAndTime());
//        intent.putExtra(ARecordDAOImpl.C_REASON, txtReason.getText().toString());
//        intent.putExtra(ARecordDAOImpl.C_AMOUNT,Integer.parseInt(txtAmount.getText().toString()));
//        setResult(MainActivity.REQUEST_UPDATE_RCURENT_RECORE, intent);
//        finish();
//        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_new_record);

        findViews();

        intent = getIntent();
        type = intent.getIntExtra(TYPE, 0);

        if (type == TYPE_UPDATE) {
            String reason = intent.getStringExtra(ARecordDAOImpl.C_REASON);
            String date = intent.getStringExtra(ARecordDAOImpl.C_DATE);
            int amount = intent.getIntExtra(ARecordDAOImpl.C_AMOUNT, 0);
            id = intent.getIntExtra(ARecordDAOImpl.C_ID, 0);

            tvDate.setText(date);
            txtAmount.setText(String.valueOf(amount));
            txtReason.setText(reason);
            btnAddRecord.setText("Update");
        }

    }

    // yyyy-MM-dd HH:mm:ss
    private String getDateAndTime() {
        try {
            return ARecordDAOImpl.getDateTime(dateFormat.parse(tvDate.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onResume() {
        super.onResume();
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
