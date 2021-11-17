package com.nat.inventorymanagement.Adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nat.inventorymanagement.Goods.DetailGoodsActivity;
import com.nat.inventorymanagement.R;
import com.nat.inventorymanagement.module.Good;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GoodAdapterActivity extends RecyclerView.Adapter<GoodAdapterActivity.MyViewHoler> implements Filterable {
    List<Good> sanpham = new ArrayList<>();
    List<Good> sanphamold = new ArrayList<>();
    Context context;

    public void addListGood(List<Good> ListGood){
        sanpham.clear();
        sanpham.addAll(ListGood);
        sanphamold.addAll(ListGood);
        notifyDataSetChanged();
    }


    public GoodAdapterActivity(Context context){
        this.context = context;
    }
    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_good_adapter, parent, false);
        return new MyViewHoler(view);
    }
    @NonNull
    @NotNull


    //trả dlieu về cho 1 item của viewholer theo position
    @Override
    public void onBindViewHolder(MyViewHoler holder, int position) {
        Good g = sanpham.get(position);
        holder.txtName.setText(g.getEtNameG());
        holder.txtConlai.setText(String.valueOf(g.getEtSL()));
        holder.txtDaban.setText(String.valueOf(g.getEtDaBan()));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(context, DetailGoodsActivity.class);
                intent.putExtra("Good", g);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sanpham.size();
    }

    public class MyViewHoler extends RecyclerView.ViewHolder {
        LinearLayout item;
        TextView txtName, txtDaban, txtConlai, txtMadein, txtPrice, txtNote, txtCode, txtTime, txtDate;

        public MyViewHoler(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.Good);
            txtName = itemView.findViewById(R.id.txt_name);
            txtConlai = itemView.findViewById(R.id.txt_stock);
            txtDaban = itemView.findViewById(R.id.txt_sold);
            txtMadein = itemView.findViewById(R.id.dt_madein);
            txtTime = itemView.findViewById(R.id.dt_time);
            txtPrice = itemView.findViewById(R.id.dt_price);
            txtNote = itemView.findViewById(R.id.dt_note);

        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty()){
                    sanpham = sanphamold;
                }else {
                    List<Good> list = new ArrayList<>();
                    for (Good good:sanphamold){
                        if(good.getEtNameG().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(good);
                        }
                    }
                    sanpham =list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = sanpham;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                sanpham = (List<Good>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}