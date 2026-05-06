package com.example.coursework.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.Model.TeacherModel;
import com.example.coursework.R;
import com.example.coursework.Service.TeacherService;

import java.util.ArrayList;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.myViewHolder> {

    private final TeacherService listener;
    Context context;
    ArrayList<TeacherModel> list;

    // Constructor
    public TeacherAdapter(Context context, ArrayList<TeacherModel> list, TeacherService listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_teacher_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        // Bind data to the views
        holder.tvTeacherName.setText(list.get(position).getName());
        holder.tvTeacherPhone.setText(list.get(position).getPhoneNo());

        // pass values to methods from parent for edit or delete action
        holder.btnEdit.setOnClickListener(view -> {
            TeacherModel model = new TeacherModel();
            model.setId(list.get(position).getId());
            model.setName(list.get(position).getName());
            model.setEmail(list.get(position).getEmail());
            model.setPhoneNo(list.get(position).getPhoneNo());
            listener.onItemClickTeacherEdit(model);
        });

        holder.btnDelete.setOnClickListener(view -> {
            listener.onItemClickTeacherDelete(list.get(position).getId(), list.get(position).getName());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView tvTeacherName, tvTeacherPhone;
        Button btnEdit, btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTeacherName = itemView.findViewById(R.id.tv_teacher_name);
            tvTeacherPhone = itemView.findViewById(R.id.tv_teacher_phone);
            btnEdit = itemView.findViewById(R.id.btn_edit_teacher);
            btnDelete = itemView.findViewById(R.id.btn_delete_teacher);
        }
    }
}
