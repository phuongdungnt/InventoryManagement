package com.nat.inventorymanagement.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.nat.inventorymanagement.Profile.EditProfileActivity;
import com.nat.inventorymanagement.R;
import com.nat.inventorymanagement.Start.StartActivity;
import com.nat.inventorymanagement.module.user;

import static android.os.Build.ID;

public class ProfileFragment extends Fragment {
    TextView txtEmail, txtName, txtAddress, txtPhone, txtNameshop;
    String ID;
    LinearLayout abc;
    ImageView EditProfile;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View bcd = inflater.inflate(R.layout.fragment_profile, container, false);
        EditProfile = bcd.findViewById(R.id.edit_prf);
        abc = bcd.findViewById(R.id.btn_signout);
        txtEmail = bcd.findViewById(R.id.txt_email);
        txtNameshop = bcd.findViewById(R.id.txt_nameshop);
        txtName = bcd.findViewById(R.id.txt_username);
        txtPhone = bcd.findViewById(R.id.txt_phone);
        txtAddress = bcd.findViewById(R.id.txt_address);
        ID = AGConnectAuth.getInstance().getCurrentUser().getUid();

        readFB2("profile", ID);
        readFB("users", ID);

        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tenteditprf = new Intent(getContext(), EditProfileActivity.class);
                startActivity(tenteditprf);
            }
        });
        abc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AGConnectAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), StartActivity.class));
                getActivity().finish();
            }
        });
        return bcd;
    }
    public void readFB(String DB, String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(DB).child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            user us = snapshot.getValue(user.class);
                            txtEmail.setText(us.getEmail());
                            txtName.setText(us.getFullName());

                        } else {
                            Toast.makeText(getContext(), "Tải dữ liệu lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(getContext(), "Tải dữ liệu lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void readFB2(String DB, String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(DB).child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            user us = snapshot.getValue(user.class);
                            txtNameshop.setText(us.getNameshop());
                            txtAddress.setText(us.getAddress());
                            txtPhone.setText(us.getPhone());

                        } else {
                            Toast.makeText(getContext(), "Tải dữ liệu lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(getContext(), "Tải dữ liệu lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}