package com.example.tvdkmedical;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;




public class FragmentEditProfile extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private CardView returnBtn, userAvatar, editIcon;
    private EditText dobText,genderText, editName, editPhone, cccdText;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private ImageView userAvatarImg ;
    private ProgressBar progressBar;
    private Button saveBtn, cancelBtn;
    private Uri imageUri;
    private Bitmap avatarConverted;
    private String avatarBase64;

    public FragmentEditProfile() {
        // Required empty public constructor
    }

    public static FragmentEditProfile newInstance(String param1, String param2) {
        FragmentEditProfile fragment = new FragmentEditProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadUserAvatar();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        returnBtn = view.findViewById(R.id.returnCardView);
        dobText = view.findViewById(R.id.dobText);
        userAvatarImg = view.findViewById(R.id.userAvatarImg);
        genderText = view.findViewById(R.id.genderText);
        editName = view.findViewById(R.id.editName);
        editPhone = view.findViewById(R.id.phoneNumber);
        userAvatar = view.findViewById(R.id.userAvatar);
        saveBtn = view.findViewById(R.id.saveBtn);
        progressBar = view.findViewById(R.id.progressBar);
        cancelBtn = view.findViewById(R.id.cancelBtn);
        cccdText = view.findViewById(R.id.cccdText);
        editIcon = view.findViewById(R.id.editIcon);
        returnBtn.setOnClickListener(v -> replaceFragment(new com.example.tvdkmedical.FragmentUserProfile()));
        dobText.setOnClickListener(this::showDatePicker);
        editIcon.setOnClickListener(v -> {

            openImagePicker();
        });
        genderText.setOnClickListener(this::showGenderPicker);

        saveBtn.setOnClickListener(v -> {
            if(editName == null){
                printError("Name Null");
            }else if (dobText == null){
                printError("DOB Null");
            }else if (editPhone == null){
                printError("Phone Null");
            }else if (cccdText == null){
                printError("cccdText Null");
            }
            else {
              saveUserData();
            }
        });

        return view;
    }
    private void printError(String error){
        Toast.makeText(getActivity(), "Save Successful", Toast.LENGTH_SHORT).show();
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();


            try {
                // Check the file size
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                int imageSize = inputStream.available();
                inputStream.close();

//                if (imageSize > 15 * 1024 * 1024) { // 15 MB in bytes
//                    Toast.makeText(getActivity(), "Image is too large. Please select an image smaller than 15MB.", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                // Decode the image dimensions
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();

                int imageWidth = options.outWidth;
                int imageHeight = options.outHeight;

//                if (imageWidth > 2000 || imageHeight > 2000) {
//                    Toast.makeText(getActivity(), "Image dimensions are too large. Please select an image smaller than 2000x2000 pixels.", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, 2000, 2000);
                options.inJustDecodeBounds = false;

                // Load and downsample the image
                inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();

                userAvatarImg.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                // Handle the case where the user denied the permission
            }
        }
    }

    public void showDatePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.show();
    }
    public Bitmap imageViewToBitmap(ImageView imageView) {
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dobText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
    }

    private void showGenderPicker(View view) {
        final String[] genderOptions = {"Male", "Female", "Other"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Gender")
                .setItems(genderOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        genderText.setText(genderOptions[which]);
                    }
                });
        builder.create().show();
    }

//    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getParentFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout, fragment);
//        fragmentTransaction.commit();
//    }

    public Uri imageViewToUri(Context context, ImageView imageView) {
        // Convert ImageView to Bitmap
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // Save Bitmap to a file
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg");
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get Uri of the file
        return Uri.fromFile(file);
    }

    private void saveUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return; // Handle the case where the user is not authenticated
        }
        progressBar.setVisibility(View.VISIBLE);
        String userId = user.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", editName.getText().toString().trim());
        userData.put("dob", dobText.getText().toString().trim());
        userData.put("gender", genderText.getText().toString().trim());
        userData.put("phone", editPhone.getText().toString().trim());
        userData.put("cccd", cccdText.getText().toString().trim());

        Bitmap bitmap = ((BitmapDrawable) userAvatarImg.getDrawable()).getBitmap();
        String avatarBase64 = bitmapToBase64(bitmap);
        userData.put("avatarConverted", avatarBase64);

        databaseReference.setValue(userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Save Successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Save Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Bitmap base64ToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void loadUserAvatar() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String avatarBase64 = dataSnapshot.child("avatarConverted").getValue(String.class);
                        if (avatarBase64 != null) {
                            try {
                                Bitmap avatarBitmap = base64ToBitmap(avatarBase64);
                                userAvatarImg.setImageBitmap(avatarBitmap);
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                                // Handle the case where the Base64 string could not be decoded
                            }
                        }
                        // Retrieve and set other user data
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String dob = dataSnapshot.child("dob").getValue(String.class);
                        String gender = dataSnapshot.child("gender").getValue(String.class);
                        String phone = dataSnapshot.child("phone").getValue(String.class);
                        String cccd = dataSnapshot.child("cccd").getValue(String.class);

                        if (name != null) {
                            editName.setText(name);
                        }
                        if (dob != null) {
                            dobText.setText(dob);
                        }
                        if (gender != null) {
                            genderText.setText(gender);
                        }
                        if (phone != null) {
                            editPhone.setText(phone);
                        }
                        if (cccd != null) {
                            cccdText.setText(cccd);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }
    }
}
