package com.example.coursework.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.Model.ClassModel;
import com.example.coursework.R;
import com.example.coursework.Service.ClassService;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.myViewHolder> {

    private final ClassService listener;
    Context context;
    ArrayList<ClassModel> list;

    // Constructor
    public ClassAdapter(Context context, ArrayList<ClassModel> list, ClassService listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClassAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_class_view, parent, false);
        return new myViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ClassAdapter.myViewHolder holder, int position) {
        // Bind data to the views
        holder.tvName.setText(list.get(position).getClassName() + "(" + list.get(position).getCourseCode() + ")");
        holder.tvTeacherName.setText(list.get(position).getTeacherName());
        holder.tvClassType.setText(list.get(position).getCourseType());
        holder.tvClassTime.setText(list.get(position).getDateOfClass() + " (" + list.get(position).getCourseTime() + ")");

        // pass values to methods from parent for detail view, edit or delete action
        holder.itemView.setOnClickListener(view -> {
            listener.onItemClickClassDetail(list.get(position).getId());
        });

        holder.btnEdit.setOnClickListener(view -> {
            listener.onItemClickClassEdit(list.get(position).getId());
        });

        holder.btnDelete.setOnClickListener(view -> {
            listener.onItemClickClassDelete(list.get(position).getId(), list.get(position).getCourseCode());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvTeacherName, tvClassType, tvClassTime;
        ImageButton btnEdit, btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_class_course_name);
            tvClassTime = itemView.findViewById(R.id.tv_class_time);
            tvClassType = itemView.findViewById(R.id.tv_class_course_type);
            tvTeacherName = itemView.findViewById(R.id.tv_class_teacher_name);
            btnEdit = itemView.findViewById(R.id.btn_class_edit);
            btnDelete = itemView.findViewById(R.id.btn_class_delete);
        }
    }
}
