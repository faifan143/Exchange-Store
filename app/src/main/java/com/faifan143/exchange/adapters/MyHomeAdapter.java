package com.faifan143.exchange.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.faifan143.exchange.R;
import com.faifan143.exchange.activities.DetailedPostActivity;
import com.faifan143.exchange.models.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
public class MyHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Context context;
    private List<PostModel> myDataList;

    public MyHomeAdapter(Context context, List<PostModel> myDataList) {
        this.context = context;
        this.myDataList = myDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_EMPTY) {
            View emptyView = inflater.inflate(R.layout.empty_layout, parent, false);
            return new MyEmptyViewHolder(emptyView);
        } else {
            View itemView = inflater.inflate(R.layout.my_post_card, parent, false);
            return new MyHomePostsViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            MyHomePostsViewHolder itemViewHolder = (MyHomePostsViewHolder) holder;
            PostModel post = myDataList.get(position);
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.uploadimg)
                    .error(R.drawable.uploadimg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter();

            Glide.with(context)
                    .load(post.getProductImage())
                    .apply(requestOptions)
                    .into(itemViewHolder.recImage);

            itemViewHolder.recTitle.setText(post.getProductCategory());
            itemViewHolder.recDesiredCategory.setText(post.getDesiredExchangeCategory());

            itemViewHolder.recDesc.setText(post.getProductDescription());
            itemViewHolder.recImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton closeImageButton;
                    Button approveButton , cancelButton;

                    View alertCustomDialog = LayoutInflater.from(context).inflate(R.layout.delete_dialog_layout,null);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setView(alertCustomDialog);
                    closeImageButton = (ImageButton) alertCustomDialog.findViewById(R.id.delete_dialog_close_icon);
                    approveButton = (Button) alertCustomDialog.findViewById(R.id.delete_dialog_approve_button);
                    cancelButton = (Button) alertCustomDialog.findViewById(R.id.delete_dialog_close_button);
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

                            FirebaseFirestore.getInstance().collection("posts").document(post.getKey()).delete().addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                int removedPosition = itemViewHolder.getAdapterPosition();
                                                myDataList.remove(removedPosition);
                                                notifyItemRemoved(removedPosition);
                                            }
                                        }
                                    }
                            );
                            dialog.cancel();
                        }
                    });
                }
            });


            itemViewHolder.recCard.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, DetailedPostActivity.class);
                            String productCategory = post.getProductCategory();
                            String productDescription = post.getProductDescription();
                            String productImage = post.getProductImage();
                            String productDesiredExchange = post.getDesiredExchangeCategory();
                            String productPrice = post.getProductPrice();
                            intent.putExtra("productCategory", productCategory);
                            intent.putExtra("productDescription", productDescription);
                            intent.putExtra("productImage", productImage);
                            intent.putExtra("productDesiredExchange", productDesiredExchange);
                            intent.putExtra("productPrice", productPrice);
                            intent.putExtra("exchangerEmail" , post.getExchangerEmail());
                            intent.putExtra("productKey" , post.getKey());
                            context.startActivity(intent);
                        }
                    }
            );
        }
    }

    @Override
    public int getItemCount() {
        if (myDataList.isEmpty()) {
            return 1; // Return 1 for the empty data list view
        } else {
            return myDataList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (myDataList.isEmpty()) {
            return VIEW_TYPE_EMPTY; // Return the view type for the empty data list view
        } else {
            return VIEW_TYPE_ITEM; // Return the view type for the normal item view
        }
    }

}

class MyHomePostsViewHolder extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView recTitle, recDesc , recDesiredCategory;
    CardView recCard;
    ImageButton recImageButton;

    public MyHomePostsViewHolder(@NonNull View itemView) {
        super(itemView);
        recImageButton = itemView.findViewById(R.id.deleteButton);
        recImage = itemView.findViewById(R.id.myRecImage);
        recCard = itemView.findViewById(R.id.myRecCard);
        recDesc = itemView.findViewById(R.id.myRecDesc);
        recTitle = itemView.findViewById(R.id.myRecTitle);
        recDesiredCategory = itemView.findViewById(R.id.myRecDesiredCategory);

    }
}

class MyEmptyViewHolder extends RecyclerView.ViewHolder {

    TextView emptyText;

    public MyEmptyViewHolder(@NonNull View itemView) {
        super(itemView);
        emptyText = itemView.findViewById(R.id.emptyId);
    }

    public void bind() {
        emptyText.setText("فارغ");
    }
}

