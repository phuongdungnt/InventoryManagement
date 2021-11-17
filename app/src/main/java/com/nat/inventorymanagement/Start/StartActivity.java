package com.nat.inventorymanagement.Start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.hmf.tasks.Continuation;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.nat.inventorymanagement.Login.LoginActivity;
import com.nat.inventorymanagement.R;
import com.nat.inventorymanagement.Signup.SignupActivity;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {
    TextView txtNameApp;
    ImageView icLogo;
    Animation topAnim, bottomAnim;

    private TimerTask timerTask;

    private static int SPLASH_TIME_OUT = 3000;
    private static final String TAG = "StartAppActivity";
    public String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getView();

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        icLogo.setAnimation(topAnim);
        txtNameApp.setAnimation(bottomAnim);

        AGConnectUser user = AGConnectAuth.getInstance().getCurrentUser();

        if (user != null)
            userEmail = user.getEmail();
        Timer RunSplash = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (userEmail != null) {
                            Intent intent = new Intent(StartActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(StartActivity.this, "Xin chào !",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(StartActivity.this, "Đăng nhập tài khoản của bạn để tiếp tục !", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        };
        RunSplash.schedule(timerTask, SPLASH_TIME_OUT);
    }

    private void getView(){
        txtNameApp=findViewById(R.id.txtNameApp);
        icLogo=findViewById(R.id.icLogo);
    }
}