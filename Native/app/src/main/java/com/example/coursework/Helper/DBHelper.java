package com.example.coursework.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.coursework.Model.ClassModel;
import com.example.coursework.Model.CourseModel;
import com.example.coursework.Model.RequestModel;
import com.example.coursework.Model.TeacherModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    // Table Setting Creation
    public static final String TABLE_SETTING = "setting";
    public static final String COLUMN_SETTING_USERNAME = "username";
    public static final String COLUMN_SETTING_PASSWORD = "password";
    public static final String COLUMN_SETTING_SAVED_PASSWORD = "save_password";
    public static final String COLUMN_SETTING_BASE_URL = "url";
    // Table Teacher Creation
    public static final String TABLE_TEACHERS = "teachers";
    public static final String COLUMN_TEACHER_ID = "teacher_id";
    public static final String COLUMN_TEACHER_NAME = "teacher_name";
    public static final String COLUMN_TEACHER_EMAIL = "teacher_email";
    public static final String COLUMN_TEACHER_PHONE = "teacher_phone";
    public static final String COLUMN_TEACHER_IS_DELETE = "teacher_is_delete";
    // Table Course Creation
    public static final String TABLE_COURSES = "courses";
    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_COURSE_CODE = "course_code";
    public static final String COLUMN_COURSE_DOW = "course_dow";
    public static final String COLUMN_COURSE_DURATION = "course_duration";
    public static final String COLUMN_COURSE_CAPACITY = "course_capacity";
    public static final String COLUMN_COURSE_TYPE_OF_CLASS = "course_type_of_class";
    public static final String COLUMN_COURSE_DIFFICULTY = "course_difficulty";
    public static final String COLUMN_COURSE_TIME_OF_COURSE = "course_time_of_course";
    public static final String COLUMN_COURSE_DESCRIPTION = "course_description";
    public static final String COLUMN_COURSE_NEED_EQUIPMENT = "course_need_equipment";
    public static final String COLUMN_COURSE_PRICE_PER_CLASS = "course_price_per_class";
    public static final String COLUMN_COURSE_IS_DELETE = "course_is_delete";
    // Table Class Creation
    public static final String TABLE_CLASSES = "classes";
    public static final String COLUMN_CLASS_ID = "class_id";
    public static final String COLUMN_CLASS_NAME = "class_name";
    public static final String COLUMN_CLASS_TID = "teacher_id";
    public static final String COLUMN_CLASS_CID = "course_id";
    public static final String COLUMN_CLASS_DOC = "date_of_class";
    public static final String COLUMN_CLASS_COMMENTS = "comments";
    public static final String COLUMN_CLASS_IS_DELETE = "class_is_delete";
    // Declare Variables
    private static final String DATABASE_NAME = "yogaClass.db";
    private static final int DATABASE_VERSION = 1;

    // declare required variables
    APIHelper apiHelper;
    Helper helper;
    Context context;
    private String res;
    private String query;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Setting Table
        query = "CREATE TABLE IF NOT EXISTS " + TABLE_SETTING +
                "(" +
                COLUMN_SETTING_USERNAME + " TEXT, " +
                COLUMN_SETTING_PASSWORD + " TEXT, " +
                COLUMN_SETTING_SAVED_PASSWORD + " INT," +
                COLUMN_SETTING_BASE_URL + " TEXT " +
                ")";
        db.execSQL(query);

        // Insert default admin account
        ContentValues values = new ContentValues();
        values.put(COLUMN_SETTING_USERNAME, "admin");
        values.put(COLUMN_SETTING_PASSWORD, Helper.getMD5Hash("0000"));
        values.put(COLUMN_SETTING_SAVED_PASSWORD, 0); // password unsaved
        db.insert(TABLE_SETTING, null, values);

        // Create Teacher Table
        query = "CREATE TABLE IF NOT EXISTS " + TABLE_TEACHERS +
                "(" +
                COLUMN_TEACHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_TEACHER_NAME + " TEXT, " +
                COLUMN_TEACHER_EMAIL + " TEXT," +
                COLUMN_TEACHER_PHONE + " TEXT, " +
                COLUMN_TEACHER_IS_DELETE + " INTEGER " +
                ")";
        db.execSQL(query);

        // Create Course Table
        query = "CREATE TABLE IF NOT EXISTS " + TABLE_COURSES +
                "(" +
                COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_COURSE_CODE + " TEXT, " +
                COLUMN_COURSE_DOW + " TEXT, " +
                COLUMN_COURSE_DURATION + " INTEGER," +
                COLUMN_COURSE_CAPACITY + " INTEGER, " +
                COLUMN_COURSE_TYPE_OF_CLASS + " TEXT, " +
                COLUMN_COURSE_DIFFICULTY + " TEXT, " +
                COLUMN_COURSE_TIME_OF_COURSE + " TEXT, " +
                COLUMN_COURSE_DESCRIPTION + " TEXT, " +
                COLUMN_COURSE_NEED_EQUIPMENT + " TEXT, " +
                COLUMN_COURSE_PRICE_PER_CLASS + " REAL, " +
                COLUMN_COURSE_IS_DELETE + " INTEGER " +
                ")";
        db.execSQL(query);

        // Create Class Table
        query = "CREATE TABLE IF NOT EXISTS " + TABLE_CLASSES +
                "(" +
                COLUMN_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_CLASS_NAME + " TEXT," +
                COLUMN_CLASS_CID + " INTEGER, " +
                COLUMN_CLASS_TID + " INTEGER," +
                COLUMN_CLASS_DOC + " INTEGER, " +
                COLUMN_CLASS_COMMENTS + " TEXT, " +
                COLUMN_CLASS_IS_DELETE + " INTEGER " +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        query = "DROP TABLE IF EXISTS " + TABLE_SETTING;
        db.execSQL(query);
        query = "DROP TABLE IF EXISTS " + TABLE_CLASSES;
        db.execSQL(query);
        query = "DROP TABLE IF EXISTS " + TABLE_COURSES;
        db.execSQL(query);
        query = "DROP TABLE IF EXISTS " + TABLE_TEACHERS;
        db.execSQL(query);
        onCreate(db);
    }

    // get url to connect with cloud
    public String getBaseURL() {
        SQLiteDatabase db = this.getReadableDatabase();
        res = "";
        query = "Select " + COLUMN_SETTING_BASE_URL + " From " + TABLE_SETTING;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            res = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SETTING_BASE_URL));
        }
        cursor.close();
        db.close();
        return res;
    }

    // save base url for later use
    public void setBaseURL(String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SETTING_BASE_URL, url);
        db.update(TABLE_SETTING, values, null, null);
        db.close();

        apiHelper = new APIHelper(getBaseURL());

        RequestModel requestModel = new RequestModel();
        requestModel.setStoredProcedure(true);
        requestModel.setProcedureName("getConnection");
        apiHelper = new APIHelper(getBaseURL());
        apiHelper.postQuery(context, requestModel);
    }

    // Check Username and Password
    public int login(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        query = "SELECT COUNT(*) FROM " + TABLE_SETTING + " WHERE " + COLUMN_SETTING_USERNAME + " = '" + username +
                "' AND " + COLUMN_SETTING_PASSWORD + " = '" + Helper.getMD5Hash(password) + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    // Update admin password
    public void updatePassword(String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SETTING_PASSWORD, Helper.getMD5Hash(password));
        db.update(TABLE_SETTING, values, null, null);
    }

    /*
    CRUD about teacher table
    isUpdate 1, 0 helps to upload updated data to server.
    isDelete 1, 0 helps to control the data is deleted or not, can handel some error on class after deleting some teacher
    upload data to cloud, after CRUD operations
    */
    // add teacher
    public void addTeacher(TeacherModel teacherModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEACHER_NAME, teacherModel.getName());
        values.put(COLUMN_TEACHER_EMAIL, teacherModel.getEmail());
        values.put(COLUMN_TEACHER_PHONE, teacherModel.getPhoneNo());
        values.put(COLUMN_TEACHER_IS_DELETE, 0);
        db.insert(TABLE_TEACHERS, null, values);
        db.close();
        updateToServer(TABLE_TEACHERS);
    }

    // Select teachers which are not deleted
    public ArrayList<TeacherModel> getTeacherList() {
        ArrayList<TeacherModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        query = "SELECT * FROM " + TABLE_TEACHERS + " WHERE " + COLUMN_TEACHER_IS_DELETE + " = 0";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            TeacherModel teacherModel = new TeacherModel();
            teacherModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_ID)));
            teacherModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_NAME)));
            teacherModel.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_EMAIL)));
            teacherModel.setPhoneNo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_PHONE)));
            list.add(teacherModel);
        }
        cursor.close();
        db.close();
        return list;
    }

    // Update teacher
    public void updateTeacher(TeacherModel teacherModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEACHER_NAME, teacherModel.getName());
        values.put(COLUMN_TEACHER_EMAIL, teacherModel.getEmail());
        values.put(COLUMN_TEACHER_PHONE, teacherModel.getPhoneNo());
        db.update(TABLE_TEACHERS, values, COLUMN_TEACHER_ID + " = " + teacherModel.getId(), null);
        db.close();

        updateToServer(TABLE_TEACHERS);
    }

    // Delete teacher, update isDelete control to 1 and not show in class create or update
    public void deleteTeacher(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TEACHER_IS_DELETE, 1);
            db.update(TABLE_TEACHERS, values, COLUMN_TEACHER_ID + " = " + ID, null);

            updateToServer(TABLE_TEACHERS);
        } catch (Exception ignored) {
        }
        db.close();
    }

    /*
    CRUD about course table
    isUpdate 1, 0 helps to upload updated data to server.
    isDelete 1, 0 helps to control the data is deleted or not, can handel some error on class after deleting some course
    upload data to cloud, after CRUD operations
     */
    // Add course
    public void addCourse(CourseModel courseModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_CODE, courseModel.getCourseCode());
        values.put(COLUMN_COURSE_DOW, courseModel.getDayOfWeek());
        values.put(COLUMN_COURSE_DURATION, courseModel.getDuration());
        values.put(COLUMN_COURSE_CAPACITY, courseModel.getCapacity());
        values.put(COLUMN_COURSE_TYPE_OF_CLASS, courseModel.getTypeOfClass());
        values.put(COLUMN_COURSE_DIFFICULTY, courseModel.getDifficulty());
        values.put(COLUMN_COURSE_TIME_OF_COURSE, courseModel.getTimeOfCourse());
        values.put(COLUMN_COURSE_DESCRIPTION, courseModel.getDescription());
        values.put(COLUMN_COURSE_NEED_EQUIPMENT, courseModel.getNeedEquipment());
        values.put(COLUMN_COURSE_PRICE_PER_CLASS, courseModel.getPricePerClass());
        values.put(COLUMN_COURSE_IS_DELETE, 0);
        db.insert(TABLE_COURSES, null, values);
        db.close();

        updateToServer(TABLE_COURSES);
    }

    // Select courses which are not deleted
    public ArrayList<CourseModel> getCourseList() {
        ArrayList<CourseModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        query = "SELECT * FROM " + TABLE_COURSES + " WHERE " + COLUMN_COURSE_IS_DELETE + " = 0";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            CourseModel courseModel = new CourseModel();
            courseModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID)));
            courseModel.setCourseCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_CODE)));
            courseModel.setDayOfWeek(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_DOW)));
            courseModel.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_DURATION)));
            courseModel.setCapacity(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_CAPACITY)));
            courseModel.setTypeOfClass(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_TYPE_OF_CLASS)));
            courseModel.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_DIFFICULTY)));
            courseModel.setTimeOfCourse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_TIME_OF_COURSE)));
            courseModel.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_DESCRIPTION)));
            courseModel.setNeedEquipment(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_NEED_EQUIPMENT)));
            courseModel.setPricePerClass(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_PRICE_PER_CLASS)));
            list.add(courseModel);
        }
        cursor.close();
        db.close();
        return list;
    }

    // Update course
    public void updateCourse(CourseModel courseModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_CODE, courseModel.getCourseCode());
        values.put(COLUMN_COURSE_DOW, courseModel.getDayOfWeek());
        values.put(COLUMN_COURSE_DURATION, courseModel.getDuration());
        values.put(COLUMN_COURSE_CAPACITY, courseModel.getCapacity());
        values.put(COLUMN_COURSE_TYPE_OF_CLASS, courseModel.getTypeOfClass());
        values.put(COLUMN_COURSE_DIFFICULTY, courseModel.getDifficulty());
        values.put(COLUMN_COURSE_TIME_OF_COURSE, courseModel.getTimeOfCourse());
        values.put(COLUMN_COURSE_DESCRIPTION, courseModel.getDescription());
        values.put(COLUMN_COURSE_NEED_EQUIPMENT, courseModel.getNeedEquipment());
        values.put(COLUMN_COURSE_PRICE_PER_CLASS, courseModel.getPricePerClass());
        db.update(TABLE_COURSES, values, COLUMN_COURSE_ID + " = " + courseModel.getId(), null);
        db.close();

        updateToServer(TABLE_COURSES);
    }

    // Delete teacher, update isDelete control to 1 and not show in class create or update
    public void deleteCourse(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_COURSE_IS_DELETE, 1);
            db.update(TABLE_COURSES, values, COLUMN_COURSE_ID + " = " + ID, null);

            updateToServer(TABLE_COURSES);
        } catch (Exception ignored) {
        }
        db.close();
    }

    /*
    CRUD about class table
    isUpdate 1, 0 helps to upload updated data to server.
    isDelete 1, 0 helps to control the data is deleted or not
    upload data to cloud, after CRUD operations
     */
    // Add class
    public void addClass(ClassModel classModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_NAME, classModel.getClassName());
        values.put(COLUMN_CLASS_CID, classModel.getCourseId());
        values.put(COLUMN_CLASS_TID, classModel.getTeacherId());
        values.put(COLUMN_CLASS_DOC, classModel.getDateOfClass());
        values.put(COLUMN_CLASS_COMMENTS, classModel.getAdditionalComments());
        values.put(COLUMN_CLASS_IS_DELETE, 0);
        db.insert(TABLE_CLASSES, null, values);
        db.close();

        updateToServer(TABLE_CLASSES);
    }

    // Select classes which are not deleted
    public ArrayList<ClassModel> getClassList(int courseID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ClassModel> list = new ArrayList<>();

        query = "SELECT C." + COLUMN_CLASS_ID +
                ", C." + COLUMN_CLASS_NAME +
                ", COU." + COLUMN_COURSE_CODE +
                ", T." + COLUMN_TEACHER_NAME +
                ", COU." + COLUMN_COURSE_TYPE_OF_CLASS +
                ", C." + COLUMN_CLASS_DOC +
                ", COU." + COLUMN_COURSE_TIME_OF_COURSE +
                ", COU." + COLUMN_COURSE_DOW +
                " FROM " + TABLE_CLASSES + " C" +
                " LEFT JOIN " + TABLE_COURSES + " COU" +
                " ON COU." + COLUMN_COURSE_ID + " = " + "C." + COLUMN_CLASS_CID +
                " LEFT JOIN " + TABLE_TEACHERS + " T" +
                " ON T." + COLUMN_TEACHER_ID + " = " + "C." + COLUMN_CLASS_TID +
                " WHERE C." + COLUMN_CLASS_IS_DELETE + " = 0" +
                " AND (COU." + COLUMN_COURSE_ID + " = " + courseID +
                " OR " + courseID + " = 0)";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            ClassModel classModel = new ClassModel();
            classModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLASS_ID)));
            classModel.setClassName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS_NAME)));
            classModel.setCourseCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_CODE)));
            classModel.setTeacherName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_NAME)));
            classModel.setCourseType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_TYPE_OF_CLASS)));
            classModel.setDateOfClass(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS_DOC)));
            classModel.setCourseTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_TIME_OF_COURSE)));
            classModel.setDayOfClass(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_DOW)));
            list.add(classModel);
        }
        cursor.close();
        db.close();
        return list;
    }

    // Update class
    public void updateClass(ClassModel classModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_NAME, classModel.getClassName());
        values.put(COLUMN_CLASS_CID, classModel.getCourseId());
        values.put(COLUMN_CLASS_TID, classModel.getTeacherId());
        values.put(COLUMN_CLASS_DOC, classModel.getDateOfClass());
        values.put(COLUMN_CLASS_COMMENTS, classModel.getAdditionalComments());
        db.update(TABLE_CLASSES, values, COLUMN_CLASS_ID + " = " + classModel.getId(), null);
        db.close();

        updateToServer(TABLE_CLASSES);
    }

    // delete class
    public void deleteClass(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CLASS_IS_DELETE, 1);
            db.update(TABLE_CLASSES, values, COLUMN_CLASS_ID + " = " + ID, null);
            updateToServer(TABLE_CLASSES);
        } catch (Exception ignored) {
        }
        db.close();
    }

    // get Class Detail
    public ClassModel getClass(int classID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ClassModel classModel = new ClassModel();

        query = "SELECT * FROM " + TABLE_CLASSES + " WHERE " + COLUMN_CLASS_ID + " = " + classID;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            classModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLASS_ID)));
            classModel.setClassName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS_NAME)));
            classModel.setTeacherId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLASS_TID)));
            classModel.setCourseId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLASS_CID)));
            classModel.setDateOfClass(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS_DOC)));
            classModel.setAdditionalComments(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS_COMMENTS)));
        }
        cursor.close();

        db.close();
        return classModel;
    }

    // reset data in sqlite first, then reset data from cloud
    public void resetData(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(tableName, null, null);

            helper = new Helper();

            if (helper.checkNetworkStatus(context) == 0) {
                Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show();
                return;
            }
            resetTableData(tableName);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            db.close();
        }
    }

    /*
    need which table to upload data
    check network is connected or not
    if no connected to cloud, show no network is available
    if not,
    get all data from the desired table, and convert to json list and upload
    if there is no data in table, it can be locally deleted and not uploaded to cloud because of network connection, so reset data in cloud
    */
    // upload data to cloud
    public void updateToServer(String table) {

        helper = new Helper();

        if (helper.checkNetworkStatus(context) == 0) {
            Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            apiHelper = new APIHelper(getBaseURL());
            RequestModel requestModel = new RequestModel();
            requestModel.setStoredProcedure(true);
            requestModel.setProcedureName("getConnection");
            apiHelper = new APIHelper(getBaseURL());

            query = "SELECT * FROM " + table;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            List<Object> list = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    try {
                        jsonObject.put(cursor.getColumnName(i), cursor.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                list.add(jsonObject);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            if (list.size() == 0) {
                resetTableData(table);
                return;
            }
            requestModel = new RequestModel();
            requestModel.setStoredProcedure(true);
            requestModel.setProcedureName(table + "UploadData");
            requestModel.setParameters(list);
            apiHelper = new APIHelper(getBaseURL());
            apiHelper.postQuery(context, requestModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // reset data in cloud
    private void resetTableData(String tableName) {
        try {
            apiHelper = new APIHelper(getBaseURL());
            RequestModel requestModel = new RequestModel();
            requestModel.setStoredProcedure(true);
            requestModel.setProcedureName("getConnection");
            apiHelper = new APIHelper(getBaseURL());

            requestModel = new RequestModel();
            requestModel.setStoredProcedure(true);
            requestModel.setProcedureName(tableName + "ResetData");
            apiHelper = new APIHelper(getBaseURL());
            apiHelper.postQuery(context, requestModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
