package com.faifan143.exchange.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.faifan143.exchange.R;
import com.faifan143.exchange.activities.DetailedPostActivity;
import com.faifan143.exchange.activities.InChatScreen;
import com.faifan143.exchange.models.PostModel;

import java.util.List;
public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Context context;
    private List<PostModel> dataList;

    public PostAdapter(Context context, List<PostModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_EMPTY) {
            View emptyView = inflater.inflate(R.layout.empty_layout, parent, false);
            return new EmptyViewHolder(emptyView);
        } else {
            View itemView = inflater.inflate(R.layout.post_card, parent, false);
            return new MyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            MyViewHolder itemViewHolder = (MyViewHolder) holder;
            PostModel post = dataList.get(position);
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
            itemViewHolder.recDesc.setText(post.getProductDescription());
            itemViewHolder.recDesiredCategory.setText(post.getDesiredExchangeCategory());
            itemViewHolder.recImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, InChatScreen.class);
                    String number = dataList.get(holder.getAdapterPosition()).getExchangerNumber();
                    String name = dataList.get(holder.getAdapterPosition()).getExchangerName();
                    String email = dataList.get(holder.getAdapterPosition()).getExchangerEmail();
                    String fcmToken = dataList.get(holder.getAdapterPosition()).getExchangerToken();
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("number", number);
                    intent.putExtra("fcmToken", fcmToken);
                    context.startActivity(intent);
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
                            intent.putExtra("productCategory", productCategory);
                            intent.putExtra("productDescription", productDescription);
                            intent.putExtra("productImage", productImage);
                            intent.putExtra("productDesiredExchange", productDesiredExchange);
                            context.startActivity(intent);
                        }
                    }
            );
        }
    }

    @Override
    public int getItemCount() {
        if (dataList.isEmpty()) {
            return 1; // Return 1 for the empty data list view
        } else {
            return dataList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.isEmpty()) {
            return VIEW_TYPE_EMPTY; // Return the view type for the empty data list view
        } else {
            return VIEW_TYPE_ITEM; // Return the view type for the normal item view
        }
    }

}

class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView recTitle, recDesc , recDesiredCategory;
    CardView recCard;
    ImageButton recImageButton;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recImageButton = itemView.findViewById(R.id.imageButton);
        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recDesc = itemView.findViewById(R.id.recDesc);
        recTitle = itemView.findViewById(R.id.recTitle);
        recDesiredCategory = itemView.findViewById(R.id.recDesiredCategory);
    }
}

class EmptyPostViewHolder extends RecyclerView.ViewHolder {

    TextView emptyText;

    public EmptyPostViewHolder(@NonNull View itemView) {
        super(itemView);
        emptyText = itemView.findViewById(R.id.emptyId);
    }

    public void bind() {
        emptyText.setText("فارغ");
    }
}

