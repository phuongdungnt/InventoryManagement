package com.nat.inventorymanagement.Login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.EmailAuthProvider;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.nat.inventorymanagement.R;
import com.nat.inventorymanagement.ResetPass.ResetPasswordActivity;
import com.nat.inventorymanagement.Signup.SignupActivity;
import com.nat.inventorymanagement.Start.MainActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    AGConnectUser user ;
    TextView btnLogin, CreateAccount, ForgotPass, txtPp;
    String userID;
    Toast outToast;
    CheckBox checkBox;
    ProgressBar progressBar;
    EditText EmailLogin, PasswordLogin;
    private long outApp;
    String email = "", password = "", uid = "";
    final static int CREATE_USER = 101;
    private static int SPLASH_TIME_OUT = 3000;
    FirebaseDatabase database;
    DatabaseReference reference;
    //silenty signIn
    AccountAuthParams authParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getView();
        progressBar.setVisibility(View.GONE);
        user = AGConnectAuth.getInstance().getCurrentUser();
        txtPp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tentprivicy = new Intent(LoginActivity.this, PrivacyPolicyActivity.class);
                startActivity(tentprivicy);
            }
        });
        ForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tentforgot = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(tentforgot);
                finish();
            }
        });

        btnLogin.setVisibility(View.GONE);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox.isChecked()){
                    btnLogin.setVisibility(View.VISIBLE);
                }else
                    btnLogin.setVisibility(View.GONE);
            }
        });
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("users");

        //silent signIn
        authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).createParams();
    }

    private void signInID(String email, String password){
        AGConnectAuthCredential credential = EmailAuthProvider.credentialWithPassword(email, password);
        AGConnectAuth.getInstance().signIn(credential)
                .addOnSuccessListener(new OnSuccessListener<SignInResult>() {
                    @Override
                    public void onSuccess(SignInResult signInResult) {
                        // Obtain sign-in information.

                        user = AGConnectAuth.getInstance().getCurrentUser();
                        userID = user.getUid();
                        callMainActivity(userID);
                        progressBar.setVisibility(View.VISIBLE);
                        closeKeyBoard();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác !", Toast.LENGTH_LONG).show();
                        Log.e("login error: ", e.getMessage());
                    }
                });
    }


    private void getView() {
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        CreateAccount = findViewById(R.id.cre_acc);
        CreateAccount.setOnClickListener(this);
        ForgotPass = findViewById(R.id.forgot_pass);
        EmailLogin = findViewById(R.id.et_emailLogin);
        PasswordLogin = findViewById(R.id.et_passwordLogin);
        txtPp = findViewById(R.id.txtPp);
        checkBox = findViewById(R.id.checkBox);
        progressBar = findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cre_acc:
                Intent intent =  new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(intent, CREATE_USER);
                this.finish();
                break;
            case R.id.btn_login:
                email = EmailLogin.getText().toString().trim();
                password = PasswordLogin.getText().toString().trim();
                if(!email.isEmpty()||!password.isEmpty()){
                    signInID(email, password);
                }
                break;
        }

    }
    private void closeKeyBoard(){
        View view =this.getCurrentFocus();
        if(view !=null){
            InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void callMainActivity(String userID) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_USER && resultCode == RESULT_OK) {
            uid = data.getStringExtra("uid");
            email = data.getStringExtra("email");
            password = data.getStringExtra("password");
        }
    }
    @Override
    public void onBackPressed() {
        if(outApp + 2000 > System.currentTimeMillis()){
            outToast.cancel();
            super.onBackPressed();
            return;
        }else{
            outToast = Toast.makeText(LoginActivity.this, "CLICK 1 lần nữa để thoát !", Toast.LENGTH_SHORT);
            outToast.show();
        }
        outApp=System.currentTimeMillis();
    }

}