package com.nealgosalia.shoppy.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.nealgosalia.shoppy.Adapters.CategoryAdapter;
import com.nealgosalia.shoppy.Adapters.ImageAdapter;
import com.nealgosalia.shoppy.MainActivity;
import com.nealgosalia.shoppy.R;
import com.nealgosalia.shoppy.Util.Category;
import com.nealgosalia.shoppy.Util.GridItem;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseStorage mStorage;
    private ImageAdapter mItemsAdapter;
    private List<GridItem> gridItemsList = new ArrayList<>();
    private TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        Intent intent = getIntent();
        GridView gridView = (GridView) findViewById(R.id.itemsGridView);

        final String categoryName = intent.getStringExtra("CATEGORY");
        String titleName = categoryName.substring(0, 1).toUpperCase() + categoryName.substring(1);
        titleText = (TextView) findViewById(R.id.toolbar_title);
        titleText.setText(titleName);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setTint(Color.parseColor("#FFFFFF"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mStorage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("items").child(categoryName);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                generateItemsList(dataSnapshot);
                mItemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                generateItemsList(dataSnapshot);
                mItemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                generateItemsList(dataSnapshot);
                mItemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mItemsAdapter = new ImageAdapter(this, gridItemsList);
        gridView.setAdapter(mItemsAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                GridItem clickedItem = gridItemsList.get(pos);
                String clickedItemId = clickedItem.getItemId();
                Intent i =new Intent(ItemsActivity.this, ItemDetailsActivity.class);
                i.putExtra("CATEGORY", categoryName);
                i.putExtra("ID", clickedItemId);
                startActivity(i);
            }
        });
    }

    public void generateItemsList(DataSnapshot dataSnapshot){
        gridItemsList.clear();
        String itemId = dataSnapshot.child("product_id").getValue().toString();
        String itemName = dataSnapshot.child("name").getValue().toString();
        String itemPrice = dataSnapshot.child("price").getValue().toString();
        GridItem gridItem = new GridItem();
        gridItem.setItemId(itemId);
        gridItem.setItemName(itemName);
        gridItem.setItemPrice(itemPrice);
        gridItemsList.add(gridItem);
    }
}
