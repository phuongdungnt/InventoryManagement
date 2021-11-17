package com.nat.inventorymanagement.Goods;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.nat.inventorymanagement.HistoryAdd.HistoryAddGoodActivity;
import com.nat.inventorymanagement.R;
import com.nat.inventorymanagement.ResetPass.ResetPasswordActivity;
import com.nat.inventorymanagement.Start.StartActivity;
import com.nat.inventorymanagement.module.Good;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailGoodsActivity extends AppCompatActivity {
    TextView dtName, dtCode, dtMadein, dtSL, dtPrice, dtNote, dtDaban, dtTime, dtDate;
    EditText etxuatSL;
    Good good;
    ImageView icBack;
    Button btnxuat, btnhuy, btnmua;
    LinearLayout xuat;
    AGConnectUser user;
    String userID;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_goods);
        user = AGConnectAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        getViews();
        getControls();
        getDataGood();
        setData();

        xuat.setVisibility(View.GONE);
        btnxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuat.setVisibility(View.VISIBLE);
                btnxuat.setVisibility(View.GONE);
            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuat.setVisibility(View.GONE);
                btnxuat.setVisibility(View.VISIBLE);
            }
        });

        btnmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etsl = etxuatSL.getText().toString();
                if(etsl.isEmpty()){
                        Toast.makeText(DetailGoodsActivity.this, "Không được để trống", Toast.LENGTH_LONG).show();
                }else{
                String id = good.getEtCodeG();
                QueryIDProduct("SanPham", id);
            }}
        });
    }


    private void setData() {
        dtCode.setText(good.getEtCodeG());
        dtName.setText(good.getEtNameG());
        dtPrice.setText(String.valueOf(good.getEtPrice()));
        dtNote.setText(good.getEtNote());
        dtTime.setText(good.getEtTime() + " " + good.getEtDate());
        dtMadein.setText(good.getEtMadeIn());
        dtSL.setText(String.valueOf(good.getEtSL()));
        dtDaban.setText(String.valueOf(good.getEtDaBan()));
    }


    //nhận dlieu từ adapter theo key Good
    private void getDataGood() {
        if (getIntent().hasExtra("Good")) {
            good = (Good) getIntent().getSerializableExtra("Good");
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    private void getViews() {
        good = new Good();
        dtCode = findViewById(R.id.dt_code);
        dtName = findViewById(R.id.dt_name);
        dtNote = findViewById(R.id.dt_note);
        dtPrice = findViewById(R.id.dt_price);
        icBack = findViewById(R.id.ic_back);
        dtMadein = findViewById(R.id.dt_madein);
        dtTime = findViewById(R.id.dt_time);
        dtSL = findViewById(R.id.dt_stock);
        xuat = findViewById(R.id.xuat);
        btnxuat = findViewById(R.id.btnxuat);
        etxuatSL = findViewById(R.id.etxuat_sl);
        btnhuy = findViewById(R.id.btnhuy_sp);
        btnmua = findViewById(R.id.btnmua_sp);
        dtDaban =findViewById(R.id.dt_sold);

    }
    public void QueryIDProduct (String add, String id){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(userID).child(add).child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Good good = snapshot.getValue(Good.class);
                            String slx = etxuatSL.getText().toString();
                            String name = good.getEtNameG();
                            String madein = good.getEtMadeIn();
                            String note = good.getEtNote();
                            String code = good.getEtCodeG();
                            int SL = Integer.valueOf(good.getEtSL());
                            String dongia = String.valueOf(good.getEtPrice());
                            Calendar calendar = Calendar.getInstance();

                            SimpleDateFormat TimeP = new SimpleDateFormat("HH:mm:ss");
                            String time=TimeP.format(calendar.getTime());
                            SimpleDateFormat DateP = new SimpleDateFormat("E dd/MM/yyyy");
                            String date= DateP.format(calendar.getTime());

                            int slm = Integer.valueOf(slx);
                            int slcu = Integer.valueOf(good.getEtSL());
                            int dg = Integer.valueOf(dongia);
                             int daban =good.getEtDaBan();
                             good.setEtDaBan(daban + slm);
                            if (slcu >= slm) {
                                good.setEtSL((slcu - slm));
                                int slmoinhat = slcu - slm;
                                int tongtien = dg * slm;
                                Good good1 = new Good(code, name, time, date, madein,note, dg, SL, tongtien, 1);
                                //Cap nhat so luong moi
                                reference.child(userID).child("SanPham").child(code).setValue(good);
                                reference.child(userID).child("LichSu").push().setValue(good1);
                                Toast.makeText(DetailGoodsActivity.this, "Xuất sản phẩm thành công !", Toast.LENGTH_SHORT).show();
                                if (slmoinhat == 0) {
                                    reference.child(userID).child("SanPham").child(code).removeValue();
                                }
                                Intent intent = new Intent(DetailGoodsActivity.this, HistoryAddGoodActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(DetailGoodsActivity.this, "Số lượng bạn đã xuất vượt quá số lượng có sẵn !!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
    }

    private void getControls() {
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}