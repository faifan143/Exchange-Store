package com.faifan143.exchange.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.faifan143.exchange.R;
import com.faifan143.exchange.adapters.ProductsAdapter;
import com.faifan143.exchange.models.PostModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DetailedPostActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailDesiredExchange , detailedProductPrice;
    ImageView detailImage;
     RecyclerView productsRecyclerView;
     ArrayList<PostModel> myProductsDataList;
     ProductsAdapter productsAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String myEmail , receiver , productKey , productImage , productCategory ,productDescription , productDesiredExchangeCategory , productPrice ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        myEmail =  getSharedPreferences("my_preferences", MODE_PRIVATE).getString("myEmail", "");
        detailDesc = findViewById(R.id.detailedProductDescription);
        detailImage = findViewById(R.id.detailedProductImage);
        detailTitle = findViewById(R.id.detailedProductCategory);
        detailDesiredExchange = findViewById(R.id.detailedProductDesiredExchange);
        detailedProductPrice  = findViewById(R.id.detailedProductPrice);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailDesc.setText(bundle.getString("productDescription"));
            detailTitle.setText(bundle.getString("productCategory"));
            detailDesiredExchange.setText(bundle.getString("productDesiredExchange"));
            detailedProductPrice.setText(bundle.getString("productPrice"));
            productCategory = bundle.getString("productCategory");
            productDescription = bundle.getString("productDescription");
            productDesiredExchangeCategory= bundle.getString("productDesiredExchange");
            productImage = bundle.getString("productImage");
            receiver = bundle.getString("exchangerEmail");
            productKey = bundle.getString("productKey");
            productPrice = bundle.getString("productPrice");
            Glide.with(this).load(bundle.getString("productImage")).into(detailImage);
        }
        System.out.println("============== details ================");
        System.out.println(productKey);
        System.out.println(receiver);
        System.out.println(productImage);
        System.out.println(productDescription);
        doMyProductsAdapter();
    }

    private void doMyProductsAdapter() {
        productsRecyclerView = findViewById(R.id.products_recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(DetailedPostActivity.this, 1);
        productsRecyclerView.setLayoutManager(gridLayoutManager);
        myProductsDataList = new ArrayList<>();
        productsAdapter = new ProductsAdapter(DetailedPostActivity.this, myProductsDataList,myEmail,receiver,productKey,productImage,productCategory,productDescription,productPrice , productDesiredExchangeCategory);
        productsRecyclerView.setAdapter(productsAdapter);
        CollectionReference postsCollectionRef = db.collection("posts");

        final ProgressDialog dialog = ProgressDialog.show(DetailedPostActivity.this, "",
                "جاري التحميل ...", true);

        String detailDesiredEx = detailDesiredExchange.getText().toString();
        List<String> categories = Arrays.asList(detailDesiredEx, "غير ذلك");

        Query query;
        if(receiver!=null && receiver.equals(myEmail)) {
             query = postsCollectionRef.whereNotEqualTo("exchangerEmail", myEmail)
                    .whereIn("productCategory",categories )
                    .orderBy("exchangerEmail")
                    .orderBy("productPrice", Query.Direction.ASCENDING);
        }else{
            query = postsCollectionRef.whereEqualTo("exchangerEmail", myEmail)
                    .whereIn("productCategory", categories)
                    .orderBy("productPrice", Query.Direction.ASCENDING);
        }


        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                myProductsDataList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    PostModel dataClass = documentSnapshot.toObject(PostModel.class);
                    dataClass.setKey(documentSnapshot.getId());
                    myProductsDataList.add(dataClass);
                }
                productsAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(DetailedPostActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(e.getMessage());
            }
        });
    }

}
