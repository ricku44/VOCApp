package com.example.greapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String sr = user.getUid();
    CircleImageView profile_pic;
    Uri file;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextInputEditText name = findViewById(R.id.username);
        TextInputEditText email = findViewById(R.id.emailadd);
        TextInputEditText pno = findViewById(R.id.phn_no);

        TextView name2 = findViewById(R.id.usrname);

        Button btn = findViewById(R.id.edit_save);

        profile_pic = findViewById(R.id.profilepic);
        name.setText(user.getDisplayName());
        name2.setText(user.getDisplayName());
        email.setText(user.getEmail());
        pno.setText(user.getPhoneNumber());


        TextView update_score = findViewById(R.id.score);
        db.collection(user.getUid()).document("score").get().addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                DocumentSnapshot documnet = task.getResult();
                update_score.setText(documnet.getData().get("1").toString());

            }

        });

        pno.setText(user.getPhoneNumber());

        mStorageRef = FirebaseStorage.getInstance().getReference();

        btn.setOnClickListener(v -> {
//
            if (btn.getText().equals("Edit")) {
                btn.setText("Save");
                name.setEnabled(true);
                email.setEnabled(true);

            } else if (btn.getText().equals("Save")) {
                btn.setText("Edit");
                name.setEnabled(false);
                email.setEnabled(false);

                setName_email(name.getText().toString(),email.getText().toString());

            }

        });

        profile_pic.setOnClickListener((View v) -> {

            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 1);


        });



        downloadfile();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        profile_pic.setImageBitmap(selectedImage);
                        uploadfile();
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                profile_pic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                                uploadfile();
                            }
                        }

                    }
                    break;
            }
        }

    }

    public void uploadfile() {
        StorageReference Ref = mStorageRef.child("pictures/" + sr + "/profile.jpg");
        profile_pic.setDrawingCacheEnabled(true);
        profile_pic.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) profile_pic.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = Ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Not uploaded");
            }
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
            System.out.println("Uploaded");
        });
    }


    public void downloadfile(){

        mStorageRef.child("pictures/"+sr+"/profile.jpg").getDownloadUrl().addOnSuccessListener(uri -> loadimage(uri)).addOnFailureListener(exception -> file = null);
    }

    public void loadimage(Uri uri)
    {
        Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
        builder.listener((picasso, uri1, arg1) -> Log.e("Picasso Error", "Failed to load image: " + arg1)
        );
        //Picasso pic = builder.build();
        Picasso.with(getApplicationContext()).load(uri).into(profile_pic);
        Log.e("LOL","error");
    }

    public void setName_email(String name , String email)
    {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("ff", "User profile updated.");
                    }
                });

        user.updateEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("ffff", "User email address updated.");
                    }
                });



    }


}