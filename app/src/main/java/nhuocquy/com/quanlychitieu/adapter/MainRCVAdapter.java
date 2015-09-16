package nhuocquy.com.quanlychitieu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nhuocquy.com.quanlychitieu.R;
import nhuocquy.com.quanlychitieu.dao.ARecordDAOImpl;
import nhuocquy.com.quanlychitieu.model.ARecord;

/**
 * Created by NhuocQuy on 9/15/2015.
 */
public class MainRCVAdapter extends RecyclerView.Adapter<MainRCVAdapter.ARecordViewHolder> {
    private List<ARecord> listRecord = new ArrayList<>();

    @Override
    public ARecordViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ARecordViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.arecord_view_holder, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ARecordViewHolder viewHolder, int i) {
        ARecord aRecord = listRecord.get(i);
        viewHolder.tvDate.setText(ARecordDAOImpl.getDateTime(aRecord.getDate()));
        viewHolder.tvAmount.setText(String.valueOf(aRecord.getAmount()));
        viewHolder.tvReason.setText(aRecord.getReason());
    }

    @Override
    public int getItemCount() {
        return listRecord.size();
    }

    public List<ARecord> getListRecord() {
        return listRecord;
    }

    public void setListRecord(List<ARecord> listRecord) {
        this.listRecord = listRecord;
    }

    class ARecordViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate, tvReason, tvAmount;
        public ARecordViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate_hd);
            tvReason = (TextView) itemView.findViewById(R.id.tvReason_hd);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount_hd);
        }
    }
}
