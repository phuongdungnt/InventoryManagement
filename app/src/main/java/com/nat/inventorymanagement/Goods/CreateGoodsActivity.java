package com.nat.inventorymanagement.Goods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.nat.inventorymanagement.R;
import com.nat.inventorymanagement.Start.MainActivity;
import com.nat.inventorymanagement.module.Good;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateGoodsActivity extends AppCompatActivity {
    ImageView icBack, imgQRCode ;
    EditText etCodeG, etNameG, etPriceG, etMadeIn, etSl, etNote, etTime, etDate;
    TextView btnAdd;

    AGConnectUser user;
    String userID;
         //khai báo firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    private String codeG;
    private String nameG;
    private String giaG;
    private String slG;
    private String madeinG;
    private String noteG;
    private static final int DEFAULT_VIEW = 111;
    private static final int REQUEST_CODE_SCAN = 0X01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goods);

        user = AGConnectAuth.getInstance().getCurrentUser();
        userID = user.getUid();


        getView();
        getControls();
        newClick();


        //ẩn edittext
        etTime.setVisibility(View.GONE);
        etDate.setVisibility(View.GONE);


        imgQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = ScanUtil.startScan(CreateGoodsActivity.this, REQUEST_CODE_SCAN, new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create());
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeG = etCodeG.getText().toString();
                nameG = etNameG.getText().toString();
                giaG = etPriceG.getText().toString();
                slG = etSl.getText().toString();
                madeinG = etMadeIn.getText().toString();
                noteG = etNote.getText().toString();
                if(codeG.isEmpty() || nameG.isEmpty() ||madeinG.isEmpty() || giaG.isEmpty() || slG.isEmpty() || noteG.isEmpty()){
                    Toast.makeText(CreateGoodsActivity.this, "Cần nhập đủ thông tin", Toast.LENGTH_LONG).show();
                }else {
                    //lấy giờ , ngày
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat TimeG = new SimpleDateFormat("HH:mm:ss");
                    etTime.setText(TimeG.format(calendar.getTime()));
                    SimpleDateFormat DateG = new SimpleDateFormat("E dd/MM/yy");
                    etDate.setText(DateG.format(calendar.getTime()));

                    //đẩy dlieu lên firebase
                    reference.child(userID).child("LichSu").push().setValue(getGoodInfo());

                    String id = etCodeG.getText().toString();
                    QueryIDProduct("SanPham", id);
                    Intent intent = new Intent(CreateGoodsActivity.this, GoodsActivity.class);
                    startActivity(intent);
                    finish();

                }
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

    private void getView() {
        icBack = findViewById(R.id.ic_back);
        etCodeG = findViewById(R.id.etCodeG);
        imgQRCode = findViewById(R.id.imgQRCode);
        etNameG = findViewById(R.id.etNameG);
        etMadeIn = findViewById(R.id.etMadeIn);
        etPriceG = findViewById(R.id.etPriceG);
        etSl = findViewById(R.id.etSl);
        etNote = findViewById(R.id.etNote);
        btnAdd = findViewById(R.id.btnAdd);
        icBack = findViewById(R.id.ic_back);
        etTime = findViewById(R.id.etTime);
        etDate = findViewById(R.id.etDate);
    }
    private Good getGoodInfo(){
        String code = etCodeG.getText().toString();
        String name = etNameG.getText().toString();
        String time = etTime.getText().toString();
        String date = etDate.getText().toString();
        String madein = etMadeIn.getText().toString();
        String sl = etSl.getText().toString();
        String note = etNote.getText().toString();
        String price = etPriceG.getText().toString();
        int SL = Integer.valueOf(sl);
        int dg = Integer.valueOf(price);
        int tongtien = SL * dg;
                return new Good(code, name,time, date, madein, note,dg, SL, tongtien, 0, 0 );
    }

    public void QueryIDProduct(String add, String id){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(userID).child(add).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Good good = snapshot.getValue(Good.class);
                    String sl = etSl.getText().toString();
                    int t = Integer.valueOf(sl);
                    int slcu = Integer.valueOf(good.getEtSL());

                    good.setEtSL((t + slcu));

                    reference.child(userID).child("SanPham").child(etCodeG.getText().toString()).setValue(good);
                    Toast.makeText(CreateGoodsActivity.this, "Cập nhật số lượng thành công", Toast.LENGTH_LONG).show();
                }else {
                    reference.child(userID).child("SanPham").child(etCodeG.getText().toString()).setValue(getGoodInfo());
                    Toast.makeText(CreateGoodsActivity.this,"Thêm sản phẩm mới thành công", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });
    }
    public void newClick(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            this.requestPermissions(
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    DEFAULT_VIEW);
        }
    }
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions == null || grantResults == null || grantResults.length < 2 ||grantResults[0] != PackageManager.PERMISSION_GRANTED ||grantResults[1] != PackageManager.PERMISSION_GRANTED){
            return;
        }
    }
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode !=RESULT_OK || data == null){
            return;
        }
        if (requestCode == REQUEST_CODE_SCAN){
            HmsScan obj = data.getParcelableExtra(ScanUtil.RESULT);
            if(obj instanceof HmsScan){
                if(!TextUtils.isEmpty(((HmsScan) obj).getOriginalValue())){
                    Toast.makeText(CreateGoodsActivity.this, ((HmsScan) obj).getOriginalValue(),
                    Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject((obj.getOriginalValue()));
                       etCodeG.setText(jsonObject.getString("code"));
                       etNameG.setText(jsonObject.getString("name"));
                       etSl.setText(jsonObject.getString("soluong"));
                       etPriceG.setText(jsonObject.getString("dongia"));
                       etMadeIn.setText(jsonObject.getString("madein"));
                       etNote.setText(jsonObject.getString("note"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        }
    }
}