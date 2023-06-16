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
import com.faifan143.exchange.activities.DealDetailedPostActivity;
import com.faifan143.exchange.models.RequestModel;

import java.util.List;
public class DealsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Context context;
    private List<RequestModel> dataList;
    public DealsAdapter(Context context, List<RequestModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_EMPTY) {
            View emptyView = inflater.inflate(R.layout.empty_layout, parent, false);
            return new EmptyDealsViewHolder(emptyView);
        } else {
            View itemView = inflater.inflate(R.layout.deals_card, parent, false);
            return new MyDealsViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            MyDealsViewHolder itemViewHolder = (MyDealsViewHolder) holder;
            RequestModel requestModel = dataList.get(position);
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.uploadimg)
                    .error(R.drawable.uploadimg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter();

            Glide.with(context)
                    .load(requestModel.getProposedImage())
                    .apply(requestOptions)
                    .into(itemViewHolder.recImage);

            itemViewHolder.recTitle.setText(requestModel.getProposedCategory());
            itemViewHolder.recDesiredCategory.setText(requestModel.getProposedDesiredExchangeCategory());
            itemViewHolder.recDesc.setText(requestModel.getProposedDescription());


            itemViewHolder.recImageButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, DealDetailedPostActivity.class);
                            intent.putExtra("proposedProductDescription",requestModel.getProposedDescription());
                            intent.putExtra("proposedProductCategory",requestModel.getProposedCategory());
                            intent.putExtra("proposedProductImage",requestModel.getProposedImage());
                            intent.putExtra("proposedProductPrice",requestModel.getProposedPrice());

                            intent.putExtra("productDescription",requestModel.getProductDescription());
                            intent.putExtra("productCategory",requestModel.getProductCategory());
                            intent.putExtra("productImage",requestModel.getProductImage());
                            intent.putExtra("productPrice",requestModel.getProductPrice());
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

class MyDealsViewHolder extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView recTitle, recDesc , recDesiredCategory ;
    CardView recCard;
    ImageButton recImageButton;

    public MyDealsViewHolder(@NonNull View itemView) {
        super(itemView);
        recImageButton = itemView.findViewById(R.id.deals_imageButton);
        recImage = itemView.findViewById(R.id.deals_recImage);
        recCard = itemView.findViewById(R.id.deals_recCard);
        recDesc = itemView.findViewById(R.id.deals_recDesc);
        recTitle = itemView.findViewById(R.id.deals_recTitle);
        recDesiredCategory = itemView.findViewById(R.id.deals_recDesiredCategory);
    }
}

class EmptyDealsViewHolder extends RecyclerView.ViewHolder {

    TextView emptyText;

    public EmptyDealsViewHolder(@NonNull View itemView) {
        super(itemView);
        emptyText = itemView.findViewById(R.id.emptyId);
    }

    public void bind() {
        emptyText.setText("فارغ");
    }
}

