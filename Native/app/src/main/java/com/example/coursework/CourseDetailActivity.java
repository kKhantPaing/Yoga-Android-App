package com.example.coursework;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coursework.Helper.DBHelper;
import com.example.coursework.Model.CourseModel;

import java.util.Calendar;

public class CourseDetailActivity extends AppCompatActivity {

    // declare variables
    EditText etName, etDesc, etNeedEquipment, etPrice, etDuration, etCapacity;
    Button btnTime, btnAdd, btnCancel, btnShowClass;
    Spinner spDOW, spTOC, spLvl;
    Toolbar toolbar;
    View vwHrLine;

    int cID;

    DBHelper dbHelper;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // for back button action
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back_24);


        // init variables
        dbHelper = new DBHelper(this);

        etName = findViewById(R.id.et_add_course_name);
        etDesc = findViewById(R.id.et_add_course_desc);
        etNeedEquipment = findViewById(R.id.et_add_course_equipment);
        etPrice = findViewById(R.id.et_add_course_price_per_class);
        etDuration = findViewById(R.id.et_add_course_duration);
        etCapacity = findViewById(R.id.et_add_course_capacity);
        btnAdd = findViewById(R.id.btn_add_course_add);
        btnCancel = findViewById(R.id.btn_add_course_cancel);
        btnTime = findViewById(R.id.btn_add_course_time);
        btnShowClass = findViewById(R.id.btn_add_course_show_classes);
        spDOW = findViewById(R.id.spinner_add_course_dow);
        spTOC = findViewById(R.id.spinner_add_course_class_type);
        spLvl = findViewById(R.id.spinner_add_course_level);
        vwHrLine = findViewById(R.id.vw_add_course_hrLine);

        // add spinners with Day of Week array
        ArrayAdapter<CharSequence> adapterDOW = ArrayAdapter.createFromResource(
                this,
                R.array.dayOfWeek,
                android.R.layout.simple_spinner_item
        );
        adapterDOW.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // add spinners with Difficulty array
        ArrayAdapter<CharSequence> adapterDifficulty = ArrayAdapter.createFromResource(
                this,
                R.array.difficulty,
                android.R.layout.simple_spinner_item
        );
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // add spinners with Type of Class array
        ArrayAdapter<CharSequence> adapterTOC = ArrayAdapter.createFromResource(
                this,
                R.array.typeOfClass,
                android.R.layout.simple_spinner_item
        );
        adapterTOC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spDOW.setAdapter(adapterDOW);
        spLvl.setAdapter(adapterDifficulty);
        spTOC.setAdapter(adapterTOC);

        cID = 0;

        builder = new AlertDialog.Builder(this);

        // Update or Create course upon model is passed or not from course list activity
        CourseModel model = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            model = getIntent().getSerializableExtra("course", CourseModel.class);
        }
        if (model != null) {
            // fill with selected task values
            cID = model.getId();
            etName.setText(model.getCourseCode());
            etDesc.setText(model.getDescription());
            etNeedEquipment.setText(model.getNeedEquipment());
            etPrice.setText(model.getPricePerClass());
            etDuration.setText(model.getDuration());
            etCapacity.setText(model.getCapacity());
            btnTime.setText(model.getTimeOfCourse());
            spDOW.setSelection(adapterDOW.getPosition(model.getDayOfWeek()));
            spLvl.setSelection(adapterDifficulty.getPosition(model.getDifficulty()));
            spTOC.setSelection(adapterTOC.getPosition(model.getTimeOfCourse()));
        }


        if (cID > 0) {
            btnAdd.setText("Update");
            getSupportActionBar().setTitle("Update Course");
        } else {
            btnAdd.setText("Add");
            getSupportActionBar().setTitle("Create Course");
            btnShowClass.setVisibility(View.GONE);
            vwHrLine.setVisibility(View.GONE);
        }

        // Time picker
        btnTime.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();

            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hh, int mm) {
                            btnTime.setText(String.format("%02d:%02d", hh, mm));
                        }
                    }, hour, minute, false);
            timePickerDialog.show();
        });

        // Create or Update course
        btnAdd.setOnClickListener(view -> {
            if (!validatingInput()) {
                try {
                    CourseModel courseModel = new CourseModel();
                    courseModel.setId(cID);
                    courseModel.setCourseCode(etName.getText().toString());
                    courseModel.setDayOfWeek(spDOW.getSelectedItem().toString());
                    courseModel.setDuration(etDuration.getText().toString());
                    courseModel.setCapacity(etCapacity.getText().toString());
                    courseModel.setTypeOfClass(spTOC.getSelectedItem().toString());
                    courseModel.setDifficulty(spLvl.getSelectedItem().toString());
                    courseModel.setTimeOfCourse(btnTime.getText().toString());
                    courseModel.setDescription(etDesc.getText().toString());
                    courseModel.setNeedEquipment(etNeedEquipment.getText().toString());
                    courseModel.setPricePerClass(etPrice.getText().toString());

                    String msg = "";

                    // if cId is greater 0, it is update course.
                    if (cID > 0) {
                        msg += "Are you sure want to update course with the following?";
                        msg += "\nCode: " + etName.getText().toString();
                        msg += "\n" + spTOC.getSelectedItem().toString();
                        msg += "\nClass Capacity: " + etCapacity.getText().toString() + "\nPrice(£): " + etPrice.getText().toString();
                        msg += "\nTime: On " + spDOW.getSelectedItem().toString() + " (" + btnTime.getText().toString() + ")";
                        msg += "\nClass Duration: " + etDuration.getText().toString() + " min";
                        builder.setMessage(msg)
                                .setCancelable(false)
                                .setPositiveButton("Yes", (dialog, id1) -> {
                                    dbHelper.updateCourse(courseModel); // update course
                                    Toast.makeText(this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(this, CourseListActivity.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .setNegativeButton("No", (dialog, id) -> dialog.cancel()).show();

                    } else {
                        msg += "Are you sure want to add course with the following?";
                        msg += "\nCode: " + etName.getText().toString();
                        msg += "\n" + spTOC.getSelectedItem().toString();
                        msg += "\nClass Capacity: " + etCapacity.getText().toString() + "\nPrice(£): " + etPrice.getText().toString();
                        msg += "\nTime: On " + spDOW.getSelectedItem().toString() + " (" + btnTime.getText().toString() + ")";
                        msg += "\nClass Duration: " + etDuration.getText().toString() + " min";
                        builder.setMessage(msg)
                                .setCancelable(false)
                                .setPositiveButton("Yes", (dialog, id1) -> {
                                    dbHelper.addCourse(courseModel); // create new course
                                    Toast.makeText(this, "Successfully Added!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(this, CourseListActivity.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .setNegativeButton("No", (dialog, id) -> dialog.cancel()).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(view -> {
            builder.setMessage("Are you sure want to Cancel?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id1) -> {
                        Intent intent = new Intent(this, CourseListActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel()).show();
        });

        /*
        call class list activity and passed with course id
        to view all the classes created from the selected course
        */
        btnShowClass.setOnClickListener(view -> {
            Intent intent = new Intent(this, ClassListActivity.class);
            intent.putExtra("parent", this.getLocalClassName());
            intent.putExtra("courseID", cID);
            startActivity(intent);
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    // Check input is valid or not, some fields are required for course creation or update
    private boolean validatingInput() {
        boolean flag = false;

        // Check course name is not missing
        if (TextUtils.isEmpty(etName.getText())) {
            etName.setError("Course name is required");
            flag = true;
        }

        // Check capacity is not missing and value is greater than 0
        if (TextUtils.isEmpty(etCapacity.getText())) {
            etCapacity.setError("Capacity is required");
            flag = true;
        } else if (Integer.parseInt(etCapacity.getText().toString()) < 1) {
            etCapacity.setError("Invalid capacity");
            flag = true;
        }

        // Check duration is not missing and value is greater than 0
        if (TextUtils.isEmpty(etDuration.getText())) {
            etDuration.setError("Duration is required");
            flag = true;
        } else if (Integer.parseInt(etDuration.getText().toString()) < 1) {
            etDuration.setError("Invalid duration");
            flag = true;
        }

        // Check price is not missing, price can be foc (meaning not charges)
        if (TextUtils.isEmpty(etPrice.getText())) {
            etPrice.setError("Price is required");
            flag = true;
        }

        return flag;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, CourseListActivity.class));
        finish();
    }
}