package com.example.coursework;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coursework.Helper.DBHelper;
import com.example.coursework.Model.ClassModel;
import com.example.coursework.Model.CourseModel;
import com.example.coursework.Model.TeacherModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ClassDetailActivity extends AppCompatActivity {

    // Declare variables
    Toolbar toolbar;
    TextView tvTOC, tvClassTime, tvClassCapacity, tvDuration, tvPrice, tvDifficulty, tvNoData;
    EditText etClassName, etClassCmt;
    Button btnDate, btnCancel, btnAdd;
    Spinner spCourseCode, spTeacher;

    DBHelper dbHelper;
    ArrayList<ClassModel> list;
    ArrayList<CourseModel> courseList;
    ArrayList<TeacherModel> teacherList;

    List<String> courseCode;
    List<String> teacherName;

    List<String> dow;

    ClassModel classData;

    private int classID, courseID, tID, isEdit, dayOfWeek;
    private boolean isInit = true; // flag to control date when review the created class

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_class_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // for back button action
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back_24);

        // init variables
        dbHelper = new DBHelper(this);

        etClassName = findViewById(R.id.et_add_class_name);
        etClassCmt = findViewById(R.id.et_add_class_cmt);
        tvTOC = findViewById(R.id.tv_add_class_course_type);
        tvClassTime = findViewById(R.id.tv_add_class_time);
        tvClassCapacity = findViewById(R.id.tv_add_class_capacity);
        tvDuration = findViewById(R.id.tv_add_class_duration);
        tvPrice = findViewById(R.id.tv_add_class_price);
        tvDifficulty = findViewById(R.id.tv_add_class_difficulty);
        tvNoData = findViewById(R.id.tv_class_noData);
        btnAdd = findViewById(R.id.btn_add_class_add);
        btnCancel = findViewById(R.id.btn_add_class_cancel);
        btnDate = findViewById(R.id.btn_add_class_date);

        spCourseCode = findViewById(R.id.spinner_add_class_course_code);
        spTeacher = findViewById(R.id.spinner_add_class_teacher_id);

        classID = getIntent().getIntExtra("id", 0);
        isEdit = getIntent().getIntExtra("edit", 0);

        list = new ArrayList<>();
        courseList = new ArrayList<>();
        teacherList = new ArrayList<>();
        dow = new ArrayList<>();

        courseCode = new ArrayList<>();
        teacherName = new ArrayList<>();

        classData = new ClassModel();

        // get data from database
        getData();

        // Check teacher or course is created or not
        if (!courseList.isEmpty() && !teacherList.isEmpty()) {
            tvNoData.setVisibility(View.GONE);

            // change course data according to course code
            spCourseCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    courseID = courseList.get(i).getId();
                    tvTOC.setText(courseList.get(i).getTypeOfClass());
                    tvClassTime.setText(courseList.get(i).getDayOfWeek() + " (" + courseList.get(i).getTimeOfCourse() + ")");
                    tvClassCapacity.setText("Capacity : " + courseList.get(i).getCapacity());
                    tvDuration.setText("Duration : " + courseList.get(i).getDuration());
                    tvPrice.setText("Price (£) : " + courseList.get(i).getPricePerClass());
                    tvDifficulty.setText("Difficulty : " + courseList.get(i).getDifficulty());

                    dayOfWeek = dow.indexOf(courseList.get(i).getDayOfWeek());
                    if (!isInit)
                        btnDate.setText("");

                    isInit = false;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            // to store tmp teacher id for class creation
            spTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    tID = teacherList.get(i).getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            btnDate.setOnClickListener(view -> {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // to check if the selected date is the same as the course day
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        (datePicker, y, m, d) -> {
                            Calendar selectedDate = Calendar.getInstance();
                            selectedDate.set(y, m, d);

                            if (selectedDate.get(Calendar.DAY_OF_WEEK) == dayOfWeek + 1) {
                                @SuppressLint("DefaultLocale") String formattedDate = String.format("%02d/%02d/%d", d, m + 1, y);
                                btnDate.setText(formattedDate);
                            } else {
                                Toast.makeText(this, "Date must be " + dow.get(dayOfWeek), Toast.LENGTH_SHORT).show();
                                btnDate.setText("");
                            }
                        },
                        year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            });

            btnAdd.setOnClickListener(view -> {
                boolean flag = false;

                // check values is missing or not for required fields
                if (TextUtils.isEmpty(etClassName.getText())) {
                    etClassName.setError("Class name is required");
                    flag = true;
                } else
                    etClassName.setError(null);

                if (TextUtils.isEmpty(btnDate.getText())) {
                    btnDate.setError("Date is required");
                    flag = true;
                } else
                    btnDate.setError(null);

                // if missing value found, do noting
                if (flag)
                    return;

                // create or update class
                try {
                    ClassModel model = new ClassModel();
                    model.setId(classID);
                    model.setClassName(etClassName.getText().toString());
                    model.setCourseId(courseID);
                    model.setTeacherId(tID);
                    model.setDateOfClass(btnDate.getText().toString());
                    model.setAdditionalComments(etClassCmt.getText().toString());
                    if (classID > 0) // update class, classId is > 0 if class is created
                        dbHelper.updateClass(model);
                    else
                        dbHelper.addClass(model); // create class
                    onBackPressed();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            });

            btnCancel.setOnClickListener(view -> {
                onBackPressed();
            });

            bindData();

            if (classID > 0) {
                if (isEdit > 0)
                    getSupportActionBar().setTitle("Update Class");
                else
                    getSupportActionBar().setTitle("Class" + classData.getClassName());
                btnAdd.setText("Update");
                etClassName.setText(classData.getClassName());
                etClassCmt.setText(classData.getAdditionalComments());

                for (int ind = 0; ind < courseList.size(); ind++) {
                    if (courseList.get(ind).getId() == classData.getCourseId()) {
                        spCourseCode.setSelection(ind);
                        break;
                    }
                }
                btnDate.setText(classData.getDateOfClass());
            } else {
                getSupportActionBar().setTitle("Create Class");
                courseID = courseList.get(0).getId();
                tID = teacherList.get(0).getId();
                dayOfWeek = dow.indexOf(courseList.get(0).getDayOfWeek());
            }

            if (isEdit < 1) {
                etClassName.setFocusable(false);
                etClassCmt.setFocusable(false);
                btnDate.setClickable(false);
                btnCancel.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                spCourseCode.setEnabled(false);
                spTeacher.setEnabled(false);
            }
        }
    }

    // get data from database
    private void getData() {
        classData = dbHelper.getClass(classID);
        courseList = dbHelper.getCourseList();
        teacherList = dbHelper.getTeacherList();
    }

    // bind course and teacher data to spinner
    private void bindData() {
        dow = Arrays.asList(getResources().getStringArray(R.array.dayOfWeek));

        ArrayAdapter<CourseModel> adapterCourse = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseList);
        adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<TeacherModel> adapterTeacher = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teacherList);
        adapterTeacher.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCourseCode.setAdapter(adapterCourse);
        spTeacher.setAdapter(adapterTeacher);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ClassListActivity.class));
        finish();
    }
}