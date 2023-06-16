package com.faifan143.exchange.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.faifan143.exchange.R;


public class DealDetailedPostActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle , detailedProductPrice;
    ImageView detailImage;


    TextView myDetailDesc, myDetailTitle , myDetailedProductPrice;
    ImageView myDetailImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deal_detail);

        detailDesc = findViewById(R.id.dealProduct_detailedProductDescription);
        detailImage = findViewById(R.id.dealProduct_detailedProductImage);
        detailTitle = findViewById(R.id.dealProduct_detailedProductCategory);
        detailedProductPrice = findViewById(R.id.dealProduct_detailedProductPrice);

        myDetailDesc = findViewById(R.id.myProduct_detailedProductDescription);
        myDetailImage = findViewById(R.id.myProduct_detailedProductImage);
        myDetailTitle = findViewById(R.id.myProduct_detailedProductCategory);
        myDetailedProductPrice = findViewById(R.id.myProduct_detailedProductPrice);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
           myDetailDesc.setText(bundle.getString("proposedProductDescription"));
            myDetailTitle.setText(bundle.getString("proposedProductCategory"));
            myDetailedProductPrice.setText(bundle.getString("proposedProductPrice"));
            Glide.with(this).load(bundle.getString("proposedProductImage")).into(myDetailImage);

            detailDesc.setText(bundle.getString("productDescription"));
            detailTitle.setText(bundle.getString("productCategory"));
            detailedProductPrice.setText(bundle.getString("productPrice"));
            Glide.with(this).load(bundle.getString("productImage")).into(detailImage);
        }

    }
}
