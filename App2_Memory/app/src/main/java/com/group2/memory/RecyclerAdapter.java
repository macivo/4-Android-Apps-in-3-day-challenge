package com.group2.memory;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private List<Bitmap> pics;
    private List<String> qrValue;

    public RecyclerAdapter(List<Bitmap> pics, List<String> qrValue) {
        this.pics = pics;
        this.qrValue = qrValue;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.RecyclerViewHolder holder, int position) {
        holder.pic.setImageBitmap(pics.get(position));
        holder.value.setText(qrValue.get(position));
    }

    @Override
    public int getItemCount() {
        return qrValue.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageView pic;
        TextView value;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.imageView);
            value = itemView.findViewById(R.id.textView);
        }
    }
}
