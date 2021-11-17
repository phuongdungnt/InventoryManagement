package com.nat.inventorymanagement.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.nat.inventorymanagement.Fragment.ProfileFragment;
import com.nat.inventorymanagement.Goods.CreateGoodsActivity;
import com.nat.inventorymanagement.R;
import com.nat.inventorymanagement.module.Good;
import com.nat.inventorymanagement.module.user;

import org.jetbrains.annotations.NotNull;


public class EditProfileActivity extends AppCompatActivity {
    ImageView icBack;
    EditText etnameshoppr, etnamepr, etdiachi, etsdt;
    TextView btnsavepr;
    private String nameshoppr;
    private String namepr;
    private String diachipr;
    private String sdt;

    AGConnectUser user;
    String userID;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        user = AGConnectAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        etnameshoppr = findViewById(R.id.et_nameshoppr);
        etnamepr  = findViewById(R.id.et_namepr);
        etdiachi = findViewById(R.id.et_diachipr);
        etsdt = findViewById(R.id.et_sdtpr);
        btnsavepr = findViewById(R.id.btn_savepr);
        icBack = findViewById(R.id.ic_back);

        readFB("users", userID);
        readFB2("profile", userID);

        btnsavepr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editprofile();
            }
        });
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void editprofile() {
        nameshoppr = etnameshoppr.getText().toString();
        namepr = etnamepr.getText().toString();
        diachipr = etdiachi.getText().toString();
        sdt = etsdt.getText().toString();

        if (nameshoppr.isEmpty() || namepr.isEmpty() || diachipr.isEmpty() || sdt.isEmpty()) {
            Toast.makeText(this, "Cần nhập đủ thông tin profile", Toast.LENGTH_SHORT).show();
        } else {
            user us = new user(diachipr, nameshoppr, sdt);
            reference.child("profile").child(userID).setValue(us);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("users").child(userID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    user user = snapshot.getValue(user.class);
                                    user.setFullName(namepr);

                                    //cập nhật tên mới lên firebase
                                    databaseReference.child("users").child(userID).setValue(user);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }

                        });

            alertDialogTest();

        }
    }

    public void readFB(String DB, String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(DB).child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            user us = snapshot.getValue(user.class);
                            etnamepr.setText(us.getFullName());

                        } else {
                            Toast.makeText(EditProfileActivity.this, "Tải dữ liệu lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(EditProfileActivity.this, "Tải dữ liệu lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void alertDialogTest(){
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(this);
        alertdialog.setTitle("Thông Báo !");
        alertdialog.setIcon(R.drawable.ic_logo);
        alertdialog.setMessage("Cập nhật thành công!!!");
        alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertdialog.show();
    }

    public void readFB2(String DB, String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(DB).child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            user us = snapshot.getValue(user.class);
                            etnameshoppr.setText(us.getNameshop());
                            etdiachi.setText(us.getAddress());
                            etsdt.setText(us.getPhone());

                        } else {
                            Toast.makeText(EditProfileActivity.this, "Tải dữ liệu lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(EditProfileActivity.this, "Tải dữ liệu lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}