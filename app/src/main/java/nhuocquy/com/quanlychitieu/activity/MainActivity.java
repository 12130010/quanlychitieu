package nhuocquy.com.quanlychitieu.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    public static final  int RESULT_ADD_NEW_RECORE_SUCCESS = 2;
    public static final int RESULT_ADD_NEW_RECORE_UNSUCCESS = 3;

    private int typeOfSum = 1;
    private ARecordDAO aRecordDAO = new ARecordDAOImpl(this);
    private MainRCVAdapter adapter;

    private Button button;
    private RecyclerView listRecord;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2015-09-12 20:37:20 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        button = (Button)findViewById( R.id.button );
        listRecord = (RecyclerView) findViewById(R.id.listRecord);

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

        setUp();
    }
    private void setUp(){
        adapter = new MainRCVAdapter();
        listRecord.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listRecord.setLayoutManager(linearLayoutManager);

        listRecord.setItemAnimator(new DefaultItemAnimator());

        listRecord.addOnItemTouchListener(new RecyclerItemClickListener(this, listRecord, new RecyclerItemClickListener.OnItemClickListener(){

            @Override
            public void onItemClick(View view, final int position) {
//                Toast.makeText( MainActivity.this,position +"", Toast.LENGTH_LONG).show();
                final CharSequence[] items = {"Thêm", "Sửa", "Xóa"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Action");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, final int item) {
//                        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                        switch (item){
                            case 2:
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
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
                                                        if(aBoolean){
                                                            Toast.makeText( MainActivity.this,"Xóa thành công", Toast.LENGTH_LONG).show();
                                                            updateData();
                                                        }else{
                                                            Toast.makeText( MainActivity.this,"Xóa không thành công", Toast.LENGTH_LONG).show();
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
    private void updateData(){
        // asynctask load
        new AsyncTask<Void, Void, List<ARecord>>() {
            @Override
            protected List<ARecord> doInBackground(Void... params) {
                try {
                    return aRecordDAO.loadByDay(null);
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
            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_ADD_NEW_RECORE:
                if(resultCode == RESULT_ADD_NEW_RECORE_SUCCESS) {
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
