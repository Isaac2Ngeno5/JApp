package com.example.darkoscript.japp;

import android.content.Entity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.darkoscript.japp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "signupActivity";
    Spinner spinner;
    EditText edit_firstname, edit_lastname, edit_email, edit_phone, edit_password, edit_location, edit_Cpassword;
    Button btn_sign_up;
    RadioButton radio_male, radio_female;
    RadioGroup radioGroup;
    String gender;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //editText initialization
        edit_firstname = findViewById(R.id.edit_firstname);
        edit_lastname = findViewById(R.id.edit_lastname);
        edit_email = findViewById(R.id.edit_emailAddress);
        edit_phone = findViewById(R.id.edit_phone);
        edit_password = findViewById(R.id.edit_password);
        edit_Cpassword = findViewById(R.id.edit_Cpassword);
        edit_location = findViewById(R.id.edit_location);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        radio_male = findViewById(R.id.radio_male);
        radio_female = findViewById(R.id.radio_female);
        spinner = findViewById(R.id.spinner);
        radioGroup = findViewById(R.id.radio_group);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.counties_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtaining input values
                final String firstname = edit_firstname.getText().toString().trim();
                final String lastname = edit_lastname.getText().toString().trim();
                final String email = edit_email.getText().toString().trim();
                final String phone = edit_phone.getText().toString();
                String password = edit_password.getText().toString();
                String pass = edit_Cpassword.getText().toString();
                final String location = edit_location.getText().toString();
                final String county = spinner.getSelectedItem().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();


                if (selectedId == radio_male.getId()) {
                    gender = "Male";
                } else if (selectedId == radio_female.getId()) {
                    gender = "Female";
                }


                //validation of input values
                if (firstname.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "please input firstname", Toast.LENGTH_LONG).show();
                } else if (lastname.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "please input lastname", Toast.LENGTH_LONG).show();
                } else if (email.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "please enter email", Toast.LENGTH_LONG).show();
                } else if (phone.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "please enter phone number", Toast.LENGTH_LONG).show();
                } else if (pass.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "please enter password", Toast.LENGTH_LONG).show();
                } else if (location.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "please fill the location field", Toast.LENGTH_LONG).show();
                } else if (password.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "please ensure password is at least six characters", Toast.LENGTH_LONG).show();
                }


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    final FirebaseUser mUser = mAuth.getCurrentUser();
                                    // Add a new document with a generated ID
                                    User user = new User(mUser.getUid(), firstname, lastname, email, phone, county, location);
                                    db.collection("users")
                                            .document(user.getId())
                                            .set(user)
                                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        mUser.delete();
                                                        Toast.makeText(SignUpActivity.this, "Failed to register user", Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            });

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }


                            }
                        });

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
