package com.nealgosalia.shoppy.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.nealgosalia.shoppy.R;
import com.nealgosalia.shoppy.Util.Category;
import com.nealgosalia.shoppy.Util.FirebaseImageLoader;

import java.util.List;

/**
 * Created by kira on 16/2/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Category> itemsList;
    Context context;
    private FirebaseStorage mStorage;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView categoryImage;
        public TextView categoryName;

        public MyViewHolder(View view) {
            super(view);
            categoryImage = (ImageView) view.findViewById(R.id.categoryImage);
            categoryName = (TextView) view.findViewById(R.id.categoryName);
        }
    }

    public CategoryAdapter(List<Category> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
        mStorage= FirebaseStorage.getInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_categories, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Category category = itemsList.get(position);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(mStorage.getReference("Categories/"+category.getCategoryName()+".jpg"))
                .into(holder.categoryImage);
        holder.categoryName.setText(category.getCategoryName().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

}
