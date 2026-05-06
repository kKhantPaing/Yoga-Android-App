package com.example.coursework1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class LogInActivity extends AppCompatActivity {

    Button btnLogin;
    EditText tvUserName, tvPassword;
    DBHelper dbHelper;

    String username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        tvUserName = findViewById(R.id.et_login_username);
        tvPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login_submit);
        dbHelper = new DBHelper(this);

        if(dbHelper.checkSavedLogin()){
            Intent indent = new Intent(this, MainActivity.class);
            startActivity(indent);
            finish();
        }

        btnLogin.setOnClickListener(view -> {
            username = tvUserName.getText().toString().trim();
            password = tvPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                Toast.makeText(this, "Missing Value found!", Toast.LENGTH_SHORT).show();
            }
            else{
                if (dbHelper.login(username, password) > 0){
                    Intent indent = new Intent(this, MainActivity.class);
                    startActivity(indent);
                    finish();
                }
                else {
                    Toast.makeText(this, "Incorrect username or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}