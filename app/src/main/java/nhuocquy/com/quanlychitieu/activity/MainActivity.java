package nhuocquy.com.quanlychitieu.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import nhuocquy.com.quanlychitieu.R;
import nhuocquy.com.quanlychitieu.adapter.MainRCVAdapter;
import nhuocquy.com.quanlychitieu.dao.ARecordDAO;
import nhuocquy.com.quanlychitieu.dao.ARecordDAOImpl;
import nhuocquy.com.quanlychitieu.dao.DAOException;
import nhuocquy.com.quanlychitieu.listener.RecyclerItemClickListener;
import nhuocquy.com.quanlychitieu.model.ARecord;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_ADD_NEW_RECORE = 1;
    public static final int RESULT_ADD_NEW_RECORE_SUCCESS = 2;
    public static final int RESULT_ADD_NEW_RECORE_UNSUCCESS = 3;

    public static final int REQUEST_UPDATE_RCURENT_RECORE = 4;
    public static final int RESULT_UPDATE_RECORE_SUCCESS = 5;

    public static final int SUMARY_BY_DAY = 0;
    public static final int SUMARY_BY_WEEK = 1;
    public static final int SUMARY_BY_MONTH = 2;
    public static final int SUMARY_BY_YEAR = 3;
    public static final int SUMARY_BY_2_DAY = 4;
    private int typeOfSumary = SUMARY_BY_MONTH;
    private String[] typeArray = {"Theo ngày","Theo tuần", "Theo tháng", "Theo năm", "Giữa 2 ngày"};
    private boolean isASC = false; // sap xep theo tang dan
    private Date chooseDate = new Date();
    private Calendar calendar = new GregorianCalendar();

    private ARecordDAO aRecordDAO = new ARecordDAOImpl(this);
    private MainRCVAdapter adapter;

    private Spinner spTypeOfSum, spOrder;
    private RecyclerView listRecord;
    private ImageButton bntDate;
    private TextView tvDate, tvSum;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2015-09-12 20:37:20 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        listRecord = (RecyclerView) findViewById(R.id.listRecord);

        bntDate = (ImageButton) findViewById(R.id.bntDate);
        bntDate.setOnClickListener(this);

        spTypeOfSum = (Spinner) findViewById(R.id.typeOfSumary);
        spOrder = (Spinner) findViewById(R.id.spOrder);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvSum = (TextView) findViewById(R.id.tvSum);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2015-09-12 20:37:20 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == bntDate) {
            openDateAndTimeDialog();
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
                tvDate.setText(ARecordDAOImpl.getDateTime3(chooseDate));
                dialog.dismiss();
                updateData();
            }
        });
        dialog.show();
    }
    private void addNewRecord(){
        Intent intent = new Intent(MainActivity.this, AddNewRecordActivity.class);
        intent.putExtra(AddNewRecordActivity.TYPE, AddNewRecordActivity.TYPE_ADD);
        startActivityForResult(intent, REQUEST_ADD_NEW_RECORE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        setUp();
    }

    private void setUp() {
        spTypeOfSum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeOfSumary = position;
                updateData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,typeArray);
        spTypeOfSum.setAdapter(spAdapter);
        //
        spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,new String[]{"Tăng dần", "Giảm dần"});
        spOrder.setAdapter(spAdapter);
        spOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isASC = position ==0;
                updateData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //
        adapter = new MainRCVAdapter();
        listRecord.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listRecord.setLayoutManager(linearLayoutManager);

        listRecord.setItemAnimator(new DefaultItemAnimator());

        listRecord.addOnItemTouchListener(new RecyclerItemClickListener(this, listRecord, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(final View view, final int position) {
//                Toast.makeText( MainActivity.this,position +"", Toast.LENGTH_LONG).show();
                final CharSequence[] items = {"Thêm", "Sửa", "Xóa"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Action");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, final int item) {
//                        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                        switch (item) {
                            case 0:
                                addNewRecord();
                                    break;
                            case 1://bth thêm case 1:
                                //hiển thị activity AddNewRecordActivity với dữ liệu từ listrecord
                                // xử lý
                                ARecord aRecord2 = adapter.getListRecord().get(position);
                                Intent intent = new Intent(MainActivity.this, AddNewRecordActivity.class);
                                intent.putExtra(ARecordDAOImpl.C_AMOUNT, aRecord2.getAmount());
                                intent.putExtra(ARecordDAOImpl.C_ID, aRecord2.getId());
                                intent.putExtra(ARecordDAOImpl.C_REASON, aRecord2.getReason());
                                intent.putExtra(ARecordDAOImpl.C_DATE, ARecordDAOImpl.getDateTime3(aRecord2.getDate()));
                                intent.putExtra(AddNewRecordActivity.TYPE, AddNewRecordActivity.TYPE_UPDATE);
                                startActivityForResult(intent, REQUEST_ADD_NEW_RECORE);
                                break;
                            case 2:
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                //asynctask delete
                                                new AsyncTask<Integer, Void, Boolean>() {
                                                    @Override
                                                    protected Boolean doInBackground(Integer... params) {
                                                        try {
                                                            aRecordDAO.delete(adapter.getListRecord().get(params[0]));
                                                        } catch (DAOException e) {
                                                            e.printStackTrace();
                                                            return false;
                                                        }
                                                        return true;
                                                    }

                                                    @Override
                                                    protected void onPostExecute(Boolean aBoolean) {
                                                        super.onPostExecute(aBoolean);
                                                        if (aBoolean) {
                                                            Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_LONG).show();
                                                            updateData();
                                                        } else {
                                                            Toast.makeText(MainActivity.this, "Xóa không thành công", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                }.execute(position);
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //No button clicked
                                                break;
                                        }
                                    }
                                };
                                ARecord aRecord = adapter.getListRecord().get(position);
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("Are you sure delete '" + aRecord.getReason() + "' with '" + aRecord.getAmount() + "'?").setPositiveButton("Yes", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();
                                break;
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        updateData();
    }

    private void updateData() {
        // asynctask load
        new AsyncTask<Void, Void, List<ARecord>>() {
            @Override
            protected List<ARecord> doInBackground(Void... params) {
                try {
                    switch (typeOfSumary){
                        case SUMARY_BY_DAY:
                            return aRecordDAO.loadByDay(chooseDate,isASC);
                        case SUMARY_BY_WEEK:
                            return aRecordDAO.loadByWeek(chooseDate, isASC);
                        case SUMARY_BY_MONTH:
                            return aRecordDAO.loadByMonth(chooseDate, isASC);
                        case SUMARY_BY_YEAR:
                            return aRecordDAO.loadByYear(chooseDate, isASC);
                        case SUMARY_BY_2_DAY:
                            // TODO
                            return aRecordDAO.loadBy2Day(new Date(), new Date(), isASC);
                        default:
                            return null;
                    }
                } catch (DAOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<ARecord> aRecords) {
                super.onPostExecute(aRecords);
                adapter.setListRecord(aRecords);
                adapter.notifyDataSetChanged();
                tvSum.setText(ARecordDAOImpl.convert(sum(aRecords)));
            }
        }.execute();

        spTypeOfSum.setSelection(typeOfSumary);
        tvDate.setText(ARecordDAOImpl.getDateTime3(chooseDate));
    }
    private long sum(List<ARecord> list ){
        long sum = 0;
        for (ARecord r : list)
            sum+= r.getAmount();
        return sum;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_NEW_RECORE:
                switch (resultCode) {
                    case RESULT_ADD_NEW_RECORE_SUCCESS:
                        final String reason = data.getStringExtra(ARecordDAOImpl.C_REASON);
                        final int amount = data.getIntExtra(ARecordDAOImpl.C_AMOUNT, 0);
                        final String date = data.getStringExtra(ARecordDAOImpl.C_DATE);
                        // asynctask save
                        new AsyncTask<Void, Void, Integer>() {
                            @Override
                            protected Integer doInBackground(Void... params) {
                                try {
                                    aRecordDAO.save(new ARecord(reason, amount, ARecordDAOImpl.getDateTime2(date)));
                                } catch (DAOException e) {
                                    e.printStackTrace();
                                }
                                return 1;
                            }

                            @Override
                            protected void onPostExecute(Integer integer) {
                                super.onPostExecute(integer);
                                updateData();
                            }
                        }.execute();
                        break;
//bth thêm case REQUEST_UPDATE_RCURENT_RECORE:
                    case RESULT_UPDATE_RECORE_SUCCESS:
                        final String reason2 = data.getStringExtra(ARecordDAOImpl.C_REASON);
                        final int amount2 = data.getIntExtra(ARecordDAOImpl.C_AMOUNT, 0);
                        final String date2 = data.getStringExtra(ARecordDAOImpl.C_DATE);
                        final int id = data.getIntExtra(ARecordDAOImpl.C_ID, 0);
                        // asynctask save
                        new AsyncTask<Void, Void, Integer>() {
                            @Override
                            protected Integer doInBackground(Void... params) {
                                try {
                                    long res = aRecordDAO.update(new ARecord(id, reason2, amount2, ARecordDAOImpl.getDateTime2(date2)));
                                } catch (DAOException e) {
                                    e.printStackTrace();
                                }
                                return 1;
                            }

                            @Override
                            protected void onPostExecute(Integer integer) {
                                super.onPostExecute(integer);
                                updateData();
                                Toast.makeText(MainActivity.this, "Update thành công",Toast.LENGTH_LONG).show();
                            }
                        }.execute();
                }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mnBtnAdd) {
            addNewRecord();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
