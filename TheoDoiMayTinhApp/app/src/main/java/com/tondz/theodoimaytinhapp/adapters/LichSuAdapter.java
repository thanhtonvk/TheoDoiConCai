package com.tondz.theodoimaytinhapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tondz.theodoimaytinhapp.R;
import com.tondz.theodoimaytinhapp.models.History;

import java.util.List;

public class LichSuAdapter extends RecyclerView.Adapter<LichSuAdapter.ViewHolder> {
    Context context;
    List<History> historyList;

    public LichSuAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = historyList.get(position);
        holder.tvUrl.setText("Link: " + history.getUrl().strip());
        holder.tvTitle.setText("Tiêu đề: " + history.gettitle().strip());
        holder.tvCount.setText("Số lần: " + history.getVisitCount());
        holder.tvTime.setText("Thời gian: " + history.getLastVisitTime());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUrl, tvTitle, tvCount, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUrl = itemView.findViewById(R.id.tvUrl);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
