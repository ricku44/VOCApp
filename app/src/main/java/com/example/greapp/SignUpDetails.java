package com.example.greapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpDetails extends AppCompatActivity {

    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);


        TextInputEditText name = findViewById(R.id.username);
        TextInputEditText email = findViewById(R.id.emailadd);
        TextInputEditText pno = findViewById(R.id.phn_no);

        Button btn = findViewById(R.id.edit_save);

        if(user.getDisplayName()!=null) {
            name.setEnabled(false);
            name.setText(user.getDisplayName());
        }

        if(user.getPhoneNumber()!=null) {
            pno.setEnabled(false);
            pno.setText(user.getPhoneNumber());
        }

        if(user.getEmail()!=null) {
            email.setEnabled(false);
            email.setText(user.getEmail());
        }

        btn.setOnClickListener(v -> {

            if (name.getText()==null || name.getText().toString().equals(""))
                name.setBackgroundColor(Color.RED);
            else if (email.getText()==null || !isValidEmail(email.getText().toString()))
                email.setBackgroundColor(Color.RED);
            else if (pno.getText()==null || !isValidMobile(pno.getText().toString()) || pno.getText().toString().length()<10)
                pno.setBackgroundColor(Color.RED);
            else {

                Map<String,String> obj = new HashMap<>();
                obj.put("1",(pno.getText().toString()));
                FirebaseFirestore.getInstance().collection(user.getUid()).document("Phone").set(obj);

                setName_email(name.getText().toString(),email.getText().toString());
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });

    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public void setName_email(String name , String email) {
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
