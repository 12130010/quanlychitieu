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
    public List<ARecord> loadByDay(Date date) throws DAOException {
        List<ARecord> list = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from " + TABLE_NAME, null );
           /* Cursor res =  db.rawQuery( "select * from " + TABLE_NAME + " where " + C_DATE + " between  '2010-01-01 00:00:00' and '2016-01-01 00:00:00' ", null );*/
            res.moveToFirst();

            while(res.isAfterLast() == false){
                list.add(new ARecord(res.getInt(res.getColumnIndex(C_ID)),  res.getString(res.getColumnIndex(C_REASON)),res.getInt(res.getColumnIndex(C_AMOUNT)), getDateTime2(res.getString(res.getColumnIndex(C_DATE)))));
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
    public List<ARecord> loadByWeek(Date date) throws DAOException {
        return null;
    }

    @Override
    public List<ARecord> loadByMonth(Date date) throws DAOException {
        return null;
    }

    @Override
    public List<ARecord> loadByYear(Date date) throws DAOException {
        return null;
    }

    @Override
    public List<ARecord> loadBy2Day(Date from, Date to) throws DAOException {
        return null;
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
    public static Date getDateTime2(String date){
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
