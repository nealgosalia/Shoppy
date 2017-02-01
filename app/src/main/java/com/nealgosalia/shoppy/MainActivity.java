package com.nealgosalia.shoppy;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ncapdevi.fragnav.FragNavController;
import com.nealgosalia.shoppy.Activities.AccountActivity;
import com.nealgosalia.shoppy.Fragments.CartFragment;
import com.nealgosalia.shoppy.Fragments.FavFragment;
import com.nealgosalia.shoppy.Fragments.HomeFragment;
import com.nealgosalia.shoppy.Fragments.InfoFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragNavController fragNavController;
    private final int TAB_FIRST = FragNavController.TAB1;
    private final int TAB_SECOND = FragNavController.TAB2;
    private final int TAB_THIRD = FragNavController.TAB3;
    private final int TAB_FOURTH = FragNavController.TAB4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);
        List<Fragment> fragments = new ArrayList<>(4);
        fragments.add(new HomeFragment());
        fragments.add(new FavFragment());
        fragments.add(new CartFragment());
        fragments.add(new InfoFragment());
        fragNavController = new FragNavController(savedInstanceState, getSupportFragmentManager(), R.id.contentContainer, fragments, TAB_FIRST);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_home:
                        fragNavController.switchTab(TAB_FIRST);
                        break;
                    case R.id.tab_favourites:
                        fragNavController.switchTab(TAB_SECOND);
                        break;
                    case R.id.tab_cart:
                        fragNavController.switchTab(TAB_THIRD);
                        break;
                    case R.id.tab_info:
                        fragNavController.switchTab(TAB_FOURTH);
                        break;
                }
            }
        });
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            public void onTabReSelected(@IdRes int tabId) {
                fragNavController.clearStack();
            }
        });
    }

    public void accountSettingsClicked(View v){
        Intent i =new Intent(MainActivity.this, AccountActivity.class);
        startActivity(i);
    }
}
