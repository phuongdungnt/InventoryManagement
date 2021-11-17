package com.nat.inventorymanagement.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nat.inventorymanagement.Goods.GoodsActivity;
import com.nat.inventorymanagement.HistoryAdd.HistoryAddGoodActivity;
import com.nat.inventorymanagement.R;
import com.nat.inventorymanagement.Start.MainActivity;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }
    LinearLayout MenuGood, MenuHistoryAdd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View abc =inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        MenuGood = abc.findViewById(R.id.menu_good);
        MenuHistoryAdd = abc.findViewById(R.id.menu_historyadd);


        MenuGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tentmenugood = new Intent(getContext(), GoodsActivity.class);
                startActivity(tentmenugood);
            }
        });

        MenuHistoryAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tentmenuhistoryadd = new Intent(getContext(), HistoryAddGoodActivity.class);
                startActivity(tentmenuhistoryadd);
            }
        });
        return abc;
    }
}