package com.faifan143.exchange.activities;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faifan143.exchange.R;
import com.faifan143.exchange.adapters.HomeAdapter;
import com.faifan143.exchange.adapters.MyHomeAdapter;
import com.faifan143.exchange.models.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView , myRecyclerView , productsRecyclerView;
    List<PostModel> dataList , myDataList , myProductsDataList;
    HomeAdapter adapter;
    MyHomeAdapter myHomeAdapter;
    SharedPreferences sharedPref;
    String myEmail , myNumber , myName ,myFCMToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_home_screen);
        getUserData();
        addNavBar();
        doAdapter();
        doMyAdapter();
    }


    private void getUserData() {
        sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        myEmail =  sharedPref.getString("myEmail", "");

        DocumentReference myDocRef = db.collection("users").document(myEmail);
        Task<DocumentSnapshot> task = myDocRef.get();
        task.addOnSuccessListener(myData -> {
            if (task.isSuccessful() && myData.exists()) {
                myEmail = myData.getString("email");
                myName = myData.getString("name");
                myNumber = myData.getString("number");
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                    return;
                                }
                                myFCMToken = task.getResult();
                                System.out.println("=================== fcmToken ==================");
                                System.out.println(task.getResult());
                                System.out.println("=================================================");
                                SharedPreferences.Editor myToken = editor.putString("myFCMToken",  myFCMToken);//
                                myDocRef.update("fcmToken",myFCMToken);
                                }
                        });



            } else {
                // Handle case where document does not exist
                System.out.println("Document does not exist!");
            }
        }).addOnFailureListener(e -> {
            // Handle exception
            System.err.println("Error getting document: " + e.getMessage());
        });
    }


    private void addNavBar(){
        bottomNavigationView = findViewById(R.id.home_bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navbar_home);
        bottomNavigationView.setOnItemSelectedListener(
                item -> {
                    switch (item.getItemId()){
                        case R.id.navbar_home: return true;
                        case R.id.navbar_post: startActivity(new Intent(getApplicationContext(),PostActivity.class));
                            return true;
                        case R.id.navbar_chat:  startActivity(new Intent(getApplicationContext(),ChatActivity.class));

                            return true;
                        case R.id.navbar_profile:  startActivity(new Intent(getApplicationContext(),ProfileActivity.class));

                            return true;
                    }
                    return false;
                }
        );
    }

    private void doAdapter() {

        recyclerView = findViewById(R.id.home_recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();
        adapter = new HomeAdapter(HomeActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        CollectionReference postsCollectionRef = db.collection("posts");

        final ProgressDialog dialog = ProgressDialog.show(HomeActivity.this, "",
                "جاري التحميل ...", true);

        Query query = postsCollectionRef.whereNotEqualTo("exchangerEmail",myEmail);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                dataList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    PostModel dataClass = documentSnapshot.toObject(PostModel.class);
                    dataClass.setKey(documentSnapshot.getId());
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(HomeActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void doMyAdapter() {
        myRecyclerView = findViewById(R.id.home_my_recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this, 1);
        myRecyclerView.setLayoutManager(gridLayoutManager);
        myDataList = new ArrayList<>();
        myHomeAdapter = new MyHomeAdapter(HomeActivity.this, myDataList);
        myRecyclerView.setAdapter(myHomeAdapter);
        CollectionReference postsCollectionRef = db.collection("posts");

        final ProgressDialog dialog = ProgressDialog.show(HomeActivity.this, "",
                "جاري التحميل ...", true);

        Query query = postsCollectionRef.whereEqualTo("exchangerEmail",myEmail);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                myDataList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    PostModel dataClass = documentSnapshot.toObject(PostModel.class);
                    dataClass.setKey(documentSnapshot.getId());
                    myDataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(HomeActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
