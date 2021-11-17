package com.nat.inventorymanagement.HistoryAdd;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nat.inventorymanagement.R;
import com.nat.inventorymanagement.module.Good;

public class DetailHistoryAddActivity extends AppCompatActivity {
    TextView nameH, codeH, slH, madeinH, dgH, Time, Date, tongtienH, noteH, slgd;
    Good goodH;
    ImageView icBack, imgin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history_add);
        getLinkViews();

        getDataGood();

        setDataViews();

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

    private void setDataViews(){
        codeH.setText(goodH.getEtCodeG());
        nameH.setText(goodH.getEtNameG());
        slH.setText(String.valueOf(goodH.getEtSL()));
        madeinH.setText(goodH.getEtMadeIn());
        Time.setText(goodH.getEtTime());
        Date.setText(goodH.getEtDate());
        Time.setText(goodH.getEtTime());
        dgH.setText(String.valueOf(goodH.getEtPrice()));
        tongtienH.setText(String.valueOf(goodH.getTongtien()));
        noteH.setText(goodH.getEtNote());
        int check= goodH.getCheck();
        if(check ==0){
         imgin.setImageResource(R.drawable.imgtransadd);
        }else if(check==1){
            imgin.setImageResource(R.drawable.imgtransout);
            slgd.setText("Số lượng bán:");
        }
    }

    private void getDataGood(){
        if(getIntent().hasExtra("sanpham")){
        goodH =(Good) getIntent().getSerializableExtra("sanpham");
    }else
        {
        Toast.makeText(DetailHistoryAddActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
    }
}
private void getLinkViews(){
        goodH = new Good();
        codeH = findViewById(R.id.txt_codeH);
        nameH = findViewById(R.id.txt_nameH);
        slH = findViewById(R.id.txt_slH);
        madeinH = findViewById(R.id.txt_madeinH);
        icBack = findViewById(R.id.ic_back);
        Time = findViewById(R.id.txt_TimeH);
        Date = findViewById(R.id.txt_DateH);
        dgH = findViewById(R.id.txt_dgH);
        tongtienH = findViewById(R.id.txt_tongtienH);
        noteH = findViewById(R.id.txt_noteH);
        imgin =findViewById(R.id.img_in);
        slgd =findViewById(R.id.txt_slgd);
}
}