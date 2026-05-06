package com.example.coursework.Service;

import com.example.coursework.Model.CourseModel;

public interface CourseService {
    void onItemClickCourseDetail(CourseModel model);

    void onItemClickCourseDelete(int id, String name);
}
