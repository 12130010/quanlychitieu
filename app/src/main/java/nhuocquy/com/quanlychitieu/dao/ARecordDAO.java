package nhuocquy.com.quanlychitieu.dao;

import java.util.Date;
import java.util.List;

import nhuocquy.com.quanlychitieu.model.ARecord;

/**
 * Created by NhuocQuy on 9/15/2015.
 */
public interface ARecordDAO {
    long save(ARecord aRecord) throws DAOException;
    long update(ARecord aRecord) throws DAOException;
    long delete(ARecord aRecord) throws DAOException;
    List<ARecord> loadByDay(Date date, boolean isASC) throws DAOException;
    List<ARecord> loadByWeek(Date date, boolean isASC) throws DAOException;
    List<ARecord> loadByMonth(Date date, boolean isASC) throws DAOException;
    List<ARecord> loadByYear(Date date, boolean isASC) throws DAOException;
    List<ARecord> loadBy2Day(Date from, Date to, boolean isASC) throws DAOException;
}
