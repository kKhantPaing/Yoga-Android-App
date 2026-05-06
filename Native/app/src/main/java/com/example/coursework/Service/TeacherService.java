package com.example.coursework.Service;

import com.example.coursework.Model.TeacherModel;

public interface TeacherService {
    void onItemClickTeacherEdit(TeacherModel model);

    void onItemClickTeacherDelete(int id, String name);
}
