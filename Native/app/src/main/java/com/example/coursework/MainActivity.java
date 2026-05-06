package com.example.coursework;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    // declare variables
    Button btnCourses, btnTeachers, btnClasses, btnSetting;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // add spinners with unit array
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnCourses = findViewById(R.id.btn_main_courses);
        btnTeachers = findViewById(R.id.btn_main_teachers);
        btnClasses = findViewById(R.id.btn_main_classes);
        btnSetting = findViewById(R.id.btn_main_setting);

        toolbar.setTitle("Welcome Back"); // Toolbar title

        // Call Course List Activity
        btnCourses.setOnClickListener(view -> {
            Intent intent = new Intent(this, CourseListActivity.class);
            startActivity(intent);
            finish();
        });

        // Call Teacher List Activity
        btnTeachers.setOnClickListener(view -> {
            Intent intent = new Intent(this, TeacherListActivity.class);
            startActivity(intent);
            finish();
        });

        // Call Class List Activity
        btnClasses.setOnClickListener(view -> {
            Intent intent = new Intent(this, ClassListActivity.class);
            startActivity(intent);
            finish();
        });

        // Call Setting Activity
        btnSetting.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Menu creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // menu items' actions
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) { // go to login activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}