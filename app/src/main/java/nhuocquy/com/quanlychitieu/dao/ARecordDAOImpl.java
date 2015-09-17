package nhuocquy.com.quanlychitieu.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nhuocquy.com.quanlychitieu.model.ARecord;

/**
 * Created by NhuocQuy on 9/15/2015.
 */
public class ARecordDAOImpl extends SQLiteOpenHelper implements ARecordDAO {
    public static final String DATABASE_NAME = "quanly.db";
    public static final String TABLE_NAME = "arecord";
    public static final String C_ID = "id";
    public static final String C_REASON = "reason";
    public static final String C_DATE = "dates";
    public static final String C_AMOUNT = "amount";

    static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat dateFormat2 = new SimpleDateFormat(
            "dd/MM/yyyy   HH:mm");

    public ARecordDAOImpl(Context context) {
        super(context, "dbname", null, 1);
    }

    @Override
    public long save(ARecord aRecord) throws DAOException {
        long res = 0;
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(C_REASON, aRecord.getReason());
            contentValues.put(C_AMOUNT, aRecord.getAmount());
            contentValues.put(C_DATE, getDateTime(aRecord.getDate()));
            res = db.insert(TABLE_NAME, null, contentValues);
        } catch (SQLiteException e) {
            throw new DAOException(e.getMessage());
        } finally {
            if (db != null)
                db.close();
        }
        return res;
    }

    @Override
    public long update(ARecord aRecord) throws DAOException {
        long res = 0;
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(C_REASON, aRecord.getReason());
            contentValues.put(C_AMOUNT, aRecord.getAmount());
            contentValues.put(C_DATE, getDateTime(aRecord.getDate()));
            res = db.update(TABLE_NAME, contentValues, C_ID + " = ?", new String[]{String.valueOf(aRecord.getId())});
        } catch (SQLiteException e) {
            throw new DAOException(e.getMessage());
        } finally {
            if (db != null)
                db.close();
        }
        return res;
    }

    @Override
    public long delete(ARecord aRecord) throws DAOException {
        long res = 0;
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            res = db.delete(TABLE_NAME, C_ID + " = ?", new String[]{String.valueOf(aRecord.getId())});
        } catch (SQLiteException e) {
            throw new DAOException(e.getMessage());
        } finally {
            if (db != null)
                db.close();
        }
        return res;
    }

    @Override
    public List<ARecord> loadByDay(Date date, boolean isASC) throws DAOException {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        date = c.getTime();

        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return loadBy2Day(date, c.getTime(),isASC);
    }

    @Override
    public List<ARecord> loadByWeek(Date date, boolean isASC) throws DAOException {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        date = c.getTime();

        c.add(Calendar.DATE, 6);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return loadBy2Day(date, c.getTime(),isASC);
    }

    @Override
    public List<ARecord> loadByMonth(Date date, boolean isASC) throws DAOException {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        date = c.getTime();

        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DATE, -1);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return loadBy2Day(date, c.getTime(), isASC);
    }

    @Override
    public List<ARecord> loadByYear(Date date, boolean isASC) throws DAOException {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        date = c.getTime();

        c.add(Calendar.YEAR, 1);
        c.add(Calendar.DATE, -1);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return loadBy2Day(date, c.getTime(),isASC);
    }

    @Override
    public List<ARecord> loadBy2Day(Date from, Date to, boolean isASC) throws DAOException {
        List<ARecord> list = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where " + C_DATE + " between  ? and ? order by " + C_DATE + (isASC ? " ASC" : " DESC"), new String[]{ARecordDAOImpl.getDateTime(from), ARecordDAOImpl.getDateTime(to)});
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                list.add(new ARecord(res.getInt(res.getColumnIndex(C_ID)), res.getString(res.getColumnIndex(C_REASON)), res.getInt(res.getColumnIndex(C_AMOUNT)), getDateTime2(res.getString(res.getColumnIndex(C_DATE)))));
                res.moveToNext();
            }
        } catch (SQLiteException e) {
            throw new DAOException(e.getMessage());
        } finally {
            if (db != null)
                db.close();
        }
        return list;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME +
                        "( " + C_ID + " integer primary key AUTOINCREMENT, " + C_REASON + " text, " + C_AMOUNT + " interger, " + C_DATE + " DATETIME)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static String getDateTime(Date date) {
        return dateFormat.format(date);
    }

    /**
     * Bỏ seccond
     * @param date
     * @return
     */
    public static String getDateTime3(Date date) {
        String s =dateFormat2.format(date);
        return s;
//        String[] arrs = s.split(" ");
//        return arrs[0] + "\n" + arrs[1];
    }

    public static Date getDateTime2(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String convert(long n){
        StringBuilder sb = new StringBuilder();
        long du = 0;
        int dot = 0;
        while(n > 0){
            if(dot == 3){
                dot = 0;
                sb.insert(0, '.');
            }
            du = n%10;
            n = n / 10;
            sb.insert(0,du);
            dot++;
        }

        return sb.toString();
    }
}
