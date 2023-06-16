package com.faifan143.exchange.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faifan143.exchange.R;
import com.faifan143.exchange.adapters.ChatScreenAdapter;
import com.faifan143.exchange.models.ContactModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    List<ContactModel> dataList;
    ChatScreenAdapter adapter;
    String myEmail , myNumber , myName , myFCMToken;
    SharedPreferences sharedPref;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_chat_screen);
        getUserData();
        addNavBar();
        doAdapter();
    }
    private void addNavBar(){
        bottomNavigationView = findViewById(R.id.chat_bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navbar_chat);
        bottomNavigationView.setOnItemSelectedListener(
                item -> {
                    switch (item.getItemId()){
                        case R.id.navbar_chat: return true;
                        case R.id.navbar_post: startActivity(new Intent(getApplicationContext(),PostActivity.class));
                            return true;
                        case R.id.navbar_home:  startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            return true;
                        case R.id.navbar_profile:  startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                            return true;
                    }
                    return false;
                }
        );
    }
//    private void doAdapter() {
//        recyclerView = findViewById(R.id.chat_recyclerView);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(ChatActivity.this, 1);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        dataList = new ArrayList<>();
//        adapter = new ChatScreenAdapter(ChatActivity.this, dataList);
//        recyclerView.setAdapter(adapter);
//        CollectionReference myEmailCollectionRef = db.collection("users").document(myEmail).collection("chats");
//        final ProgressDialog dialog = ProgressDialog.show(ChatActivity.this, "",
//                "جاري التحميل ...", true);
//        Query query = myEmailCollectionRef.whereNotEqualTo("email",myEmail);
//        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                dataList.clear();
//                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                    ContactModel dataClass = documentSnapshot.toObject(ContactModel.class);
//
//                    dataClass.setKey(documentSnapshot.getId());
//                    dataList.add(dataClass);
//                }
//                adapter.notifyDataSetChanged();
//                dialog.dismiss();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                dialog.dismiss();
//                Toast.makeText(ChatActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
private void doAdapter() {
    recyclerView = findViewById(R.id.chat_recyclerView);
    GridLayoutManager gridLayoutManager = new GridLayoutManager(ChatActivity.this, 1);
    recyclerView.setLayoutManager(gridLayoutManager);
    dataList = new ArrayList<>();
    adapter = new ChatScreenAdapter(ChatActivity.this, dataList);
    recyclerView.setAdapter(adapter);

    CollectionReference myEmailCollectionRef = db.collection("users").document(myEmail).collection("chats");
    final ProgressDialog dialog = ProgressDialog.show(ChatActivity.this, "", "جاري التحميل ...", true);

    myEmailCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            dataList.clear();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String otherUserEmail = documentSnapshot.getId();
                ContactModel dataClass = documentSnapshot.toObject(ContactModel.class);
                dataClass.setEmail(otherUserEmail);
                dataList.add(dataClass);
            }
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            dialog.dismiss();
            Toast.makeText(ChatActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}

    private void getUserData() {
         sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE);
        myEmail = sharedPref.getString("myEmail", "");
        myFCMToken = sharedPref.getString("myFCMToken", "");
        DocumentReference myDocRef = db.collection("users").document(myEmail);
        Task<DocumentSnapshot> task = myDocRef.get();
        task.addOnSuccessListener(myData -> {
            if (myData.exists()) {
                myEmail = myData.getString("email");
                myName = myData.getString("name");
                myNumber = myData.getString("number");
            } else {
                // Handle case where document does not exist
                System.out.println("Document does not exist!");
            }
        }).addOnFailureListener(e -> {
            // Handle exception
            System.err.println("Error getting document: " + e.getMessage());
        });
    }

}
