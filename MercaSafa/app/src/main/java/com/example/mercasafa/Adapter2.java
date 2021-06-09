package com.example.mercasafa;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter2 extends RecyclerView.Adapter<Adapter2.View_Holder>{



    List<Objetos> list = Collections.emptyList();
    Context context;
    RecyclerViewClickListener2 listener;
    //private AdapterView.OnItemClickListener listener;
    //private AdapterView.OnClickListener listener;

    public Adapter2(List<Objetos> list, Application application, RecyclerViewClickListener2 listener) {
        this.list = list;
        this.context = application;
        this.listener=listener;

    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row2, parent, false);
        View_Holder holder = new View_Holder(v);
        //v.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.name.setText(list.get(position).getNombre());
        holder.desc.setText(list.get(position).getEmail());
        //holder.imageView.setImageURI(list.get(position).getUrl());

        Glide.with(this.context)
                .load(list.get(position).getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getImage());

    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    public class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        TextView desc;
        CircleImageView imageView;

        View_Holder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.txt_title2);
            desc = (TextView) itemView.findViewById(R.id.txt_desc2);
            imageView = (CircleImageView) itemView.findViewById(R.id.circleImageView2);
            itemView.setOnClickListener(this);

            //Implementa!


        }

        public ImageView getImage(){ return this.imageView;}

        @Override
        public void onClick(View v) {
            listener.OnClick(v,getAdapterPosition());

        }
    }
    public interface RecyclerViewClickListener2{
        void OnClick (View v, int position);
    }



}