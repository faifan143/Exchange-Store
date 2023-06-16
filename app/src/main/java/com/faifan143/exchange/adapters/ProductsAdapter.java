package com.faifan143.exchange.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.faifan143.exchange.R;
import com.faifan143.exchange.models.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Context context;
    private List<PostModel> productsDataList;
    private String myEmail , receiver , productKey , productImage , productCategory ,productDescription ,productPrice , productDesiredExchangeCategory ;

    public ProductsAdapter(Context context, List<PostModel> productsDataList, String myEmail, String receiver, String productKey, String productImage, String productCategory, String productDescription , String productPrice ,String productDesiredExchangeCategory) {
        this.context = context;
        this.productsDataList = productsDataList;
        this.myEmail = myEmail;
        this.receiver = receiver;
        this.productKey = productKey;
        this.productImage = productImage;
        this.productCategory = productCategory;
        this.productDescription = productDescription;
        this.productPrice=productPrice;
        this.productDesiredExchangeCategory=productDesiredExchangeCategory;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_EMPTY) {
            View emptyView = inflater.inflate(R.layout.empty_layout, parent, false);
            return new MyProductsEmptyViewHolder(emptyView);
        } else {
            View itemView = inflater.inflate(R.layout.product_card, parent, false);
            return new ProductsPostsViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            ProductsPostsViewHolder itemViewHolder = (ProductsPostsViewHolder) holder;
            PostModel post = productsDataList.get(position);
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.uploadimg)
                    .error(R.drawable.uploadimg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter();

            Glide.with(context)
                    .load(post.getProductImage())
                    .apply(requestOptions)
                    .into(itemViewHolder.recImage);

            itemViewHolder.recName.setText(post.getProductCategory());
            itemViewHolder.recPrice.setText(post.getProductPrice());

            itemViewHolder.recDesc.setText(post.getProductDescription());
            itemViewHolder.recImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ImageButton closeImageButton;
                    Button approveButton , cancelButton;

                    View alertCustomDialog = LayoutInflater.from(context).inflate(R.layout.exchange_dialog_layout,null);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setView(alertCustomDialog);
                    closeImageButton = (ImageButton) alertCustomDialog.findViewById(R.id.exchange_dialog_close_icon);
                    approveButton = (Button) alertCustomDialog.findViewById(R.id.exchange_dialog_approve_button);
                    cancelButton = (Button) alertCustomDialog.findViewById(R.id.exchange_dialog_close_button);
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

                    approveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, Object> requestMapData = new HashMap<>();
                            CollectionReference collRef = FirebaseFirestore.getInstance().collection("users");
                            DocumentReference docRef;
                            if(!myEmail.equals(receiver)) {
                                requestMapData.put("sender", myEmail);
                                requestMapData.put("receiver", receiver);
                                requestMapData.put("productKey", productKey);
                                requestMapData.put("productImage", productImage);
                                requestMapData.put("productCategory", productCategory);
                                requestMapData.put("productDescription", productDescription);
                                requestMapData.put("productPrice",productPrice);
                                requestMapData.put("productDesiredExchangeCategory",productDesiredExchangeCategory);
                                requestMapData.put("proposedImage", post.getProductImage());
                                requestMapData.put("proposedCategory", post.getProductCategory());
                                requestMapData.put("proposedDescription", post.getProductDescription());
                                requestMapData.put("proposedPrice",post.getProductPrice());
                                requestMapData.put("proposedDesiredExchangeCategory",post.getDesiredExchangeCategory());
                                requestMapData.put("proposedProductKey", post.getKey());

                                docRef = collRef.document(receiver).collection("requests").document(productKey);
                            } else{
                                requestMapData.put("sender", myEmail);
                                requestMapData.put("receiver", receiver);
                                requestMapData.put("productKey", post.getKey());
                                requestMapData.put("productImage", post.getProductImage());
                                requestMapData.put("productCategory", post.getProductCategory());
                                requestMapData.put("productDescription", post.getProductDescription());
                                requestMapData.put("productPrice",post.getProductPrice());
                                requestMapData.put("productDesiredExchangeCategory",post.getDesiredExchangeCategory());

                                requestMapData.put("proposedImage", productImage);
                                requestMapData.put("proposedCategory", productCategory);
                                requestMapData.put("proposedDescription", productDescription);
                                requestMapData.put("proposedPrice",productPrice);
                                requestMapData.put("proposedDesiredExchangeCategory",productDesiredExchangeCategory);
                                requestMapData.put("proposedProductKey", productKey);

                                docRef = collRef.document(post.getExchangerEmail()).collection("requests").document(post.getKey());
                            }

                            docRef.get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    // Document already exists, do nothing
                                                    Toast.makeText(context, "الطلب موجود بالفعل", Toast.LENGTH_SHORT).show();
                                                    dialog.cancel();

                                                } else {
                                                    // Document doesn't exist, create it
                                                    docRef.set(requestMapData)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    dialog.cancel();
                                                                    Toast.makeText(context, "تم ارسال الطلب بنجاح", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });
                        }
                    });

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        if (productsDataList.isEmpty()) {
            return 1; // Return 1 for the empty data list view
        } else {
            return productsDataList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (productsDataList.isEmpty()) {
            return VIEW_TYPE_EMPTY; // Return the view type for the empty data list view
        } else {
            return VIEW_TYPE_ITEM; // Return the view type for the normal item view
        }
    }

}

class ProductsPostsViewHolder extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView recName, recDesc , recPrice;
    CardView recCard;
    ImageButton recImageButton;

    public ProductsPostsViewHolder(@NonNull View itemView) {
        super(itemView);
        recImageButton = itemView.findViewById(R.id.product_imageButton);
        recImage = itemView.findViewById(R.id.product_recImage);
        recCard = itemView.findViewById(R.id.product_recCard);
        recDesc = itemView.findViewById(R.id.product_recDesc);
        recName = itemView.findViewById(R.id.product_recName);
        recPrice = itemView.findViewById(R.id.product_recPrice);

    }
}

class MyProductsEmptyViewHolder extends RecyclerView.ViewHolder {

    TextView emptyText;

    public MyProductsEmptyViewHolder(@NonNull View itemView) {
        super(itemView);
        emptyText = itemView.findViewById(R.id.emptyId);
    }

    public void bind() {
        emptyText.setText("فارغ");
    }
}

