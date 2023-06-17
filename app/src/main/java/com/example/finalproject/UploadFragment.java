package com.example.finalproject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class UploadFragment extends Fragment {
    View view;
    private FloatingActionButton uploadBtn;
    private ImageView uploadImage;
    EditText uploadCaption;
    ProgressBar progressBar;
    private Uri imageUri;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Images");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    // Declare a constant for the image picker request code
    private static final int IMAGE_PICKER_REQUEST_CODE = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    ActivityResultLauncher<Intent> activityResultLauncher;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload, container, false);

        uploadBtn = view.findViewById(R.id.uploadBtn);
        uploadCaption = view.findViewById(R.id.uploadCaption);
        uploadImage = view.findViewById(R.id.uploadImage);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        ActivityResultLauncher<Intent> activityResultLauncherGallery = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                if (data.getData() != null) {
                                    // Image selected from gallery
                                    imageUri = data.getData();
                                    uploadImage.setImageURI(imageUri);
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        ActivityResultLauncher<Intent> activityResultLauncherCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                    // Image captured from camera
                                    Bundle extras = data.getExtras();
                                    if (extras != null) {
                                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                                        // Save the image to a file
                                        imageUri = saveImageToStorage(imageBitmap);
                                        // Set the image URI to the ImageView
                                        uploadImage.setImageURI(imageUri);

                                    }
                            }
                        } else {
                            Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Image");
                builder.setItems(new CharSequence[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index) {
                        switch (index) {
                            case 0:
                                // Open gallery to select an image
                                Intent galleryPicker = new Intent();
                                galleryPicker.setAction(Intent.ACTION_GET_CONTENT);
                                galleryPicker.setType("image/*");
                                activityResultLauncherGallery.launch(galleryPicker);
                                break;
                            case 1:
                               // Open camera to capture an image
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (cameraIntent.resolveActivity(getContext().getPackageManager()) != null) {
                                    activityResultLauncherCamera.launch(cameraIntent);
                                } else {
                                    Toast.makeText(getContext(), "No camera app found", Toast.LENGTH_SHORT).show();
                                }

                                break;
                        }
                    }
                });
                builder.show();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    uploadToFirebase(imageUri,uploadCaption.getText().toString());
                }
                else {
                    Toast.makeText(getActivity(),"Please select Image",Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private Uri saveImageToStorage(Bitmap imageBitmap) {

            // Create a file to save the image
            File imagesDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(imagesDir, "captured_image.jpg");

            try {
                // Compress the bitmap and save it to the file
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle any errors that occur during the image saving process
            }

            // Generate a content URI for the saved image file
            return FileProvider.getUriForFile(getActivity(), "com.example.finalproject.fileprovider", imageFile);


    }


    public void uploadToFirebase (Uri uri  , String uploadCaption)
    {
        // Get the current user ID
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create a storage reference for the image
        StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

        // Upload the image to Firebase Storage
        imageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL of the uploaded image
                        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Create a new DataClass object with the imageURL, caption, and currentUserId
                                DataClass dataClass = new DataClass(uri.toString(), uploadCaption, currentUserId);

                                // Save the data to Firebase
                                String key = databaseReference.push().getKey();
                                databaseReference.child(key).setValue(dataClass)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Data saved successfully
                                                Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_LONG).show();
                                                Fragment secondFrag = new NotesFragment();
                                                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                                                fm.replace(R.id.frameLayout, secondFrag).commit();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Failed to save data
                                                Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_LONG).show();
                    }
                });
    }


    private String getFileExtension (Uri fileUri)
    {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

}