package com.example.coursework.Model;

import java.io.Serializable;

public class CourseModel implements Serializable {
    int id;
    String courseCode, dayOfWeek, typeOfClass, difficulty, timeOfCourse, description, needEquipment, duration, capacity, pricePerClass;
    boolean updateData;

    // Constructor
    public CourseModel() {
    }

    // Getters and Setters
    public String toString() {
        return courseCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTypeOfClass() {
        return typeOfClass;
    }

    public void setTypeOfClass(String typeOfClass) {
        this.typeOfClass = typeOfClass;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTimeOfCourse() {
        return timeOfCourse;
    }

    public void setTimeOfCourse(String timeOfCourse) {
        this.timeOfCourse = timeOfCourse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNeedEquipment() {
        return needEquipment;
    }

    public void setNeedEquipment(String needEquipment) {
        this.needEquipment = needEquipment;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getPricePerClass() {
        return pricePerClass;
    }

    public void setPricePerClass(String pricePerClass) {
        this.pricePerClass = pricePerClass;
    }

    public boolean isUpdateData() {
        return updateData;
    }

    public void setUpdateData(boolean updateData) {
        this.updateData = updateData;
    }
}
