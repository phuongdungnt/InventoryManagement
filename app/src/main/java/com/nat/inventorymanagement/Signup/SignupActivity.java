package com.nat.inventorymanagement.Signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.EmailUser;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskExecutors;
import com.nat.inventorymanagement.Login.LoginActivity;
import com.nat.inventorymanagement.R;
import com.nat.inventorymanagement.Start.MainActivity;
import com.nat.inventorymanagement.module.user;

import java.util.Locale;
import java.util.regex.Pattern;

import static com.huawei.agconnect.auth.VerifyCodeSettings.ACTION_REGISTER_LOGIN;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    EditText EditEmail, EditName, EditPassword, EditRetypePassword, EditVerify;
    TextView EmailAlert, NameAlert, PasswordAlert, RepasswordAlert;
    Button btnVerify;
    AGConnectUser userag;
    String userID;
    private TextView btnSignup, btnBackLogin;
    private AGConnectAuth huaweiAuth;
    private String email;
    private String name;
    private String password;
    private String confirmPassword;
    private String verCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnBackLogin = findViewById(R.id.btn_back_login);
        btnSignup = findViewById(R.id.btn_signup);
        EditEmail = findViewById(R.id.et_email);
        EditName = findViewById(R.id.et_name);
        EditPassword = findViewById(R.id.et_password);
        EditRetypePassword = findViewById(R.id.et_repassword);
        EditVerify = findViewById(R.id.edit_verify);
        btnVerify = findViewById(R.id.btn_verify);
        btnVerify.setOnClickListener(this);
        btnBackLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        huaweiAuth = AGConnectAuth.getInstance();

        //message
        EmailAlert = findViewById(R.id.email_alert);
        NameAlert = findViewById(R.id.name_alert);
        PasswordAlert = findViewById(R.id.pass_alert);
        RepasswordAlert = findViewById(R.id.repass_alert);


        EditEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pass=EditPassword.getText().toString().trim();
                validatePassword(pass);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private boolean validateEmail() {
        String emailInput = EditEmail.getText().toString().trim();
        if (emailInput.isEmpty()) {
            EmailAlert.setText("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            EmailAlert.setText("Please enter valid email address");
            return false;
        } else {
            EmailAlert.setText("");
            return true;
        }
    }

    private boolean validateName() {
        String nameInput = EditName.getText().toString().trim();
        if (nameInput.isEmpty()) {
            NameAlert.setText("Field can't be empty");
            return false;
        } else {
            NameAlert.setText("");
            return true;
        }
    }

    private void validatePassword(String wPass) {
        Pattern chuHoa= Pattern.compile("[A-Z]");
        Pattern chuThuong= Pattern.compile("[a-z]");
        Pattern number= Pattern.compile("[0-9]");
        Pattern kyTu= Pattern.compile("[@#$%^&+=]");

        if(!chuHoa.matcher(wPass).find()){
            PasswordAlert.setText("Phải có ít nhất 1 chữ hoa");
        }else if(!chuThuong.matcher(wPass).find()){
            PasswordAlert.setText("Phải có ít nhất 1 chữ thường");
        }else if(!number.matcher(wPass).find()) {
            PasswordAlert.setText("Phải có ít nhất 1 chữ số");
        }else if(!kyTu.matcher(wPass).find()) {
            PasswordAlert.setText("Phải có ít nhất 1 ký tự");
        }else if(wPass.length()<8) {
            PasswordAlert.setText("Phải có ít nhất 8 ký tự trở lên");
        } else PasswordAlert.setText(null);
    }

    private void sendCodeVerification() {
        email = EditEmail.getText().toString();
        if (email.isEmpty() || email == null) {
            Toast.makeText(this, "Không được để trống Email !!! ", Toast.LENGTH_LONG).show();
        } else {
            VerifyCodeSettings settings = VerifyCodeSettings.newBuilder()
                    .action(ACTION_REGISTER_LOGIN)
                    .sendInterval(30)
                    .locale(Locale.ENGLISH)
                    .build();
            Task<VerifyCodeResult> task = huaweiAuth.requestVerifyCode(email, settings);
            task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
                @Override
                public void onSuccess(VerifyCodeResult verifyCodeResult) {
                    Toast.makeText(SignupActivity.this, "Kiểm tra mã xác minh trong Email của bạn !", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(SignupActivity.this, "Lỗi khi gửi mã xác minh", Toast.LENGTH_SHORT).show();
                    Log.d("VerifyCodeErr", e.getMessage());
                }
            });


        }
    }

    private void signUpWithEmail() {
        email = EditEmail.getText().toString();
        name = EditName.getText().toString();
        password = EditPassword.getText().toString();
        verCode = EditVerify.getText().toString();
        confirmPassword = EditRetypePassword.getText().toString();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty() || verCode.isEmpty()) {
            Toast.makeText(this, "Không được để trống !", Toast.LENGTH_LONG).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu không trùng khớp !", Toast.LENGTH_SHORT).show();
        } else {
            EmailUser emailUser = new EmailUser.Builder().setEmail(email)
                    .setVerifyCode(verCode)
                    .setPassword(password).build();
            AGConnectAuth.getInstance().createUser(emailUser)
                    .addOnCompleteListener(new OnCompleteListener<SignInResult>() {
                        @Override
                        public void onComplete(Task<SignInResult> task) {
                            if (task.isSuccessful()) {
                                userag = AGConnectAuth.getInstance().getCurrentUser();
                                userID = userag.getUid();
                                //insert in realtime db firebase
                                user us = new user(name, email, userID, password);

                                insContactDB(us, userID);
                                //call back login form
                                Intent intent = new Intent();
                                intent.putExtra("uid", userID);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                setResult(RESULT_OK, intent);
                                Intent intt = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(intt);
                                finish();


                            } else {
                                Log.d("SignUpErr", task.getException().getMessage());
                                Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }


    private void insContactDB(user us, String userID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        reference.child("users").child(userID).setValue(us);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:
                if (!validateEmail() || !validateName()) {
                    Toast.makeText(this, "Kiểm tra lại thông tin!", Toast.LENGTH_SHORT).show();

                } else {
                    //register account
                    signUpWithEmail();
                }

                break;
            case R.id.btn_verify:
                //send verify code
                sendCodeVerification();
                break;
            case R.id.btn_back_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}