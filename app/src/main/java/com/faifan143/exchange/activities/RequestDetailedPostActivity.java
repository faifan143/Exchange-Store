package com.faifan143.exchange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.faifan143.exchange.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RequestDetailedPostActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailDesiredExchange , detailedProductPrice;
    ImageView detailImage;
    Button approve , decline;
    String productKey, myEmail , senderEmail;
    Map<String , Object> data = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_detail);

        detailDesc = findViewById(R.id.request_detailedProductDescription);
        detailImage = findViewById(R.id.request_detailedProductImage);
        detailTitle = findViewById(R.id.request_detailedProductCategory);
        detailDesiredExchange = findViewById(R.id.request_detailedProductDesiredExchange);
        detailedProductPrice  = findViewById(R.id.request_detailedProductPrice);
        approve = findViewById(R.id.request_approve_button);
        decline = findViewById(R.id.request_close_button);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailDesc.setText(bundle.getString("proposedProductDescription"));
            detailTitle.setText(bundle.getString("proposedProductCategory"));
            detailDesiredExchange.setText(bundle.getString("proposedProductDesiredExchange"));
            detailedProductPrice.setText(bundle.getString("proposedProductPrice"));
            productKey = bundle.getString("productKey");
            myEmail = bundle.getString("receiverEmail");
            senderEmail = bundle.getString("senderEmail");
            Glide.with(this).load(bundle.getString("proposedProductImage")).into(detailImage);

        }

        decline.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseFirestore.getInstance().collection("users").document(myEmail).collection("requests").document(productKey).delete().addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(RequestDetailedPostActivity.this ,RequestsActivity.class);
                                        startActivity(intent);
                                    }
                                }
                        );
                    }
                }
        );
        approve.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.put("productDescription", bundle.getString("productDescription"));
                        data.put("productCategory", bundle.getString("productCategory"));
                        data.put("productDesiredExchange", bundle.getString("productDesiredExchange"));
                        data.put("productPrice", bundle.getString("productPrice"));
                        data.put("productImage", bundle.getString("productImage"));
                        data.put("productKey", bundle.getString("productKey"));
                        data.put("proposedDescription", bundle.getString("proposedProductDescription"));
                        data.put("proposedCategory", bundle.getString("proposedProductCategory"));
                        data.put("proposedDesiredExchange", bundle.getString("proposedProductDesiredExchange"));
                        data.put("proposedPrice", bundle.getString("proposedProductPrice"));
                        data.put("proposedImage", bundle.getString("proposedProductImage"));
                        data.put("proposedProductKey", bundle.getString("proposedProductKey"));
                        List<String> dealers = new ArrayList<>();
                        dealers.add(senderEmail);
                        dealers.add(myEmail);
                        data.put("dealers", dealers);


                    FirebaseFirestore.getInstance().collection("users").document(myEmail).collection("deals").document(productKey+"-"+senderEmail).set(data).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseFirestore.getInstance().collection("users").document(myEmail).collection("requests").document(productKey).delete();
                                    FirebaseFirestore.getInstance().collection("users").document(senderEmail).collection("deals").document(bundle.getString("proposedProductKey")+"-"+myEmail).set(data).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(RequestDetailedPostActivity.this, "م القبول بنجاح", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RequestDetailedPostActivity.this ,ProfileActivity.class);
                                    startActivity(intent);
                                }
                            }
                                );
                                }
                            }
                    );
                    }
                }
        );
    }
}
