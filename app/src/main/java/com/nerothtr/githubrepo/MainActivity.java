package com.nerothtr.githubrepo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nerothtr.githubrepo.app.Constants;
import com.nerothtr.githubrepo.fragment.AboutFragment;
import com.nerothtr.githubrepo.fragment.BookmarkFragment;
import com.nerothtr.githubrepo.fragment.HomeFragment;
import com.nerothtr.githubrepo.fragment.SettingsFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private Fragment mHomeFragment;
    private Fragment mBookmarkFragment;
    private Fragment mSettingsFragment;
    private Fragment mAboutFragment;

    private ArrayList<String> mFragListTag;
    private ArrayList<FragmentTag> mFragmentTags;
    private SearchView mSearchView;

    private boolean mDoubleBackToExitOnce = false;

    private static void taglines(String name) {
        Log.d(TAG, "taglines: " + name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        init();
        setHeaderImage();
    }

    private void init() {

        if (mHomeFragment == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mHomeFragment = new HomeFragment();
            fragmentTransaction.add(R.id.fragment_container, mHomeFragment, Constants.HOME_FRAGMENT_TAG);
            fragmentTransaction.commit();

            mFragListTag = new ArrayList<>();
            mFragListTag.add(Constants.HOME_FRAGMENT_TAG);

            mFragmentTags = new ArrayList<>();
            mFragmentTags.add(new FragmentTag(mHomeFragment, Constants.HOME_FRAGMENT_TAG));
        } else {
            mFragListTag.remove(Constants.HOME_FRAGMENT_TAG);
            mFragListTag.add(Constants.HOME_FRAGMENT_TAG);
        }

        setFragmentVisibility(Constants.HOME_FRAGMENT_TAG);

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        int tagCount = mFragListTag.size();
        if (tagCount > 1) {
            String tag = mFragListTag.get(tagCount - 2);
            setFragmentVisibility(tag);
            mFragListTag.remove(mFragListTag.get(tagCount - 1));
        } else {
            if (mDoubleBackToExitOnce) {
                super.onBackPressed();
                return;
            }

            mDoubleBackToExitOnce = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

            new Handler(Looper.myLooper()).postDelayed(() -> mDoubleBackToExitOnce = false, 2000);

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                init();
                mFragListTag.clear();
                mFragListTag.add(Constants.HOME_FRAGMENT_TAG);
                taglines(Constants.HOME_FRAGMENT_TAG);
                mSearchView.setVisibility(View.VISIBLE);
                if (!mSearchView.isIconified())
                    mSearchView.setIconified(true);
                break;

            case R.id.nav_bookmark:
                if (mBookmarkFragment == null) {
                    mBookmarkFragment = new BookmarkFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mBookmarkFragment, Constants.BOOKMARK_FRAGMENT_TAG);
                    fragmentTransaction.commit();
                    mFragmentTags.add(new FragmentTag(mBookmarkFragment, Constants.BOOKMARK_FRAGMENT_TAG));
                } else {
                    mFragListTag.remove(Constants.BOOKMARK_FRAGMENT_TAG);
                }
                mFragListTag.add(Constants.BOOKMARK_FRAGMENT_TAG);

                setFragmentVisibility(Constants.BOOKMARK_FRAGMENT_TAG);
                taglines(Constants.BOOKMARK_FRAGMENT_TAG);

                break;

            case R.id.nav_settings:
                if (mSettingsFragment == null) {
                    mSettingsFragment = new SettingsFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mSettingsFragment, Constants.SETTINGS_FRAGMENT_TAG);
                    fragmentTransaction.commit();
                    mFragmentTags.add(new FragmentTag(mSettingsFragment, Constants.SETTINGS_FRAGMENT_TAG));
                } else {
                    mFragListTag.remove(Constants.SETTINGS_FRAGMENT_TAG);
                }
                mFragListTag.add(Constants.SETTINGS_FRAGMENT_TAG);

                mSearchView.setVisibility(View.GONE);
                setFragmentVisibility(Constants.SETTINGS_FRAGMENT_TAG);
                taglines(Constants.SETTINGS_FRAGMENT_TAG);

                break;

            case R.id.nav_about:
                if (mAboutFragment == null) {
                    mAboutFragment = new AboutFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mAboutFragment, Constants.ABOUT_FRAGMENT_TAG);
                    fragmentTransaction.commit();
                    mFragmentTags.add(new FragmentTag(mAboutFragment, Constants.ABOUT_FRAGMENT_TAG));
                } else {
                    mFragListTag.remove(Constants.ABOUT_FRAGMENT_TAG);
                }
                mFragListTag.add(Constants.ABOUT_FRAGMENT_TAG);

                mSearchView.setVisibility(View.GONE);
                setFragmentVisibility(Constants.ABOUT_FRAGMENT_TAG);
                taglines(Constants.ABOUT_FRAGMENT_TAG);

                break;

            case R.id.nav_sign_out:
                new AlertDialog.Builder(this).setTitle("Do you want to sign out?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        dialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragmentVisibility(String tagName) {

        for (int i = 0; i < mFragmentTags.size(); i++) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (tagName.equals(mFragmentTags.get(i).getTag())) {
                fragmentTransaction.show(mFragmentTags.get(i).getFragment());
                taglines("showing " + tagName);

            } else {
                fragmentTransaction.hide(mFragmentTags.get(i).getFragment());
                taglines("hiding " + tagName);

            }
            fragmentTransaction.commit();
        }

        taglines(mFragListTag.toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.top_menu_search);
        mSearchView = findViewById(R.id.search_view);
        mSearchView = (SearchView) menuItem.getActionView();
        mSearchView.setQueryHint("Search Repository");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.d(Constants.TAG, "onCreateOptionsMenu: " + query);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.SEARCH_DATA_KEY, query);

                getSupportFragmentManager().beginTransaction().remove(mHomeFragment).commit();

                for (int i = 0; i < mFragmentTags.size(); i++) {
                    if (mFragmentTags.get(i).getTag().equals(Constants.HOME_FRAGMENT_TAG)) {
                        mFragmentTags.remove(mFragmentTags.get(i));
                        break;
                    }
                }

                mHomeFragment = new HomeFragment();
                mHomeFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mHomeFragment, Constants.HOME_FRAGMENT_TAG).commit();
                mFragmentTags.add(new FragmentTag(mHomeFragment, Constants.HOME_FRAGMENT_TAG));

                mFragListTag.remove(Constants.HOME_FRAGMENT_TAG);
                mFragListTag.add(Constants.HOME_FRAGMENT_TAG);
                setFragmentVisibility(Constants.HOME_FRAGMENT_TAG);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void setHeaderImage() {
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView imageView = headerView.findViewById(R.id.iv_header);
        Glide.with(this).load(R.drawable.github).centerCrop().into(imageView);
    }

    private void checkAuthState() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.d(TAG, "checkAuthState: user is null");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else
            Log.d(TAG, "checkAuthState: user is authenticated");
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthState();
    }
}