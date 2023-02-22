package org.niwri.backgroundchanger;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapterDay extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    boolean[] daysEnable;
    public RecyclerAdapterDay(boolean[] daysEnable) {
        this.daysEnable = daysEnable;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtWeekday;
        public MyViewHolder(final View view) {
            super(view);
            txtWeekday = view.findViewById(R.id.txtWeekday);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
