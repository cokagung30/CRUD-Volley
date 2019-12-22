package com.example.trainingcrudapi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trainingcrudapi.R;
import com.example.trainingcrudapi.interfaces.StudentClickListener;
import com.example.trainingcrudapi.model.Student;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private ArrayList<Student> students;
    private Context context;
    private StudentClickListener listener;

    public StudentAdapter(ArrayList<Student> students, Context context, StudentClickListener listener) {
        this.students = students;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.ViewHolder holder, final int position) {
        holder.tvNameStudent.setText(students.get(position).getName());
        holder.tvNimStudent.setText(students.get(position).getNim());
        holder.tvClassStudent.setText(students.get(position).getClasses());
        holder.cvStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(position, students);
            }
        });

        holder.cvStudent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.longClick(position, students);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameStudent, tvNimStudent, tvClassStudent;
        CardView cvStudent;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameStudent = itemView.findViewById(R.id.tv_name_student);
            tvClassStudent = itemView.findViewById(R.id.tv_class_student);
            tvNimStudent = itemView.findViewById(R.id.tv_nim_student);
            cvStudent = itemView.findViewById(R.id.cv_item_student);
        }
    }
}
