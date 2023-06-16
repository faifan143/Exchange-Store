package com.faifan143.exchange.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.faifan143.exchange.activities.HomeActivity;
import com.faifan143.exchange.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;


public class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText emailEditText;
    private EditText numberEditText;
    private Button signupButton;
    private TextView loginTextButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        initViews();
        initListeners();
    }

    private void initViews() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        usernameEditText = findViewById(R.id.signupUsername);
        numberEditText = findViewById(R.id.signupNumber);
        passwordEditText = findViewById(R.id.signupPassword);
        confirmPasswordEditText = findViewById(R.id.signupConfirmPassword);
        emailEditText = findViewById(R.id.signupEmail);
        signupButton = findViewById(R.id.signupButton);
        loginTextButton = findViewById(R.id.loginTextButton);
        progressBar = findViewById(R.id.su_progressbar);
    }

    private void initListeners() {
        signupButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (validateFields() && passwordEditText.getText().toString().trim().equals(confirmPasswordEditText.getText().toString().trim())) {

                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();
                String number = numberEditText.getText().toString().trim();
                Map<String, Object> fields = new HashMap<>();
                fields.put("email", email);
                fields.put("password", password);
                fields.put("username", username);
                fields.put("number", username);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    user.sendEmailVerification().addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        firestore.collection("users").document(email).set(fields);
                                                        Toast.makeText(Signup.this, "تم ارسال رسالة تفعيل الحساب الى بريدك الالكتروني",
                                                                Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Signup.this, LoginActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(Signup.this, "فشل الانشاء",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    );
                                } else {
                                    Toast.makeText(Signup.this, "فشل الانشاء",
                                            Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });


            } else {
                progressBar.setVisibility(View.GONE);
            }
        });

        loginTextButton.setOnClickListener(v -> {
            Intent intent = new Intent(Signup.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean validateFields() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String number = numberEditText.getText().toString().trim();

        if (username.isEmpty()) {
            usernameEditText.setError("يجب ادخال اسم المستخدم");
            return false;
        }

        if (email.isEmpty()) {
            emailEditText.setError("يجب ادخال البريد الالكتروني");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("يجب ادخال بريد الكتروني صالح");
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("يجب ادخال كلمة المرور");
            return false;
        } else if (password.length() < 6) {
            passwordEditText.setError("يجب ان تكون كلمة المرور على الأقل 6 أحرف");
            return false;
        }
        if (number.isEmpty()) {
            numberEditText.setError("يجب ادخال كلمة المرور");
            return false;
        } else if (number.length() !=10) {
            numberEditText.setError("الرقم يجب ان يكون من 10 خانات");
            return false;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("يجب تأكيد كلمة المرور");
            return false;
        } else if (!confirmPassword.equals(password)) {
            confirmPasswordEditText.setError("كلمة المرور غير متطابقة");
            return false;
        }

        return true;
    }
}
