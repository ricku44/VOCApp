package com.example.greapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
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
import java.util.concurrent.TimeUnit;


public class Login extends AppCompatActivity implements TextWatcher {

    private FirebaseAuth mAuth;


    private static final String EMAIL = "email";
    private static final String TAG = "email";
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
    private ProgressDialog progressDoalog;



    private GoogleSignInClient mGoogleSignInClient;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    CallbackManager callbackManager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        SignInButton google = findViewById(R.id.signInButton);

        final Button login1 = findViewById(R.id.login1);
        final Button signup1 = findViewById(R.id.signin1);
        Button button2 = custom_dialog.findViewById(R.id.resend);
        Button otp = findViewById(R.id.OTP);
        LoginButton loginButton = findViewById(R.id.buttonFacebookLogin);

        final EditText phone = findViewById(R.id.ed1);


        mAuth = FirebaseAuth.getInstance();


        progressDoalog = new ProgressDialog(this);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progressDoalog.setTitle("Verifying ...");
        progressDoalog.setMessage("Verifying ...");
        progressDoalog.setIndeterminate(true);
        progressDoalog.setCanceledOnTouchOutside(false);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);

                updateUI2(STATE_VERIFY_SUCCESS, credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    phone.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
                updateUI2(STATE_VERIFY_FAILED);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                mResendToken = token;
                mVerificationId = verificationId;

                updateUI2(STATE_CODE_SENT);
            }
        };



        custom_dialog = new Dialog(this);
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

        if(smscode!=null){
            editText_one.setText(smscode.charAt(0));
            editText_two.setText(smscode.charAt(1));
            editText_three.setText(smscode.charAt(2));
            editText_four.setText(smscode.charAt(3));
            editText_five.setText(smscode.charAt(4));
            editText_six.setText(smscode.charAt(5));
        }


        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                        // ...
                    }

                });



        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });



        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup1.setBackground(getResources().getDrawable(R.drawable.textlines2));
                login1.setBackground(getResources().getDrawable(R.drawable.textlines));
                signup1.setTextColor(Color.parseColor("#B5E4E4E4"));
                login1.setTextColor(Color.WHITE);
                if(phone.getText().toString().equals(""))
                    phone.setHint(" Enter your mobile number");
            }
        });

        signup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup1.setBackground(getResources().getDrawable(R.drawable.textlines));
                login1.setBackground(getResources().getDrawable(R.drawable.textlines2));
                login1.setTextColor(Color.parseColor("#B5E4E4E4"));
                signup1.setTextColor(Color.WHITE);
                if(phone.getText().toString().equals(""))
                    phone.setHint(" Register your mobile number");
            }
        });


        phone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(phone.getText().toString().equals(""))
                    phone.setText(" +91  ");
                if(!phone.getText().toString().substring(0,6).equals(" +91  "))
                    phone.setText(" +91  ");
                return false;
            }
        });

        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = phone.getText().toString();

                if(phone.getText().toString().equals("")) {
                    phone.setError("Invalid phone number.");
                    phone.setText(" +91  ");
                }
                else if(number.equals(" +91  ")){
                    phone.setError("Invalid phone number.");
                }
                else if(!number.substring(0,6).equals(" +91  ")){
                    phone.setText(" +91  ");
                    phone.setError("Invalid phone number.");
                }
                else if(number.length()<16)
                    phone.setError("Invalid phone number.");
                else{
                    phoneNumber = "+91"+number.substring(6,16);
                    view();
                    progressDoalog.show();
                }

            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(phoneNumber, mResendToken);
            }
        });

        Button button1 = custom_dialog.findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPhoneNumberWithCode(mVerificationId, smscode);
            }
        });


    }



    //Google SignIn

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
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
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user!=null)
            startActivity(new Intent(getApplicationContext(),Home.class));

    }



    /***SMSMSMSMSMSMSMS***/

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            if(user!=null)
                                startActivity(new Intent(getApplicationContext(),Home.class));
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
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
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressDoalog.dismiss();
                        custom_dialog.show();
                    }
                }, 10000);
                break;
            case STATE_VERIFY_FAILED:
                custom_dialog.show();
                break;
            case STATE_VERIFY_SUCCESS:

                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        smscode = cred.getSmsCode();
                    } else {
                        //mVerificationField.setText(R.string.instant_validation);
                    }
                }

                break;

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
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }




    //TextWatcher Override

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
