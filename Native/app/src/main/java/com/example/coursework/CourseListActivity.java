package com.example.coursework;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.Adapter.CourseAdapter;
import com.example.coursework.Helper.DBHelper;
import com.example.coursework.Model.CourseModel;
import com.example.coursework.Service.CourseService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CourseListActivity extends AppCompatActivity implements CourseService {

    // Declare variable
    Toolbar toolbar;
    FloatingActionButton fab;
    DBHelper dbHelper;
    RecyclerView recyclerView;
    TextView tv_noList;

    ArrayList<CourseModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);

        recyclerView = findViewById(R.id.recycler_course);
        tv_noList = findViewById(R.id.tv_course_noList);
        fab = findViewById(R.id.fab);

        list = new ArrayList<>();

        getData();

        // for back button action
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back_24);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, CourseDetailActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onItemClickCourseDetail(CourseModel model) {
        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.putExtra("course", model);
        startActivity(intent);
    }

    // get data from database
    private void getData() {
        list = dbHelper.getCourseList();

        // check if there is no data, show no list view
        if (!list.isEmpty()) {
            tv_noList.setVisibility(View.GONE);
        } else {
            tv_noList.setVisibility(View.VISIBLE);
        }
        // refresh recycler view
        CourseAdapter adapter = new CourseAdapter(this, list, this);
        recyclerView.setAdapter(adapter);
        getSupportActionBar().setTitle("Course List (" + list.size() + ")");
    }

    @Override
    public void onItemClickCourseDelete(int id, String name) {
        try {
            // ask user for confirmation
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure want to delete '" + name + "'?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id1) -> {
                        dbHelper.deleteCourse(id);
                        Toast.makeText(CourseListActivity.this, "Successfully Deleted!", Toast.LENGTH_SHORT).show();
                        getData();
                    })
                    .setNegativeButton("No", (dialog, id2) -> dialog.cancel()).show();
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

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
        if (item.getItemId() == R.id.menu_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}