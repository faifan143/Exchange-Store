package com.faifan143.exchange.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.faifan143.exchange.R;
import com.faifan143.exchange.auth.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    TextView profileName , profileEmail , profileNumber ;
    Button profileLogout , profileChangePassword , profileRequests,profileDeals;
    ProgressBar spsProgressbar , spsPasswordProgressbar ;
    EditText profileOldPassword , profileNewPassword;
    String myEmail , myNumber , myName ;
    SharedPreferences sharedPref ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button approveButton , cancelButton ;
    ImageButton closeImageButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_profile_screen);
        getUserData();
        addNavBar();
        initViews();
        doViews();
    }
    private void addNavBar(){
        bottomNavigationView = findViewById(R.id.profile_bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navbar_profile);
        bottomNavigationView.setOnItemSelectedListener(
                item -> {
                    switch (item.getItemId()){
                        case R.id.navbar_profile: return true;
                        case R.id.navbar_post: startActivity(new Intent(getApplicationContext(),PostActivity.class));
                            return true;
                        case R.id.navbar_chat:  startActivity(new Intent(getApplicationContext(),ChatActivity.class));
                            return true;
                        case R.id.navbar_home:  startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            return true;
                    }
                    return false;
                }
        );
    }
    private void getUserData() {
        sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE);
        myEmail =  sharedPref.getString("myEmail", "");
        DocumentReference myDocRef = db.collection("users").document(myEmail);
        Task<DocumentSnapshot> task = myDocRef.get();
        task.addOnSuccessListener(myData -> {
            if (task.isSuccessful() && myData.exists()) {
                myEmail = myData.getString("email");
                myName = myData.getString("name");
                myNumber = myData.getString("number");
                System.out.println(myName);
                System.out.println(myEmail);
                System.out.println(myNumber);

                profileEmail.setText(myEmail);
                profileName.setText(myName);
                profileNumber.setText(myNumber);


            } else {
                // Handle case where document does not exist
                System.out.println("Document does not exist!");
            }
        }).addOnFailureListener(e -> {
            // Handle exception
            System.err.println("Error getting document: " + e.getMessage());
        });
    }

    private void initViews(){
    profileEmail = findViewById(R.id.profileEmail);
    profileName = findViewById(R.id.profileName);
    profileNumber = findViewById(R.id.profileNumber);
    profileLogout = findViewById(R.id.profileLogout);
    profileChangePassword = findViewById(R.id.profileChangePassword);
    profileOldPassword = findViewById(R.id.profileOldPassword);
    profileNewPassword = findViewById(R.id.profileNewPassword);
    spsProgressbar = findViewById(R.id.sps_progressbar);
    spsPasswordProgressbar = findViewById(R.id.sps_password_progressbar);
    profileRequests = findViewById(R.id.profileRequests);
    profileDeals = findViewById(R.id.profileDeals);
    }
    private void doViews(){
        profileLogout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    View alertCustomDialog = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.dialog_layout,null);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
                        alertDialog.setView(alertCustomDialog);
                        closeImageButton = (ImageButton) alertCustomDialog.findViewById(R.id.dialog_close_icon);
                        approveButton = (Button) alertCustomDialog.findViewById(R.id.dialog_approve_button);
                        cancelButton = (Button) alertCustomDialog.findViewById(R.id.dialog_close_button);
                        final AlertDialog dialog = alertDialog.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        closeImageButton.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                    }
                                }
                        );
                        cancelButton.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                    }
                                }
                        );
                        approveButton.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("is_logged_in", "false"); //
                                        editor.apply();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent i = new Intent(ProfileActivity.this , LoginActivity.class);
                                        startActivity(i);
                                    }
                                }
                        );
                    }
                }
        );

        profileChangePassword.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(validateFields()) {
                            String oldPassword = profileOldPassword.getText().toString().trim();
                            String newPassword = profileNewPassword.getText().toString().trim();
                            changeOldPassword(oldPassword, newPassword);
                        }
                    }
                }
        );


        profileRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,RequestsActivity.class);
                startActivity(intent);
            }
        });

        profileDeals.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProfileActivity.this,DealsActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    private void changeOldPassword(String oldPassword,String newPassword) {

        // Create a reference to the user's document in the 'users' collection
        DocumentReference docRef = db.collection("users").document(myEmail);

        // Retrieve the user's document from Firestore
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Get the user's document data
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the user's password from the document data
                        String password = document.getString("password");
                        if (password != null && password.equals(oldPassword)) {
                            // Password matches
                            // Do something here
                            docRef.update("password",newPassword);
                            Toast.makeText(ProfileActivity.this, "تم تغيير كلمة المرور",
                                    Toast.LENGTH_SHORT).show();
                            profileNewPassword.setText("");
                            profileOldPassword.setText("");
                        } else {
                            // Password doesn't match
                            // Do something here
                            Toast.makeText(ProfileActivity.this, "كلمة المرور القديمة غير صحيحة",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // User document doesn't exist
                        // Do something here
                    }
                } else {
                    // Error retrieving user document from Firestore
                    // Do something here
                    Toast.makeText(ProfileActivity.this, "فشل و يرجى اعادة المحاولة",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean validateFields() {
        String oldPassword = profileOldPassword.getText().toString().trim();
        String newPassword = profileNewPassword.getText().toString().trim();

        if (oldPassword.isEmpty()) {
            profileOldPassword.setError("يجب ادخال كلمة المرور");
            return false;
        } else if (oldPassword.length() < 6) {
            profileOldPassword.setError("يجب ان تكون كلمة المرور على الأقل 6 أحرف");
            return false;
        }
        if (newPassword.isEmpty()) {
            profileNewPassword.setError("يجب ادخال كلمة المرور");
            return false;
        } else if (newPassword.length() < 6) {
            profileNewPassword.setError("يجب ان تكون كلمة المرور على الأقل 6 أحرف");
            return false;
        }
        return true;
    }



}
