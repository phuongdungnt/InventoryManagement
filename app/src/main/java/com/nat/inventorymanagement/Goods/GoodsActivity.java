package com.nat.inventorymanagement.Goods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;
import com.nat.inventorymanagement.Adapter.GoodAdapterActivity;
import com.nat.inventorymanagement.R;
import com.nat.inventorymanagement.Start.MainActivity;
import com.nat.inventorymanagement.Start.StartActivity;
import com.nat.inventorymanagement.module.Good;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoodsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Good> list;
    GoodAdapterActivity goodAdapterActivity;
    AGConnectUser user;
    String userID;
    EditText etSearch;
    LinearLayout tb;
    int li;
    LinearLayout icBack;
    TextView btnCreateGood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        user = AGConnectAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        getView();
        getDatabase();
        TimKiem();


        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        btnCreateGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tencregood = new Intent(GoodsActivity.this, CreateGoodsActivity.class);
                startActivity(tencregood);
                finish();
            }
        });
    }
    private void getView() {
        icBack = findViewById(R.id.ic_back);
        btnCreateGood = findViewById(R.id.cre_good);
        list = new ArrayList<>();
        etSearch = findViewById(R.id.etSearch);
        tb = findViewById(R.id.tb);
        recyclerView =findViewById(R.id.recyclerview);
        goodAdapterActivity = new GoodAdapterActivity(GoodsActivity.this);
        recyclerView.setAdapter(goodAdapterActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(GoodsActivity.this));
    }

    private void getDatabase(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(userID).child("SanPham");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Good g = dataSnapshot.getValue(Good.class);
                    list.add(g);
                    li = list.size();
                    checkSp();
                }
                Collections.reverse(list);
                goodAdapterActivity.addListGood(list);
                list.clear();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(GoodsActivity.this, "Tải dữ liệu lỗi!", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private  void checkSp(){
        if(li!=0){
            tb.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            tb.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
    private void TimKiem(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                goodAdapterActivity.getFilter().filter(s.toString());
            }
        });
    }
}