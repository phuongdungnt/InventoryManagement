package com.nat.inventorymanagement.HistoryAdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;
import com.nat.inventorymanagement.Adapter.GoodSellAdapterActivity;
import com.nat.inventorymanagement.R;
import com.nat.inventorymanagement.module.Good;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.huawei.agconnect.auth.VerifyCodeSettings.ACTION_RESET_PASSWORD;

public class HistoryAddGoodActivity extends AppCompatActivity {

    RecyclerView recyclerViewH;
    List<Good> listS;
    GoodSellAdapterActivity goodSellAdapterH;
    ImageView icBack;
    AGConnectUser user;
    String userID;
    int li;
    TextView tb;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_add_good);
        user = AGConnectAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        getLinkViews();
        getDatabase();
        getControls();


    }
    private void getControls(){
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void getLinkViews(){
        listS = new ArrayList<>();
        icBack = findViewById(R.id.ic_back);
        recyclerViewH = findViewById(R.id.recyclerview);
        goodSellAdapterH = new GoodSellAdapterActivity(HistoryAddGoodActivity.this);
        recyclerViewH.setAdapter(goodSellAdapterH);
        tb = findViewById(R.id.tb);
        recyclerViewH.setLayoutManager(new LinearLayoutManager(HistoryAddGoodActivity.this));
    }
    private void checkSP(){
        if (li != 0){
            tb.setVisibility(View.GONE);
        }else{
            tb.setVisibility(View.VISIBLE);
            recyclerViewH.setVisibility(View.GONE);
        }
    }

    private void getDatabase(){
        FirebaseDatabase f = FirebaseDatabase.getInstance();
        DatabaseReference d = f.getReference(userID).child("LichSu");

        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Good gS = dataSnapshot.getValue(Good.class);
                    listS.add(gS);
                    li = listS.size();
                    checkSP();
                }
                Collections.reverse(listS);
                goodSellAdapterH.addListGoodS(listS);
                listS.clear();
            }

            @Override
            public void onCancelled( @NotNull DatabaseError error) {
                Toast.makeText(HistoryAddGoodActivity.this, "Tải dữ liệu lỗi!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}