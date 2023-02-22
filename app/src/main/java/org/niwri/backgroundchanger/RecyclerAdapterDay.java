package org.niwri.backgroundchanger;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapterDay extends RecyclerView.Adapter<RecyclerAdapterDay.MyViewHolder> {

    boolean[] daysEnable;

    char[] dayChar = {'S', 'M', 'T', 'W', 'T', 'F', 'S'};

    int offColor = 0xff << 24 | 0x55 << 16 | 0x55 << 8 | 0x55;
    int onColor = 0xff << 24 | 0x38 << 16 | 0xb8 << 8 | 0xfb;

    public RecyclerAdapterDay(boolean[] daysEnable) {
        this.daysEnable = daysEnable;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtWeekday;
        private ImageView imgWeekday;
        MyViewHolder(final View view) {
            super(view);
            txtWeekday = view.findViewById(R.id.txtWeekday);
            imgWeekday = view.findViewById(R.id.imgWeekday);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterDay.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_weekdays, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterDay.MyViewHolder holder, int position) {
        holder.txtWeekday.setText(String.valueOf(dayChar[position]));

        holder.txtWeekday.setTextColor(offColor);
        holder.imgWeekday.setVisibility(View.INVISIBLE);

        if(daysEnable[position]) {
            //Sets color as #5555FF
            holder.txtWeekday.setTextColor(onColor);
            holder.imgWeekday.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return daysEnable.length;
    }
}
