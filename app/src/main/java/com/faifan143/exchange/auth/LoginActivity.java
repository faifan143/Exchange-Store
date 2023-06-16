package com.faifan143.exchange.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.faifan143.exchange.R;
import com.faifan143.exchange.activities.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText numberEditText;
    private Button loginButton;
    private TextView signupTextView;
    private ProgressBar progressBar;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
    }

    private void initViews() {
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.loginEmail);
        passwordEditText = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        signupTextView = findViewById(R.id.signupTextButton);
        progressBar = findViewById(R.id.li_progressbar);
        checkBox = findViewById(R.id.li_checkbox);
        numberEditText = findViewById(R.id.loginNumber);

    }

    private void initListeners() {
        loginButton.setOnClickListener(v -> {
            if (validateFields()) {
            progressBar.setVisibility(View.VISIBLE);
                // proceed with sign-up process
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String number = numberEditText.getText().toString().trim();
                FirebaseUser user = mAuth.getCurrentUser();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this, "تم تسجيل الدخول بنجاح",
                                                Toast.LENGTH_SHORT).show();
                                        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        SharedPreferences.Editor myStatus =  editor.putBoolean("is_logged_in", true); // Set the boolean value to true
                                        SharedPreferences.Editor myEmail = editor.putString("myEmail", email);//
                                        SharedPreferences.Editor myNumber = editor.putString("myNumber", number);//
                                        editor.apply(); // Commit the changes

                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this, "فشل تسجيل الدخول",
                                                Toast.LENGTH_SHORT).show();
                                        System.out.println(task.getResult());
                                    }
                                }
                            });
                }

        });

        signupTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, Signup.class);
            startActivity(intent);
        });

        checkBox.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        } else {
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }
                    }
                }
        );

    }
    private boolean validateFields() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String number = numberEditText.getText().toString().trim();
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
            numberEditText.setError("يجب ادخال رقم الهاتف");
            return false;
        } else if (number.length() != 10) {
            numberEditText.setError("يجب ان يكون الرقم من 10 خانات");
            return false;
        }
        return true;
    }
}
