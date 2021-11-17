package com.nat.inventorymanagement.Adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nat.inventorymanagement.HistoryAdd.DetailHistoryAddActivity;
import com.nat.inventorymanagement.HistoryAdd.HistoryAddGoodActivity;
import com.nat.inventorymanagement.R;
import com.nat.inventorymanagement.module.Good;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GoodSellAdapterActivity extends RecyclerView.Adapter<GoodSellAdapterActivity.MyViewHoler> {
    List<Good> sanphamS = new ArrayList<>();
    Context context;


    public void addListGoodS(List<Good> ListGood) {
        sanphamS.clear();
        sanphamS.addAll(ListGood);
        notifyDataSetChanged();
    }

    public GoodSellAdapterActivity(Context context){
        this.context = context;
    }

    @Override
    public GoodSellAdapterActivity.MyViewHoler onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View s = inflater.inflate(R.layout.activity_good_sell_adapter, parent, false );
        return new GoodSellAdapterActivity.MyViewHoler(s);
    }


    @NonNull
    @NotNull
    @Override
    public void onBindViewHolder( GoodSellAdapterActivity.MyViewHoler holder, int position) {
        Good gS =sanphamS.get(position);
        holder.txtName.setText(gS.getEtNameG());
        holder.txtCode.setText(gS.getEtCodeG());
        holder.txtTongTienBan.setText(String.valueOf(gS.getTongtien()));
        holder.enterTime.setText(gS.getEtTime());
        holder.enterDate.setText(gS.getEtDate());
        holder.itemS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailHistoryAddActivity.class);
                intent.putExtra("sanpham", gS);
                context.startActivity(intent);
            }
        });
        int check= gS.getCheck();
        if(check ==0){
            holder.txtTongTienBan.setTextColor(Color.GREEN);
            holder.txtsub.setText("+");
            holder.txtsub.setTextColor(Color.GREEN);
            holder.txtvnd.setTextColor(Color.GREEN);
        }else if(check==1){
            holder.txtTongTienBan.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return sanphamS.size();
    }

    public class MyViewHoler  extends RecyclerView.ViewHolder{
        LinearLayout itemS;
        TextView txtName, txtCode, txtTongTienBan, enterTime, enterDate, txtMadeIn, txtDongia, txtvnd, txtsub,txtSoLuongBan;
        public MyViewHoler(View itemViewS) {
            super(itemViewS);
            itemS = itemViewS.findViewById(R.id.GoodHistorySell);
            txtName = itemViewS.findViewById(R.id.txtNameS);
            txtCode = itemViewS.findViewById(R.id.txtCodeS);
            txtDongia = itemViewS.findViewById(R.id.etPriceG);
            txtMadeIn = itemViewS.findViewById(R.id.etMadeIn);
            txtTongTienBan = itemViewS.findViewById(R.id.txtTongTienBan);
            enterDate =itemViewS.findViewById(R.id.enterDateS);
            enterTime = itemViewS.findViewById(R.id.enterTimeS);
//            txtSoLuongBan = itemViewS.findViewById(R.id.);
            txtsub = itemViewS.findViewById(R.id.txt_sub);
            txtvnd = itemViewS.findViewById(R.id.txt_vnd);
        }
    }

}