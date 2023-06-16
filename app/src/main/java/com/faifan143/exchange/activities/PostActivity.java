package com.faifan143.exchange.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faifan143.exchange.ImageUploader;
import com.faifan143.exchange.R;
import com.faifan143.exchange.adapters.PostAdapter;
import com.faifan143.exchange.models.PostModel;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostActivity extends AppCompatActivity {
    private Button galleryButton;
    private static final int GALLERY_REQUEST_CODE = 1;
    private Uri selectedImageUri;
    private String selectedImageUrl;
    private FirebaseFirestore firestore;
    RecyclerView recyclerView;
    List<PostModel> dataList;
    PostAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sharedPref;
    String myEmail , myNumber , myName , myFCMToken;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_post_screen);
        getUserData();
        addNavBar();
        spinnersBrain();
        pickingImage();
        handlePostButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();
            // Do something with the selected image URI
            ImageUploader imageUploader = new ImageUploader();

            imageUploader.uploadImage(selectedImageUri, new ImageUploader.OnImageUploadListener() {
                @Override
                public void onUploadSuccess(String downloadUrl) {
                    // Handle the success case, e.g. display the image in an ImageView
                    selectedImageUrl = downloadUrl;
                    Toast.makeText(PostActivity.this, selectedImageUrl, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUploadFailure(String errorMessage) {
                    // Handle the error case, e.g. display an error message
                    Toast.makeText(PostActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void addNavBar(){
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.post_bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navbar_post);
        bottomNavigationView.setOnItemSelectedListener(
                item -> {
                    switch (item.getItemId()){
                        case R.id.navbar_post: return true;
                        case R.id.navbar_home: startActivity(new Intent(getApplicationContext(),HomeActivity.class));

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

    private void spinnersBrain(){
        Spinner exchangeSpinner = findViewById(R.id.exchange_spinner);

        exchangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                // Do something with the selected option
                if (selectedOption.contains( "اختر فئة للبحث عن سلع")){
                }else{
                    doAdapter(selectedOption.trim());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        Spinner desiredProductsSpinner = findViewById(R.id.desired_product);
        desiredProductsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                // Do something with the selected option

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

    }
    private void pickingImage(){
        galleryButton = findViewById(R.id.gallery_button);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });

    }
    private void handlePostButton(){
        Button postButton = findViewById(R.id.post_button);
        EditText productDescriptionEditText = findViewById(R.id.product_description_edit_text);
        EditText productPriceEditText = findViewById(R.id.product_price_edit_text);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,String> data = new HashMap<>();
                // Check if product name EditText is empty
                Spinner productCategorySpinner = findViewById(R.id.product_category);
                Object selectedProductCategory = productCategorySpinner.getSelectedItem();
                if (selectedProductCategory.toString().contains( "اختر نوع المنتج")) {
                    Toast.makeText(PostActivity.this, "الرجاء اختيار نوع المنتج", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (productDescriptionEditText.getText().toString().isEmpty()) {
                    productDescriptionEditText.setError("الرجاء ادخال اسم السلعة");
                    productDescriptionEditText.requestFocus();
                    return;
                }
                if (productPriceEditText.getText().toString().isEmpty()) {
                    productPriceEditText.setError("الرجاء ادخال سعر السلعة التقريبي");
                    productPriceEditText.requestFocus();
                    return;
                }
                if (selectedImageUri == null) {
                    Toast.makeText(PostActivity.this, "الرجاء ادخال صورة", Toast.LENGTH_SHORT).show();
                    return;
                }
                Spinner exchangeSpinner = findViewById(R.id.desired_product);
                Object selectedExchange = exchangeSpinner.getSelectedItem();
                if (selectedExchange.toString() == "اختر فئة المقايضة المرغوبة") {
                    Toast.makeText(PostActivity.this, "الرجاء اختيار فئة مرغوبة", Toast.LENGTH_SHORT).show();
                    return;
                }
                while (selectedImageUrl == null) {
                    Toast.makeText(PostActivity.this, "جاري رفع صورة المنتج .. الرجاء الانتظار", Toast.LENGTH_LONG).show();
                    return;
                }
                DocumentReference docRef =  db.collection("posts").document();
                // Do something with the selected exchange option, image, and product name
                data.put("exchangerEmail",myEmail);
                data.put("exchangerName",myName);
                data.put("exchangerNumber",myNumber);
                data.put("productCategory",selectedProductCategory.toString());
                data.put("productDescription",productDescriptionEditText.getText().toString());
                data.put("productImage",selectedImageUrl);
                data.put("desiredExchangeCategory",selectedExchange.toString());
                data.put("productPrice",productPriceEditText.getText() + " s.p.");
                data.put("key",docRef.getId());
                docRef.set(data);

                Toast.makeText(PostActivity.this, "تم رفع منشورك بنجاح",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void doAdapter(String desiredExchange) {
        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(PostActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();
        adapter = new PostAdapter(PostActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        CollectionReference postsCollectionRef = db.collection("posts");

        final ProgressDialog dialog = ProgressDialog.show(PostActivity.this, "",
                "جاري التحميل ...", true);

        Query query = postsCollectionRef
                .whereNotEqualTo("exchangerEmail",myEmail )
                .whereEqualTo("productCategory", desiredExchange);

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
                Toast.makeText(PostActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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


