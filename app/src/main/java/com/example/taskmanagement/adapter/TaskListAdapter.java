package com.example.taskmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.taskmanagement.R;
import com.example.taskmanagement.activities.TaskDetailActivity;
import com.example.taskmanagement.models.Task;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
    private List<Task> tasks;

    public TaskListAdapter(List<Task> tasks) {
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
        private Task currentTask;  // Store current task here

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task_name);
            taskDescription = itemView.findViewById(R.id.task_description);
            taskState = itemView.findViewById(R.id.task_state);

            itemView.setOnClickListener(v -> {
                if (currentTask != null) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, TaskDetailActivity.class);
                    intent.putExtra("taskId", currentTask.getId());
                    context.startActivity(intent);
                }
            });
        }

        public void bindData(Task task) {
            currentTask = task;
            Context context = taskName.getContext();
            taskName.setText(context.getString(R.string.task_name_label) + ": " + currentTask.getName());
            taskDescription.setText(context.getString(R.string.task_description_label) + ": " + currentTask.getDescription());
            taskState.setText(task.getStateTask().getStatue());
        }
    }

    public void updateTasks(List<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        notifyDataSetChanged();
    }
}
