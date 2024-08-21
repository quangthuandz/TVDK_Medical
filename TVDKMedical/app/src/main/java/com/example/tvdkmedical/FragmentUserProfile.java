package com.example.tvdkmedical;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tvdkmedical.fragments.HomeFragment;

public class FragmentUserProfile extends Fragment {

    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;
    private static final String PHONE_NUMBER = "0978788128";
    private Boolean isAuthenticated = false;
    private Button takePermission, cardViewer;
    private CardView returnBtn, editProfile;

    public FragmentUserProfile() {
        // Required empty public constructor
    }

    public static FragmentUserProfile newInstance(String param1, String param2) {
        FragmentUserProfile fragment = new FragmentUserProfile();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param1");
            String mParam2 = getArguments().getString("param2");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        returnBtn  = view.findViewById(R.id.returnBtn);
        editProfile = view.findViewById(R.id.editCardView);
  //      cardViewer.setOnClickListener(v -> replaceFragment(new LibraryFragment()));
        editProfile.setOnClickListener(v->replaceFragment(new FragmentEditProfile()));
       returnBtn.setOnClickListener(v -> replaceFragment(new HomeFragment()));
        return view;
    }


    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


    private void requestCallPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CALL_PHONE)) {
                showPermissionRationale();
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
            }
        } else {
            Toast.makeText(getContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
            if (isAuthenticated) {
                makePhoneCall();
            }
        }
    }

    private void showPermissionRationale() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Permission Needed")
                .setMessage("This permission is needed to make phone calls directly from the app.")
                .setPositiveButton("OK", (dialog, which) -> {
                    isAuthenticated = true;
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                showPermissionRequiredWarning();
            }
        }
    }

    private void makePhoneCall() {
        String dial = "tel:" + PHONE_NUMBER;
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
    }

    private void showPermissionRequiredWarning() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Permission Required")
                .setMessage("Phone call permission is required to call the host. Please grant the permission.")
                .setPositiveButton("OK", (dialog, which) -> requestCallPermission())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}
