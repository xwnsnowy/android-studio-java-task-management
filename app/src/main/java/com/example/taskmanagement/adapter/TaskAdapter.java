package com.example.taskmanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagement.R;
import com.example.taskmanagement.models.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> tasks;

    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bindData(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView taskName;
        private TextView taskDescription;
        private TextView taskState;
        private TextView taskDate;
        private TextView taskEstimatedDuration;
        private TextView taskProject;
        private ImageView taskIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task_name);
            taskDescription = itemView.findViewById(R.id.task_description);
            taskState = itemView.findViewById(R.id.task_state);
            taskDate = itemView.findViewById(R.id.task_date);
            taskEstimatedDuration = itemView.findViewById(R.id.task_estimated_duration);
            taskProject = itemView.findViewById(R.id.task_project);
            taskIcon = itemView.findViewById(R.id.task_icon);
            itemView.setOnClickListener(v ->
                    Toast.makeText(v.getContext(), taskName.getText(), Toast.LENGTH_SHORT).show()
            );
        }

        public void bindData(Task task) {
            taskName.setText(task.getName());
            taskDescription.setText(task.getDescription());
            taskState.setText(task.getStateTask().getStatue());
            taskDate.setText(task.getMaxEndDate());
            taskEstimatedDuration.setText(task.getEstimateDuration());
            taskProject.setText(task.getProjectName());
            // Set icon or mnemonic if needed
            // taskIcon.setImageResource(task.getMnemonic());
        }
    }
}
