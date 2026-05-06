package com.example.coursework;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.Adapter.TeacherAdapter;
import com.example.coursework.Helper.DBHelper;
import com.example.coursework.Model.TeacherModel;
import com.example.coursework.Service.TeacherService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TeacherListActivity extends AppCompatActivity implements TeacherService {

    // declare variable
    Toolbar toolbar;
    FloatingActionButton fab;
    Dialog dialog;
    EditText etTeacherName, etTeacherEmail, etTeacherPhone;
    Button btnCancel, btnAdd;
    DBHelper dbHelper;
    ArrayList<TeacherModel> list;
    RecyclerView recyclerView;
    TextView tv_noList;
    private int teacherID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_list);

        // init variable
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);

        recyclerView = findViewById(R.id.recycler_teacher);
        tv_noList = findViewById(R.id.tv_teacher_noList);
        fab = findViewById(R.id.fab);

        list = new ArrayList<>();

        getData();

        // for back button action
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back_24);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Custom Layout Dialog from https://www.geeksforgeeks.org/how-to-create-dialog-with-custom-layout-in-android/
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_teacher);
        dialog.setCancelable(false);
        etTeacherName = dialog.findViewById(R.id.et_add_teacher_name);
        etTeacherEmail = dialog.findViewById(R.id.et_add_teacher_email);
        etTeacherPhone = dialog.findViewById(R.id.et_add_teacher_phone);
        btnAdd = dialog.findViewById(R.id.btn_add_teacher);
        btnCancel = dialog.findViewById(R.id.btn_cancel);

        teacherID = 0;

        // Action on Dialog Add button
        btnAdd.setOnClickListener(view -> {
            boolean flag = false;

            // check required fields
            if (TextUtils.isEmpty(etTeacherName.getText().toString())) {
                etTeacherName.setError("Name is required");
                flag = true;
            } else {
                etTeacherName.setError(null);
            }

            if (TextUtils.isEmpty(etTeacherEmail.getText().toString())) {
                etTeacherEmail.setError("Email is required");
                flag = true;
            } else {
                etTeacherEmail.setError(null);
            }

            if (TextUtils.isEmpty(etTeacherPhone.getText().toString())) {
                etTeacherPhone.setError("Phone is required");
                flag = true;
            } else {
                etTeacherPhone.setError(null);
            }

            // if some values are missing, do nothing
            if (flag)
                return;
            TeacherModel teacherModel = new TeacherModel();
            teacherModel.setName(etTeacherName.getText().toString());
            teacherModel.setEmail(etTeacherEmail.getText().toString());
            teacherModel.setPhoneNo(etTeacherPhone.getText().toString());
            try {
                // check create or update teacher
                if (teacherID > 0) {
                    teacherModel.setId(teacherID);
                    dbHelper.updateTeacher(teacherModel);
                    Toast.makeText(this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.addTeacher(teacherModel);
                    Toast.makeText(this, "Successfully Added!", Toast.LENGTH_SHORT).show();
                }
                teacherID = 0;
                etTeacherName.setText("");
                etTeacherName.setError(null);
                etTeacherEmail.setText("");
                etTeacherPhone.setText("");
                dialog.dismiss();
                getData();
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        // Action on Dialog Cancel button
        btnCancel.setOnClickListener(view -> {
            teacherID = 0;
            etTeacherName.setText("");
            etTeacherName.setError(null);
            etTeacherEmail.setText("");
            etTeacherPhone.setText("");
            dialog.dismiss();
        });

        // Action on floating action button to add a teacher
        fab.setOnClickListener(view -> {
            teacherID = 0;
            btnAdd.setText("Add");
            dialog.show();
        });
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

    // Get all teacher data
    private void getData() {
        list = dbHelper.getTeacherList();
        if (!list.isEmpty()) {
            tv_noList.setVisibility(View.GONE);
        } else {
            tv_noList.setVisibility(View.VISIBLE);
        }
        // refresh recycler view
        TeacherAdapter adapter = new TeacherAdapter(this, list, this);
        recyclerView.setAdapter(adapter);
        getSupportActionBar().setTitle("Teacher List (" + list.size() + ")");
    }

    @Override
    public void onItemClickTeacherEdit(TeacherModel model) {
        try {
            // update teacher
            btnAdd.setText("Update");
            teacherID = model.getId();
            etTeacherName.setText(model.getName());
            etTeacherEmail.setText(model.getEmail());
            etTeacherPhone.setText(model.getPhoneNo());
            dialog.show();
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClickTeacherDelete(int id, String name) {
        try {
            // ask user for confirmation
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure want to delete '" + name + "'?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id1) -> {
                        dbHelper.deleteTeacher(id);
                        Toast.makeText(TeacherListActivity.this, "Successfully Deleted!", Toast.LENGTH_SHORT).show();
                        getData();
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    // go back to previous activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}