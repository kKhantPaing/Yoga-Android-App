package com.example.coursework;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coursework.Helper.DBHelper;
import com.example.coursework.Helper.Helper;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText tvUserName, tvPassword;
    DBHelper dbHelper;
    CheckBox cbRememberMe;

    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        tvUserName = findViewById(R.id.et_login_username);
        tvPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login_submit);
        cbRememberMe = findViewById(R.id.cb_remember_me);

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
                tvPassword.setError("Password is required!");
            } else {
                tvPassword.setError(null);
            }

            // check username and password for login
            if (flag) {
                password = Helper.getMD5Hash(password);
                if (dbHelper.login(username, password) > 0) {

                    // Save only if Remember Me checked
                    if (cbRememberMe.isChecked()){
                        dbHelper.saveUser(username, password);
                    }
                    else {
                        dbHelper.saveUser("",""); // Clear saved Username and Password
                    }

                    Intent indent = new Intent(this, MainActivity.class);
                    startActivity(indent);
                    finish();
                } else {
                    Toast.makeText(this, "Incorrect username or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Auto Login if Remember Me is checked
        HashMap<String, String> tmp = dbHelper.getSavedUser();
        username = tmp.get("username");
        password = tmp.get("password");

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
            if (dbHelper.login(username, password) > 0) {
                Intent indent = new Intent(this, MainActivity.class);
                startActivity(indent);
                finish();
            }
        }
    }
}