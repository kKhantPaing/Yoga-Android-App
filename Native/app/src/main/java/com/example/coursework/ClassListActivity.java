package com.example.coursework;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.Adapter.ClassAdapter;
import com.example.coursework.Helper.DBHelper;
import com.example.coursework.Model.ClassModel;
import com.example.coursework.Service.ClassService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class ClassListActivity extends AppCompatActivity implements ClassService {

    // declare variables
    Toolbar toolbar;
    FloatingActionButton fab;
    DBHelper dbHelper;
    RecyclerView recyclerView;
    TextView tv_noList;

    ArrayList<ClassModel> list;
    ArrayList<ClassModel> filteredList;
    private ClassAdapter adapter;
    private String parentClass;
    private int courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_class_list);

        // init variable
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);

        parentClass = getIntent().getStringExtra("parent");
        courseID = getIntent().getIntExtra("courseID", 0);

        recyclerView = findViewById(R.id.recycler_class);
        tv_noList = findViewById(R.id.tv_class_noList);
        fab = findViewById(R.id.fab);

        list = new ArrayList<>();
        filteredList = new ArrayList<>();

        // for back button action
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back_24);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClassAdapter(this, filteredList, this);
        recyclerView.setAdapter(adapter);

        getData();

        /*
        If this page is called from course detail, only show classes created based on course id
        Floating button is not to show because it is view only and not to create new class
        */
        if (courseID > 0) {
            fab.setVisibility(View.GONE);
        }

        // call create class activity
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, ClassDetailActivity.class);
            intent.putExtra("edit", 1);
            startActivity(intent);
            finish();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getData() {
        // if there is no data, show No List view
        list = dbHelper.getClassList(courseID);
        if (!list.isEmpty()) {
            tv_noList.setVisibility(View.GONE);
        } else {
            tv_noList.setVisibility(View.VISIBLE);
        }
        // refresh recycler view
        getSupportActionBar().setTitle("Class List (" + list.size() + ")");

        // reset filter
        filteredList.clear();
        filteredList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    // call class detail activity, id for selected class id, edit 0 means view only
    @Override
    public void onItemClickClassDetail(int id) {
        Intent intent = new Intent(this, ClassDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("edit", 0);
        startActivity(intent);
        finish();
    }

    // call class detail activity, id for selected class id, edit 1 means to edit class, parent means which class is called from
    @Override
    public void onItemClickClassEdit(int id) {
        Intent intent = new Intent(this, ClassDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("edit", 1);
        intent.putExtra("parent", this.getLocalClassName());
        startActivity(intent);
        finish();
    }

    // delete class
    @Override
    public void onItemClickClassDelete(int id, String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to delete '" + name + "'?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id1) -> {
                    dbHelper.deleteClass(id);
                    getData();
                    Toast.makeText(this, "Successfully Deleted!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, id2) -> dialog.cancel()).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // filter class with teacher name
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        // call advance filter, date or day filter
        MenuItem filterItem = menu.findItem(R.id.action_filter);
        filterItem.setOnMenuItemClickListener(item -> {
            showFilterPopup();
            return true;
        });

        return true;
    }

    // class filter with teacher name
    @SuppressLint("NotifyDataSetChanged")
    private void filter(String text) {
        filteredList.clear();
        for (ClassModel data : list) {
            if (data.getTeacherName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(data);
            }
        }
        adapter.notifyDataSetChanged();
        getSupportActionBar().setTitle("Class List (" + filteredList.size() + ")");
    }

    // advance filter with teacher name, day or date
    private void applyFilter(String teacherName, String filterDate, String filterDay) {
        filteredList.clear();
        ArrayList<ClassModel> tmpList = new ArrayList<>();

        if (TextUtils.isEmpty(teacherName)) {
            tmpList.addAll(list);
        } else {
            for (ClassModel item : list) {
                if (item.getTeacherName().toLowerCase().contains(teacherName.toLowerCase())) {
                    tmpList.add(item);
                }
            }
        }

        if (filterDate != null) {
            for (ClassModel item : tmpList) {
                if (item.getDateOfClass().equals(filterDate)) {
                    filteredList.add(item);
                }
            }
        }
        if (filterDay != null) {
            for (ClassModel item : tmpList) {
                if (item.getDayOfClass().equals(filterDay)) {
                    filteredList.add(item);
                }
            }
        }

        if (filterDate == null && filterDay == null) {
            filteredList.addAll(tmpList);
        }

        adapter.notifyDataSetChanged();
        getSupportActionBar().setTitle("Class List (" + filteredList.size() + ")");
    }

    // Advance filter popup
    private void showFilterPopup() {
        View filterView = LayoutInflater.from(this).inflate(R.layout.popup_filter_options, null);
        PopupWindow popupWindow = new PopupWindow(filterView, Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        EditText etTeacherName = filterView.findViewById(R.id.et_teacher_name);
        Spinner spDay = filterView.findViewById(R.id.sp_day);
        RadioGroup radioGroup = filterView.findViewById(R.id.rg_filter_type);
        RadioButton rbDate = filterView.findViewById(R.id.radio_date);
        RadioButton rbDay = filterView.findViewById(R.id.radio_day);
        Button btnApplyFilter = filterView.findViewById(R.id.btn_apply_filter);
        Button btnApplyCancel = filterView.findViewById(R.id.btn_apply_clear);
        Button btnApplyDate = filterView.findViewById(R.id.btn_apply_date);

        btnApplyDate.setEnabled(false);
        spDay.setEnabled(true);

        // check day or date filter
        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            if (R.id.radio_date == i) {
                spDay.setEnabled(false);
                btnApplyDate.setEnabled(true);
            } else if (R.id.radio_day == i) {
                spDay.setEnabled(true);
                btnApplyDate.setEnabled(false);
            }
        });

        btnApplyFilter.setOnClickListener(v -> {
            String teacherName = etTeacherName.getText().toString();

            if (rbDate.isChecked()) {
                applyFilter(teacherName, btnApplyDate.getText().toString(), null);
            } else if (rbDay.isChecked()) {
                applyFilter(teacherName, null, spDay.getSelectedItem().toString());
            }
            popupWindow.dismiss();
        });

        btnApplyCancel.setOnClickListener(v -> {
            applyFilter(null, null, null);
            popupWindow.dismiss();
        });

        btnApplyDate.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (datePicker, y, m, d) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(y, m, d);
                        @SuppressLint("DefaultLocale") String formattedDate = String.format("%02d/%02d/%d", d, m + 1, y);
                        btnApplyDate.setText(formattedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(findViewById(R.id.toolbar), 0, 0);
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
        if (courseID == 0) {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}