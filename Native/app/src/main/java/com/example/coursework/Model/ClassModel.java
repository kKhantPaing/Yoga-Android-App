package com.example.coursework.Model;

public class ClassModel {
    int id, teacherId, courseId;
    String className, dateOfClass, additionalComments, teacherName, courseCode, courseTime, courseType, dayOfClass;
    boolean updateData;

    // Constructor
    public ClassModel() {
    }


    // Getters and Setters
    public String getClassName() {
        return className.trim();
    }

    public void setClassName(String className) {
        this.className = className.trim();
    }

    public String getTeacherName() {
        return teacherName.trim();
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName.trim();
    }

    public String getCourseCode() {
        return courseCode.trim();
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode.trim();
    }

    public String getCourseTime() {
        return courseTime.trim();
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime.trim();
    }

    public String getCourseType() {
        return courseType.trim();
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType.trim();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getDateOfClass() {
        return dateOfClass.trim();
    }

    public void setDateOfClass(String dateOfClass) {
        this.dateOfClass = dateOfClass.trim();
    }

    public String getDayOfClass() {
        return dayOfClass.trim();
    }

    public void setDayOfClass(String dayOfClass) {
        this.dayOfClass = dayOfClass.trim();
    }

    public String getAdditionalComments() {
        return additionalComments.trim();
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments.trim();
    }

    public boolean isUpdateData() {
        return updateData;
    }

    public void setUpdateData(boolean updateData) {
        this.updateData = updateData;
    }
}
