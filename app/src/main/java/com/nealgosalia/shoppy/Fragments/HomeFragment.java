package com.nealgosalia.shoppy.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.nealgosalia.shoppy.Activities.ItemsActivity;
import com.nealgosalia.shoppy.Adapters.CategoryAdapter;
import com.nealgosalia.shoppy.R;
import com.nealgosalia.shoppy.Util.Category;
import com.nealgosalia.shoppy.Util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private DatabaseReference mDatabase;
    private List<Category> categoryList = new ArrayList<>();
    private RecyclerView recyclerItems;
    private CategoryAdapter mItemsAdapter;
    private FirebaseStorage mStorage;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home, container, false);
        categoryList.clear();
        mStorage= FirebaseStorage.getInstance();
        Log.d("HomeFragment", mStorage.getReference().toString());
        mDatabase = FirebaseDatabase.getInstance().getReference().child("categories");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("HomeFragment","Inside");
                String categoryName = dataSnapshot.getValue().toString();
                Category category = new Category();
                category.setCategoryName(categoryName);
                categoryList.add(category);
                mItemsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        recyclerItems = (RecyclerView) view.findViewById(R.id.listItems);
        mItemsAdapter = new CategoryAdapter(categoryList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerItems.setLayoutManager(mLayoutManager);
        recyclerItems.setItemAnimator(new DefaultItemAnimator());
        //recyclerItems.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerItems.setAdapter(mItemsAdapter);
        recyclerItems.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                Intent i = new Intent(getActivity(), ItemsActivity.class);
                i.putExtra("CATEGORY", categoryList.get(position).categoryName);
                startActivity(i);
            }
        }));
        return view;
    }

}
