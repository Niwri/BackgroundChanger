package org.niwri.backgroundchanger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

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
        private RecyclerView recyclerView;

        MyViewHolder(final View view) {
            super(view);

            nameTxt = view.findViewById(R.id.backgroundName);
            recyclerView = view.findViewById(R.id.backgroundDate);
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
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        holder.nameTxt.setText(backgroundList.get(position).getBackgroundName());
        holder.timeTxt.setText(String.format("%02d", backgroundList.get(position).getBackgroundDate().getHour())
                + ":" +
                String.format("%02d", backgroundList.get(position).getBackgroundDate().getMinute()));
        holder.backgroundImg.setImageBitmap(backgroundList.get(position).getBackgroundBitmap());
        holder.onOff.setChecked(backgroundList.get(position).isEnabled());

        holder.nameTxt.setTextColor(ContextCompat.getColorStateList(
                parentContext.getApplicationContext(),
                backgroundList.get(position).isEnabled() ? R.color.onColorTxt : R.color.offColorTxt));

        holder.timeTxt.setTextColor(ContextCompat.getColorStateList(
                parentContext.getApplicationContext(),
                backgroundList.get(position).isEnabled() ? R.color.onColorTxt : R.color.offColorTxt));

        //Rewrites enable boolean value stored in information.txt
        holder.onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                backgroundList.get(holder.getAdapterPosition()).setEnabled(b);

                holder.nameTxt.setTextColor(ContextCompat.getColorStateList(
                        parentContext.getApplicationContext(),
                        b ? R.color.onColorTxt : R.color.offColorTxt));

                holder.timeTxt.setTextColor(ContextCompat.getColorStateList(
                        parentContext.getApplicationContext(),
                        b ? R.color.onColorTxt : R.color.offColorTxt));

                try {
                    File infoFile = new File(backgroundList.get(holder.getAdapterPosition()).getFileDirectory() + "/information.txt");
                    FileInputStream information = new FileInputStream(infoFile);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(information));
                    StringBuffer inputBuffer = new StringBuffer();
                    String line;

                    //Copies file content
                    while ((line = reader.readLine()) != null)
                        inputBuffer.append(line + "\r\n");

                    line = inputBuffer.toString();

                    //Replaces enable data
                    line = line.replaceAll("Enable:((true)|(false))", "Enable:" + backgroundList.get(holder.getAdapterPosition()).isEnabled());
                    reader.close();

                    FileWriter fos = new FileWriter(infoFile, false);
                    fos.write(line);
                    fos.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        holder.constraintBackground.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               System.out.println("Test");
               Toast.makeText(parentContext, "Test", Toast.LENGTH_SHORT);
           }
        });

        //Sets up child recyclerView for weekday display
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setInitialPrefetchItemCount(backgroundList.get(position).getBackgroundDate().getDay().length);
        holder.recyclerView.setLayoutManager(layoutManager);
        RecyclerAdapterDay adapter = new RecyclerAdapterDay(backgroundList.get(position).getBackgroundDate().getDay());
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());

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
