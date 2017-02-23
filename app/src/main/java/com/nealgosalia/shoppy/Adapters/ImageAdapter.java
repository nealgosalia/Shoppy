package com.nealgosalia.shoppy.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.nealgosalia.shoppy.R;
import com.nealgosalia.shoppy.Util.FirebaseImageLoader;
import com.nealgosalia.shoppy.Util.GridItem;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ImageAdapter extends BaseAdapter {

    private final List<GridItem> gridItems;
    private Context context;
    private FirebaseStorage mStorage;

    public ImageAdapter(Context context, List<GridItem> gridItems) {
        this.context = context;
        this.gridItems = gridItems;
        mStorage= FirebaseStorage.getInstance();

    }

    @Override
    public int getCount() {
        return gridItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            GridItem gridItem = gridItems.get(position);
            Log.d("ImageAdapter", "Entered");
            gridView = inflater.inflate(R.layout.custom_gridview, null);
            TextView textView = (TextView) gridView.findViewById(R.id.productname);
            textView.setText(gridItem.getItemName());
            String formattedString = null;
            try {
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,##,##,##,###");
                formattedString = "₹ " + formatter.format(Double.parseDouble(gridItem.getItemPrice()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            TextView textView1 = (TextView) gridView.findViewById(R.id.price);
            textView1.setText(formattedString);
            ImageView imageView = (ImageView) gridView.findViewById(R.id.leo);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(mStorage.getReference("Items/"+gridItem.getItemName()+".jpg"))
                    .into(imageView);

            return gridView;
        }
        return convertView;

    }


}