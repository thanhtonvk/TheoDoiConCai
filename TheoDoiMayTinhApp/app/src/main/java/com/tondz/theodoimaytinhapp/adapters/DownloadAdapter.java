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
import com.tondz.theodoimaytinhapp.models.Download;

import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {
    Context context;
    List<Download> DownloadList;

    public DownloadAdapter(Context context, List<Download> DownloadList) {
        this.context = context;
        this.DownloadList = DownloadList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_download, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Download Download = DownloadList.get(position);
        holder.tvPath.setText("Đường dẫn: " + Download.getTargetPath().strip());
        holder.tvTime.setText("Thời gian: " + Download.getTime());
        holder.tvSize.setText("Kích thước: " + Download.getTotalBytes() + " byte");
    }

    @Override
    public int getItemCount() {
        return DownloadList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPath, tvSize, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPath = itemView.findViewById(R.id.tvPath);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvSize = itemView.findViewById(R.id.tvSize);
        }
    }
}
