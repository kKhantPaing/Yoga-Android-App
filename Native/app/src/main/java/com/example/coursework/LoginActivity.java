package com.example.coursework;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coursework.Helper.DBHelper;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText tvUserName, tvPassword;
    DBHelper dbHelper;

    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        tvUserName = findViewById(R.id.et_login_username);
        tvPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login_submit);

        dbHelper = new DBHelper(this);

        // check required fields
        btnLogin.setOnClickListener(view -> {
            username = tvUserName.getText().toString().trim();
            password = tvPassword.getText().toString().trim();
            boolean flag = true;
            if (TextUtils.isEmpty(username)) {
                flag = false;
                tvUserName.setError("Username is required!");
            } else {
                tvUserName.setError(null);
            }
            if (TextUtils.isEmpty(password)) {
                flag = false;
                tvPassword.setError("Username is required!");
            } else {
                tvPassword.setError(null);
            }

            // check username and password for login
            if (flag) {
                if (dbHelper.login(username, password) > 0) {
                    Intent indent = new Intent(this, MainActivity.class);
                    startActivity(indent);
                    finish();
                } else {
                    Toast.makeText(this, "Incorrect username or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}