package com.example.tvdkmedical;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.adapters.PostAdapter;
import com.example.tvdkmedical.databinding.ActivityMainBinding;
import com.example.tvdkmedical.databinding.ActivityViewMainContentBinding;
import com.example.tvdkmedical.fragments.AppointmentFragment;
import com.example.tvdkmedical.fragments.AppointmentListDoctorFragment;
import com.example.tvdkmedical.fragments.HomeFragment;
import  com.example.tvdkmedical.R;
import com.example.tvdkmedical.fragments.SearchFragment;
import com.example.tvdkmedical.fragments.UserProfileFragment;
import com.example.tvdkmedical.models.Appointment;
import com.example.tvdkmedical.models.Post;
import com.example.tvdkmedical.views.appointment.AppointmentDetailsActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ViewMainContent extends AppCompatActivity {

    ActivityViewMainContentBinding binding;
    Fragment homeFragment;
    Fragment userProfileFragment;
    Fragment appointmentFragment;
    Fragment searchFragment;
    Fragment activeFragment;
    Fragment geminiFragment;
    Fragment appointmentListDoctorFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewMainContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        homeFragment = new HomeFragment();
        userProfileFragment = new FragmentUserProfile();
        appointmentFragment = new AppointmentFragment();
        geminiFragment = new GeminiFragment();
        searchFragment = new SearchFragment();
        //appointmentListDoctorFragment = new AppointmentListDoctorFragment();
        activeFragment = homeFragment;

        getSupportFragmentManager().beginTransaction()
               // .add(R.id.frame_layout,appointmentListDoctorFragment,"6").hide(appointmentListDoctorFragment)
                .add(R.id.frame_layout,searchFragment,"5").hide(searchFragment)
                .add(R.id.frame_layout,geminiFragment,"4").hide(geminiFragment)
                .add(R.id.frame_layout,appointmentFragment,"3").hide(appointmentFragment)
                .add(R.id.frame_layout, userProfileFragment, "2").hide(userProfileFragment)
                .add(R.id.frame_layout, homeFragment, "1")
                .commit();

        binding.bottomnavigation.setOnItemSelectedListener(item -> {
            switch (item.getTitle().toString()) {
                case "Home":
                    showFragment(homeFragment);
                    break;
                case "Profile":

                    showFragment(userProfileFragment);

                    break;
                case "Gemini Analysis":
                    showFragment(geminiFragment);
                    break;

                case "Appointment":
                    showFragment(appointmentFragment);
                    break;
//                    showFragment(appointmentListDoctorFragment);
//                    break;
                case "Search":
                    showFragment(searchFragment);
                    break;
                default:
                    break;
            }
            return true;
        });

    }
    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(activeFragment).show(fragment).commit();
        activeFragment = fragment;
    }
}
