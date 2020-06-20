package com.example.greapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Login extends AppCompatActivity implements TextWatcher {

    private FirebaseAuth mAuth;
    public static String smscode = null;
    private String mVerificationId;
    private String phoneNumber = null;
    private static final int RC_SIGN_IN = 9001;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;

    private EditText editText_one;
    private EditText editText_two;
    private EditText editText_three;
    private EditText editText_four;
    private EditText editText_five;
    private EditText editText_six;
    private Dialog custom_dialog;
    private ProgressDialog progressDialog;
    AlertDialog.Builder builder;


    private GoogleSignInClient mGoogleSignInClient;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    FirebaseUser mUser;
    CallbackManager callbackManager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button login1 = findViewById(R.id.login1);
        final Button signup1 = findViewById(R.id.signin1);
        Button otp = findViewById(R.id.OTP);
        LoginButton loginButton = findViewById(R.id.buttonFacebookLogin);

        LinearLayout layout1 = findViewById(R.id.ll3);
        LinearLayout layout2 = findViewById(R.id.ll2);
        LinearLayout layout3 = findViewById(R.id.ll4);
        final EditText phone = findViewById(R.id.ed1);

        EditText input_name = findViewById(R.id.input_name);
        EditText input_email = findViewById(R.id.input_email);
        EditText input_password = findViewById(R.id.input_password);
        Button btn_signup = findViewById(R.id.btn_signup);
        
        mAuth = FirebaseAuth.getInstance();
        
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Verifying ...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                updateUI2(STATE_VERIFY_SUCCESS, credential);
                if(mUser!=null)
                    linkCredential(credential);
                else
                    callDialog(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    phone.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    phone.setError("Too Many Attempts! Please try again after sometime");
                }
                updateUI2(STATE_VERIFY_FAILED);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mResendToken = token;
                mVerificationId = verificationId;

                updateUI2(STATE_CODE_SENT);
            }
        };


        custom_dialog = new Dialog(this,R.style.AppTheme);
        custom_dialog.setContentView(R.layout.custom__dialog);

        editText_one = custom_dialog.findViewById(R.id.editTextone);
        editText_two = custom_dialog.findViewById(R.id.editTexttwo);
        editText_three = custom_dialog.findViewById(R.id.editTextthree);
        editText_four = custom_dialog.findViewById(R.id.editTextfour);
        editText_five =custom_dialog. findViewById(R.id.editTextfive);
        editText_six = custom_dialog.findViewById(R.id.editTextsix);

        editText_one.addTextChangedListener(this);
        editText_two.addTextChangedListener(this);
        editText_three.addTextChangedListener(this);
        editText_four.addTextChangedListener(this);
        editText_five.addTextChangedListener(this);
        editText_six.addTextChangedListener(this);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted2 = prefs.getBoolean(getString(R.string.firsthome1), false);
        if (previouslyStarted2) {

            builder = new AlertDialog.Builder(this);

            builder.setMessage("Please disable the notification for better experience.")
                    .setCancelable(false)
                    .setPositiveButton("Proceed", (dialog, id) -> {


                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putBoolean(getString(R.string.firsthome1), Boolean.TRUE);
                        edit.apply();

                        Intent intent = new Intent();

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {

                            intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getApplicationContext().getPackageName());
                            intent.putExtra(Settings.EXTRA_CHANNEL_ID, "com.example.GreApp");
                            startActivity(intent);
                        }

                        else {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getApplicationContext().getPackageName());
                                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, "com.example.GreApp");
                                } else {
                                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                    intent.putExtra("app_package", getApplicationContext().getPackageName());
                                    intent.putExtra("app_uid", getApplicationContext().getApplicationInfo().uid);
                                }

                                startActivity(intent);
                                startService(new Intent(getApplicationContext(), InstOverlay.class));
                            }
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> {
                        dialog.cancel();
                    });

            AlertDialog alert = builder.create();
            alert.setTitle("Next Steps");
            alert.show();
        }

        if(smscode!=null){
            editText_one.setText(smscode.charAt(0));
            editText_two.setText(smscode.charAt(1));
            editText_three.setText(smscode.charAt(2));
            editText_four.setText(smscode.charAt(3));
            editText_five.setText(smscode.charAt(4));
            editText_six.setText(smscode.charAt(5));
        }


        callbackManager = CallbackManager.Factory.create();
        loginButton.setPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }
                    @Override
                    public void onCancel() {
                    }
                    @Override
                    public void onError(FacebookException error) {
                    }
                });

        login1.setOnClickListener(v -> {
            signup1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textlines2));
            login1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textlines));
            login1.setTypeface(signup1.getTypeface());
            signup1.setTypeface(login1.getTypeface());
            layout1.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        });

        signup1.setOnClickListener(v -> {
            signup1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textlines));
            login1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textlines2));
            login1.setTypeface(signup1.getTypeface());
            signup1.setTypeface(login1.getTypeface());
            layout2.setVisibility(View.VISIBLE);
            layout1.setVisibility(View.INVISIBLE);
            layout3.setVisibility(View.GONE);
        });

        otp.setOnClickListener(v -> {

            String number = phone.getText().toString();

            if(phone.getText().toString().isEmpty() || number.length()<15)
                phone.setError("Invalid phone number.");
            else{
                phoneNumber = "+91"+number.substring(5,15);
                view();
                progressDialog.show();
            }

        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith(" +91 ")) {
                    if(s.toString().equals(" +91"))
                        phone.setText("");
                    else
                        phone.setText(" +91 " + s);
                        Selection.setSelection(phone.getText(), phone.getText().length());
                }
            }
        });


        custom_dialog.findViewById(R.id.resend).setOnClickListener(v -> {
            custom_dialog.findViewById(R.id.done).setVisibility(View.VISIBLE);
            resendVerificationCode(phoneNumber, mResendToken);
        });

        custom_dialog.findViewById(R.id.button1).setOnClickListener(v -> {
            verifyPhoneNumberWithCode(mVerificationId, smscode);
        });


        findViewById(R.id.fb).setOnClickListener(v -> loginButton.performClick());

        findViewById(R.id.google).setOnClickListener(v -> signIn());

        btn_signup.setOnClickListener(view -> {
            if (input_name.getText()==null || input_name.getText().toString().equals(""))
                input_name.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            else if (input_email.getText()==null || !isValidEmail(input_email.getText().toString()))
                input_email.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            else if (input_password.getText()==null || input_password.getText().toString().length()<10)
                input_password.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            else {
                registerUser(input_email.getText().toString(),input_password.getText().toString(),input_name.getText().toString(), null);
            }
        });

    }


    private void registerUser(String email, String password, String name, PhoneAuthCredential credential){

        progressDialog.setMessage("Registering...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean previouslyStarted4 = prefs.getBoolean(getString(R.string.firstlogin2), false);
                if (!previouslyStarted4) {
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean(getString(R.string.firstlogin2), Boolean.TRUE);
                    edit.apply();
                    storehelper s = new storehelper();
                    s.updateappscore(getApplicationContext());
                    s.updateSQL(getApplicationContext());
                    s.updatestorescore(new Score().getTotal_score());
                }
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

                mUser.updateProfile(profileUpdates).addOnCompleteListener(task2 -> {
                            if (task.isSuccessful()) { }
                });
                if(credential!=null)
                    linkCredential(credential);
                else
                    callDialog(credential);
            }else{
                Toast.makeText(getApplicationContext(),"Registration Failed!",Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    //Facebook SignIn

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user!=null) {
            callDialog(user);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean previouslyStarted3 = prefs.getBoolean(getString(R.string.firstlogin1), false);
            if (!previouslyStarted3) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(getString(R.string.firstlogin1), Boolean.TRUE);
                edit.apply();

                storehelper s = new storehelper();
                s.updateappscore(getApplicationContext());
                s.updateSQL(getApplicationContext());
                s.updatestorescore(new Score().getTotal_score());
            }
        }
    }


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        if(mUser!=null)
            linkCredential(credential);
        else
            callDialog(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();
                if(user!=null) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    boolean previouslyStarted4 = prefs.getBoolean(getString(R.string.firstlogin2), false);
                    if (!previouslyStarted4) {
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putBoolean(getString(R.string.firstlogin2), Boolean.TRUE);
                        edit.apply();
                        storehelper s = new storehelper();
                        s.updateappscore(getApplicationContext());
                        s.updateSQL(getApplicationContext());
                        s.updatestorescore(new Score().getTotal_score());
                    }
                    if(user.getDisplayName()==null || user.getPhoneNumber()==null || user.getEmail()==null)
                        callDialog(credential);
                    else
                        startActivity(new Intent(getApplicationContext(), Home.class));
                }
            } else {
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(getApplicationContext(),"Invalid Code", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks,
                token);
    }

    private void view(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    private void updateUI2(int uiState) {
        updateUI2(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI2(int uiState, PhoneAuthCredential cred) {
        updateUI2(uiState, null, cred);
    }

    private void updateUI2 (int uiState, FirebaseUser user, PhoneAuthCredential cred) {

        switch (uiState) {
            case STATE_CODE_SENT:

                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    progressDialog.dismiss();
                    custom_dialog.show();
                }, 10000);
                break;
            case STATE_VERIFY_FAILED:
                custom_dialog.show();
                break;
            case STATE_VERIFY_SUCCESS:

                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        smscode = cred.getSmsCode();
                    }
                }
                break;
        }
    }


    void callDialog(PhoneAuthCredential credential) {

        Dialog dialog=new Dialog(this,R.style.AppTheme);
        dialog.setContentView(R.layout.activity_sign_up_details);
        dialog.show();

        EditText named = dialog.findViewById(R.id.input_named);
        EditText emaild = dialog.findViewById(R.id.input_emaild);
        EditText phoned = dialog.findViewById(R.id.input_passwordd);
        Button proceedd = dialog.findViewById(R.id.btn_signupd);
        Button skipd = dialog.findViewById(R.id.skipd);


        if(mUser==null)
            mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser.getDisplayName()!=null && mUser.getPhoneNumber()!=null && mUser.getEmail()!=null)
            startActivity(new Intent(getApplicationContext(), Home.class));

        if(mUser.getDisplayName()!=null) {
            named.setVisibility(View.GONE);
            findViewById(R.id.input_layout_named).setVisibility(View.GONE);
        }

        if(mUser.getPhoneNumber()!=null) {
            phoned.setVisibility(View.GONE);
            findViewById(R.id.input_layout_passwordd).setVisibility(View.GONE);
        }

        if(mUser.getEmail()!=null) {
            emaild.setVisibility(View.GONE);
            findViewById(R.id.input_layout_emaild).setVisibility(View.GONE);
        }

        phoned.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith(" +91 ")) {
                    if(s.toString().equals(" +91"))
                        phoned.setText("");
                    else
                        phoned.setText(" +91 " + s);
                    Selection.setSelection(phoned.getText(), phoned.getText().length());
                }
            }
        });

        proceedd.setOnClickListener(v -> {

            if (named.getVisibility()!=View.GONE && (named.getText()==null || named.getText().toString().equals("")))
                named.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            else if (emaild.getVisibility()!=View.GONE && (emaild.getText()==null || !isValidEmail(emaild.getText().toString())))
                emaild.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            else if (phoned.getVisibility()!=View.GONE && (phoned.getText()==null || phoned.getText().toString().length()<15))
                phoned.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            else {
                if(credential==null)
                    setName_phone(named.getText().toString(),phoned.getText().toString());
                else
                    registerUser(emaild.getText().toString(),random(),named.getText().toString(), credential);
            }
        });

        skipd.setOnClickListener(view -> {
            if(mUser==null && credential!=null)
                signInWithPhoneAuthCredential(credential);
            else
                startActivity(new Intent(getApplicationContext(), Home.class));
        });

    }

    void callDialog(FirebaseUser user) {

        Dialog dialog=new Dialog(this,R.style.AppTheme);
        dialog.setContentView(R.layout.activity_sign_up_details);
        dialog.show();

        EditText named = dialog.findViewById(R.id.input_named);
        EditText emaild = dialog.findViewById(R.id.input_emaild);
        EditText phoned = dialog.findViewById(R.id.input_passwordd);
        Button proceedd = dialog.findViewById(R.id.btn_signupd);
        Button skipd = dialog.findViewById(R.id.skipd);

        if(mUser==null)
            mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser.getDisplayName()!=null && mUser.getPhoneNumber()!=null && mUser.getEmail()!=null)
            startActivity(new Intent(getApplicationContext(), Home.class));

        if(mUser.getDisplayName()!=null) {
            named.setVisibility(View.GONE);
            dialog.findViewById(R.id.input_layout_named).setVisibility(View.GONE);
        }

        if(mUser.getPhoneNumber()!=null) {
            phoned.setVisibility(View.GONE);
            dialog.findViewById(R.id.input_layout_passwordd).setVisibility(View.GONE);
        }

        if(mUser.getEmail()!=null) {
            emaild.setVisibility(View.GONE);
            dialog.findViewById(R.id.input_layout_emaild).setVisibility(View.GONE);
        }

        phoned.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith(" +91 ")) {
                    if(s.toString().equals(" +91"))
                        phoned.setText("");
                    else
                        phoned.setText(" +91 " + s);
                    Selection.setSelection(phoned.getText(), phoned.getText().length());
                }
            }
        });

        proceedd.setOnClickListener(v -> {

            if (named.getVisibility()!=View.GONE && (named.getText()==null || named.getText().toString().equals("")))
                named.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            else if (emaild.getVisibility()!=View.GONE && (emaild.getText()==null || !isValidEmail(emaild.getText().toString())))
                emaild.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            else if (phoned.getVisibility()!=View.GONE && (phoned.getText()==null || phoned.getText().toString().length()<15))
                phoned.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            else
                if(user!=null)
                    setName_phone(named.getText().toString(),phoned.getText().toString());
        });

        skipd.setOnClickListener(view ->  startActivity(new Intent(getApplicationContext(), Home.class)));

    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public void linkCredential(AuthCredential credential) {
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        mUser = task.getResult().getUser();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                    }
                });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public void setName_phone(String name, String phone) {

        if(!name.isEmpty()) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            mUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("ff", "User profile updated.");
                        }
                    });
        }

        if(!phone.isEmpty()) {
            phone = "+91" + phone.substring(5,15);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone,
                    60,
                    TimeUnit.SECONDS,
                    this,
                    mCallbacks);
            progressDialog.show();
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() == 1) {
            if (editText_one.length() == 1) {
                editText_two.requestFocus();
            }

            if (editText_two.length() == 1) {
                editText_three.requestFocus();
            }
            if (editText_three.length() == 1) {
                editText_four.requestFocus();
            }
            if (editText_four.length() == 1) {
                editText_five.requestFocus();
            }
            if (editText_five.length() == 1) {
                editText_six.requestFocus();
            }
            if (editText_six.length() == 1) {
                    smscode = editText_one.getText().toString() + editText_two.getText().toString() + editText_three.getText().toString() + editText_four.getText().toString()
                            + editText_five.getText().toString() + editText_six.getText().toString();
            }
        } else if (editable.length() == 0) {
            if (editText_six.length() == 0) {
                editText_five.requestFocus();
            }
            if (editText_five.length() == 0) {
                editText_four.requestFocus();
            }
            if (editText_four.length() == 0) {
                editText_three.requestFocus();
            }
            if (editText_three.length() == 0) {
                editText_two.requestFocus();
            }
            if (editText_two.length() == 0) {
                editText_one.requestFocus();
            }

        }
    }

}
