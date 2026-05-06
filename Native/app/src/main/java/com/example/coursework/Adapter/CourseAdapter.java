package com.example.coursework.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.Model.CourseModel;
import com.example.coursework.R;
import com.example.coursework.Service.CourseService;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.myViewHolder> {

    private final CourseService listener;
    Context context;
    ArrayList<CourseModel> list;

    // Constructor
    public CourseAdapter(Context context, ArrayList<CourseModel> list, CourseService listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_course_view, parent, false);
        return new myViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        // Bind data to the views
        holder.tvName.setText("Course Code : " + list.get(position).getCourseCode());
        holder.tvDOW.setText("Course Day : " + list.get(position).getDayOfWeek());
        holder.tvTOC.setText("Course Time : " + list.get(position).getTypeOfClass());
        holder.tvDifficulty.setText("Course Difficulty : " + list.get(position).getDifficulty());

        // pass values to methods from parent for detail view, edit or delete action
        holder.itemView.setOnClickListener(view -> { // pass course model for detail view
            CourseModel courseModel = new CourseModel();
            courseModel.setId(list.get(position).getId());
            courseModel.setCourseCode(list.get(position).getCourseCode());
            courseModel.setDayOfWeek(list.get(position).getDayOfWeek());
            courseModel.setDuration(list.get(position).getDuration());
            courseModel.setCapacity(list.get(position).getCapacity());
            courseModel.setTypeOfClass(list.get(position).getTypeOfClass());
            courseModel.setDifficulty(list.get(position).getDifficulty());
            courseModel.setTimeOfCourse(list.get(position).getTimeOfCourse());
            courseModel.setDescription(list.get(position).getDescription());
            courseModel.setNeedEquipment(list.get(position).getNeedEquipment());
            courseModel.setPricePerClass(list.get(position).getPricePerClass());
            listener.onItemClickCourseDetail(courseModel);
        });

        holder.btnDelete.setOnClickListener(view -> {
            listener.onItemClickCourseDelete(list.get(position).getId(), list.get(position).getCourseCode());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvDOW, tvTOC, tvDifficulty;
        Button btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_course_name);
            tvTOC = itemView.findViewById(R.id.tv_course_type);
            tvDOW = itemView.findViewById(R.id.tv_course_dow);
            tvDifficulty = itemView.findViewById(R.id.tv_course_difficulty);
            btnDelete = itemView.findViewById(R.id.btn_course_delete);
        }
    }
}
