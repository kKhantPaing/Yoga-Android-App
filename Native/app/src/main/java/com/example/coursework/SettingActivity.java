package com.example.coursework;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coursework.Helper.DBHelper;

public class SettingActivity extends AppCompatActivity {

    // declare variable
    TextView tvUpload, tvReset;
    CheckBox cbUploadTeacher, cbUploadCourse, cbUploadClass, cbResetTeacher, cbResetCourse, cbResetClass;
    Toolbar toolbar;
    Button btnUpload, btnReset, btnConnect, btnChangePassword;
    EditText etUrl, etPort, etPassword;

    DBHelper dbHelper;
    private int countReset = 0, countUpload = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // for back button action
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back_24);

        // init variables
        dbHelper = new DBHelper(this);

        etUrl = findViewById(R.id.et_setting_url);
        etPort = findViewById(R.id.et_setting_port);
        etPassword = findViewById(R.id.et_setting_password);

        tvUpload = findViewById(R.id.tv_setting_upload);
        tvReset = findViewById(R.id.tv_setting_reset);

        btnConnect = findViewById(R.id.btn_setting_connect);
        btnReset = findViewById(R.id.btn_setting_reset);
        btnUpload = findViewById(R.id.btn_setting_upload);
        btnChangePassword = findViewById(R.id.btn_setting_change_password);

        cbUploadTeacher = findViewById(R.id.cb_setting_upload_teacher);
        cbUploadCourse = findViewById(R.id.cb_setting_upload_course);
        cbUploadClass = findViewById(R.id.cb_setting_upload_class);
        cbResetTeacher = findViewById(R.id.cb_setting_reset_teacher);
        cbResetCourse = findViewById(R.id.cb_setting_reset_course);
        cbResetClass = findViewById(R.id.cb_setting_reset_class);

        // split ip and port
        if (!TextUtils.isEmpty(dbHelper.getBaseURL())) {
            String[] url = dbHelper.getBaseURL().split(":");
            etUrl.setText(url[0]);
            etPort.setText(url[1]);
        }

        // change admin password
        btnChangePassword.setOnClickListener(view -> {
            if (TextUtils.isEmpty(etPassword.getText().toString())) {
                etPassword.setError("Invalid Password");
            } else {
                dbHelper.updatePassword(etPassword.getText().toString());
                etPassword.setError(null);
                Toast.makeText(this, "Password successfully changed!", Toast.LENGTH_SHORT).show();
                etPassword.setText("");
                etPassword.clearFocus();
            }
        });

        // check connection with server, save url for later use
        btnConnect.setOnClickListener(view -> {
            boolean flag = false;
            if (TextUtils.isEmpty(etUrl.getText().toString())) {
                etUrl.setError("Invalid URL!");
                flag = true;
            } else {
                etUrl.setError(null);
            }

            if (TextUtils.isEmpty(etPort.getText().toString())) {
                etPort.setError("Invalid Port!");
                flag = true;
            } else {
                etPort.setError(null);
            }

            if (!flag) {
                try {
                    dbHelper.setBaseURL(etUrl.getText().toString().trim() + ":" + etPort.getText().toString().trim());
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // upload data to cloud
        btnUpload.setOnClickListener(view -> {
            if (cbUploadCourse.isChecked())
                dbHelper.updateToServer(DBHelper.TABLE_COURSES);
            if (cbUploadTeacher.isChecked())
                dbHelper.updateToServer(DBHelper.TABLE_TEACHERS);
            if (cbUploadClass.isChecked())
                dbHelper.updateToServer(DBHelper.TABLE_CLASSES);
        });

        // reset data from both local and cloud
        btnReset.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure want to reset table(s)?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id1) -> {
                        if (cbResetClass.isChecked())
                            dbHelper.resetData(DBHelper.TABLE_CLASSES);
                        if (cbResetCourse.isChecked())
                            dbHelper.resetData(DBHelper.TABLE_COURSES);
                        if (cbResetTeacher.isChecked())
                            dbHelper.resetData(DBHelper.TABLE_TEACHERS);
                    })
                    .setNegativeButton("No", (dialog, id2) -> dialog.cancel()).show();
        });

        cbUploadTeacher.setOnCheckedChangeListener((compoundButton, b) -> {
            if (cbUploadTeacher.isChecked()) countUpload += 1;
            else countUpload -= 1;
            tvUpload.setText(countUpload + " Tables Selected");
        });

        cbUploadCourse.setOnCheckedChangeListener((compoundButton, b) -> {
            if (cbUploadCourse.isChecked()) countUpload += 1;
            else countUpload -= 1;
            tvUpload.setText(countUpload + " Tables Selected");
        });

        cbUploadClass.setOnCheckedChangeListener((compoundButton, b) -> {
            if (cbUploadClass.isChecked()) countUpload += 1;
            else countUpload -= 1;
            tvUpload.setText(countUpload + " Tables Selected");
        });

        cbResetClass.setOnCheckedChangeListener((compoundButton, b) -> {
            if (cbResetClass.isChecked()) countReset += 1;
            else countReset -= 1;
            tvReset.setText(countReset + " Tables Selected");
        });

        cbResetTeacher.setOnCheckedChangeListener((compoundButton, b) -> {
            if (cbResetTeacher.isChecked()) countReset += 1;
            else countReset -= 1;
            tvReset.setText(countReset + " Tables Selected");
        });

        cbResetCourse.setOnCheckedChangeListener((compoundButton, b) -> {
            if (cbResetCourse.isChecked()) countReset += 1;
            else countReset -= 1;
            tvReset.setText(countReset + " Tables Selected");
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) { // user logout, go to login activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}