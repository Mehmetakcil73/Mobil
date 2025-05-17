package com.example.proje;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.proje.SessionManager.SessionManager;
import com.example.proje.databinding.ActivityMainBinding;
import com.example.proje.ViewModel.UserViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private UserViewModel userViewModel;
    private SessionManager sessionManager;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar
        setSupportActionBar(binding.toolbar);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        sessionManager = new SessionManager(this);

        if (!userViewModel.isLoggedIn() && !sessionManager.isGuest()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNav, navController);

            // Hide menu items based on user status
            Menu menu = binding.bottomNav.getMenu();
            boolean isAdmin = userViewModel.isCurrentUserAdmin();
            
            // Admin menü öğelerini ayarla
            menu.findItem(R.id.adminUserListFragment).setVisible(isAdmin);
            menu.findItem(R.id.myCarListFragment).setVisible(!isAdmin); // Admin için İlanlarım gizlenir
            
            // Misafir kullanıcı kontrolleri
            if (sessionManager.isGuest()) {
                menu.findItem(R.id.myCarListFragment).setVisible(false);
                menu.findItem(R.id.profileFragment).setVisible(false);
                
                // Add navigation listener to show login message for restricted features
                binding.bottomNav.setOnItemSelectedListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.myCarListFragment || itemId == R.id.profileFragment) {
                        Toast.makeText(this, "Bu özelliği kullanmak için giriş yapmalısınız", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    return NavigationUI.onNavDestinationSelected(item, navController);
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (sessionManager.isGuest()) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            sessionManager.clearSession();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
