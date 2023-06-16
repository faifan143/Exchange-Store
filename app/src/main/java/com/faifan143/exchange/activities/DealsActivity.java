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
import com.faifan143.exchange.adapters.DealsAdapter;
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

public class DealsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<RequestModel> dataList;
    DealsAdapter dealsAdapter;
    SharedPreferences sharedPref;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String myEmail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deals_layout);
        sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE);
        myEmail = sharedPref.getString("myEmail", "");
        doDealsAdapter();



    }

    private void doDealsAdapter() {
        recyclerView = findViewById(R.id.deals_recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(DealsActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();
        dealsAdapter = new DealsAdapter(DealsActivity.this, dataList);
        recyclerView.setAdapter(dealsAdapter);
        CollectionReference myEmailCollectionRef = db.collection("users").document(myEmail).collection("deals");
        final ProgressDialog dialog = ProgressDialog.show(DealsActivity.this, "",
                "جاري التحميل ...", true);
        Query query = myEmailCollectionRef.whereArrayContains("dealers",myEmail);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                dataList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    RequestModel dataClass = documentSnapshot.toObject(RequestModel.class);
                    System.out.println("===========datum===========");
                    System.out.println(documentSnapshot.getData());
                    System.out.println("==========================");
                    dataClass.setProductKey(documentSnapshot.getId());
                    dataList.add(dataClass);
                }
                dealsAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(DealsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
