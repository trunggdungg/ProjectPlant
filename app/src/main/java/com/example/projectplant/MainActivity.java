    package com.example.projectplant;

    import android.content.Intent;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;

    import com.example.projectplant.model.User;
    import com.example.projectplant.retrofit.ApiAppPlant;
    import com.example.projectplant.retrofit.RetrofitClient;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.android.material.snackbar.Snackbar;
    import com.google.android.material.navigation.NavigationView;

    import androidx.annotation.NonNull;
    import androidx.navigation.NavController;
    import androidx.navigation.Navigation;
    import androidx.navigation.ui.AppBarConfiguration;
    import androidx.navigation.ui.NavigationUI;
    import androidx.drawerlayout.widget.DrawerLayout;
    import androidx.appcompat.app.AppCompatActivity;

    import com.example.projectplant.databinding.ActivityMainBinding;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.messaging.FirebaseMessaging;
    import com.example.projectplant.utils.Utils;

    import io.paperdb.Paper;
    import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
    import io.reactivex.rxjava3.disposables.CompositeDisposable;
    import io.reactivex.rxjava3.schedulers.Schedulers;

    public class MainActivity extends AppCompatActivity {

        private AppBarConfiguration mAppBarConfiguration;
        private ActivityMainBinding binding;
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        ApiAppPlant apiAppPlant;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            setSupportActionBar(binding.appBarMain.toolbar);
            binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setAnchorView(R.id.fab).show();
                }
            });

//            // Initialize PaperDB
            Paper.init(this);
            if (Paper.book().read("user") != null) {
                User user = Paper.book().read("user");
                Utils.user_current = user;
                Log.d("MainActivity", "User loaded from PaperDB: " + user.toString());
            } else {
                Log.d("MainActivity", "No user found in PaperDB");
            }


            // Initialize ApiAppPlant
            apiAppPlant = RetrofitClient.getInstance().create(ApiAppPlant.class);

            // Get token
            getToken();
            DrawerLayout drawer = binding.drawerLayout;
            NavigationView navigationView = binding.navView;

            // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_search, R.id.nav_cart, R.id.nav_wishlist, R.id.nav_profile, R.id.nav_signout)
                    .setOpenableLayout(drawer)
                    .build();

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            // Handle menu item clicks
            navigationView.setNavigationItemSelectedListener(menuItem -> {
                int id = menuItem.getItemId();
                if (id == R.id.nav_signout) {
                    signOut();
                    return true;
                }
                return NavigationUI.onNavDestinationSelected(menuItem, navController)
                        || super.onOptionsItemSelected(menuItem);
            });
        }

        private void getToken() {
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    if (!TextUtils.isEmpty(s)) {
                        User user = Paper.book().read("user");
                        if (user != null && user.getId() != 0) {
                            Log.d("UpdateToken", "Attempting to update token. User ID: " + user.getId() + " Token: " + s);
                            updateTokenToServer(user.getId(), s);
                        } else {
                            Log.e("UpdateToken", "User information is missing or invalid");
                        }
                    }
                }
            });
        }

        private void updateTokenToServer(int userId, String token) {
            compositeDisposable.add(
                    apiAppPlant.updatetoken(userId, token)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {
                                        Log.d("UpdateToken", "Token updated successfully for user ID: " + userId);
                                    },
                                    throwable -> {
                                        Log.e("UpdateToken", "Failed to update token: " + throwable.getMessage());
                                    }
                            )
            );
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.nav_signout) {
                signOut();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        private void signOut() {
            // Clear user data from Paper or SharedPreferences
            Paper.book().delete("user");

            // Sign out from Firebase
            FirebaseAuth.getInstance().signOut();

            // Redirect to login activity
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        @Override
        public boolean onSupportNavigateUp() {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                    || super.onSupportNavigateUp();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            // Dispose of all RxJava subscriptions
            compositeDisposable.clear();
        }
    }
