package org.niwri.backgroundchanger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    private ArrayList<BackgroundImage> backgroundList;
    private Context parentContext;

    public RecyclerAdapter(ArrayList<BackgroundImage> backgroundList) {
        this.backgroundList = backgroundList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTxt, dateTxt, timeTxt;
        private ImageView backgroundImg;
        private ConstraintLayout constraintBackground;
        private Switch onOff;

        public MyViewHolder(final View view) {
            super(view);

            nameTxt = view.findViewById(R.id.backgroundName);
            dateTxt = view.findViewById(R.id.backgroundDate);
            timeTxt = view.findViewById(R.id.backgroundTime);
            backgroundImg = view.findViewById(R.id.backgroundImage);
            onOff = view.findViewById(R.id.backgroundEnable);
            constraintBackground = view.findViewById(R.id.backgroundContainer);

        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(lp);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        holder.nameTxt.setText(backgroundList.get(position).getBackgroundName());
        holder.dateTxt.setText("Every " + backgroundList.get(position).getBackgroundDate().getDay());
        holder.timeTxt.setText(backgroundList.get(position).getBackgroundDate().getHour() + ":" + backgroundList.get(position).getBackgroundDate().getMinute());
        holder.backgroundImg.setImageBitmap(backgroundList.get(position).getBackgroundBitmap());
        holder.onOff.setChecked(backgroundList.get(position).isEnabled());

        holder.constraintBackground.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               System.out.println("Test");
               Toast.makeText(parentContext, "Test", Toast.LENGTH_SHORT);
           }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentContext = recyclerView.getContext();
    }


    @Override
    public int getItemCount() {
        return backgroundList.size();
    }
}
