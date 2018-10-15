package com.example.darkoscript.japp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Button btn_login, btn_register;
    EditText edit_email, edit_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Initialization of the editText fields
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        //Initialization of the buttons
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        //setting onclick listener for buttons
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_email.getText().toString().trim();
                String password = edit_password.getText().toString().trim();

                if(email.isEmpty()){
                    Toast.makeText(LoginActivity.this, "please enter email", Toast.LENGTH_LONG).show();
                }else if(password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "please enter password", Toast.LENGTH_LONG).show();
                }else if (!isValidEmail(email)){
                    Toast.makeText(LoginActivity.this, "please enter a valid email Adrdress", Toast.LENGTH_LONG).show();
                }else if (password.length() < 6){
                    Toast.makeText(LoginActivity.this, "Password should be longer than 6 characters", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isValidEmail(CharSequence target){
        return(!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
