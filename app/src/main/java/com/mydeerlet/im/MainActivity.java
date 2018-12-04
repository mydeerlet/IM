package com.mydeerlet.im;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.mydeerlet.im.fragmentHome.Home1;
import com.mydeerlet.im.fragmentHome.Home2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    /**
     * 当前Fragment
     */
    private Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //让图片就是显示他本身的颜色
        navigation.setItemIconTintList(null);

        //显示相应的fragment
        if (savedInstanceState != null){
            String tag = savedInstanceState.getString("tag");
            currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
            switch (savedInstanceState.getInt("index")){
                case 0:
                    navigation.setSelectedItemId(R.id.navigation_home1);
                    break;
                case 1:
                    navigation.setSelectedItemId(R.id.navigation_home2);
                    break;

            }
        } else {
            replaceFragment("home1");
        }

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.navigation_home1:
                    replaceFragment("home1");

                    return true;
                case R.id.navigation_home2:
                    replaceFragment("home2");
                    return true;
            }
            return false;
        }
    };

    //显示哪一个fragment
    private void replaceFragment(String tag) {
        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(currentFragment).commit();
        }
        currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment == null) {
            switch (tag) {
                case "home1":
                    currentFragment = new Home1();
                    break;
                case "home2":
                    currentFragment = new Home2();
                    break;

            }
            getSupportFragmentManager().beginTransaction().add(R.id.container, currentFragment, tag).commit();
        }else {
            getSupportFragmentManager().beginTransaction().show(currentFragment).commit();
        }
    }


}
