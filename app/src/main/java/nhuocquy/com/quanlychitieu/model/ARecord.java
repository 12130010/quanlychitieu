package nhuocquy.com.quanlychitieu.model;

import java.util.Date;

/**
 * Created by NhuocQuy on 9/15/2015.
 */
public class ARecord {
    private int id;
    private String reason;
    private int amount;
    private Date date;
    public ARecord(){

    }
    public ARecord(int id, String reason, int amount, Date date) {
        this.id = id;
        this.reason = reason;
        this.amount = amount;
        this.date = date;
    }

    public ARecord(String reason, int amount, Date date) {
        this.reason = reason;
        this.amount = amount;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public int getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }
}
