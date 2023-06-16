package com.faifan143.exchange.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faifan143.exchange.R;
import com.faifan143.exchange.adapters.RequestAdapter;
import com.faifan143.exchange.models.RequestModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RequestsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<RequestModel> dataList;
    RequestAdapter requestAdapter;
    SharedPreferences sharedPref;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String myEmail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests_layout);
        sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE);
        myEmail = sharedPref.getString("myEmail", "");
        doRequestsAdapter();
    }

    private void doRequestsAdapter() {
        recyclerView = findViewById(R.id.request_recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(RequestsActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();
        requestAdapter = new RequestAdapter(RequestsActivity.this, dataList);
        recyclerView.setAdapter(requestAdapter);
        CollectionReference myEmailCollectionRef = db.collection("users").document(myEmail).collection("requests");
        final ProgressDialog dialog = ProgressDialog.show(RequestsActivity.this, "",
                "جاري التحميل ...", true);
        Query query = myEmailCollectionRef.whereNotEqualTo("sender",myEmail);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                dataList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    RequestModel dataClass = documentSnapshot.toObject(RequestModel.class);
                    dataClass.setProductKey(documentSnapshot.getId());
                    dataList.add(dataClass);
                }
                requestAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(RequestsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
