package com.example.taskmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.taskmanagement.R;
import com.example.taskmanagement.activities.TaskDetailActivity;
import com.example.taskmanagement.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> implements Filterable {
    private List<Task> tasks;
    private List<Task> filteredTasks;

    public TaskListAdapter(List<Task> tasks) {
        this.tasks = tasks;
        this.filteredTasks = new ArrayList<>(tasks);
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
        Task task = filteredTasks.get(position);
        holder.bindData(task);
    }

    @Override
    public int getItemCount() {
        return filteredTasks.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Task> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(tasks);
                } else {
                    String filterPattern = constraint.toString().toUpperCase().trim();

                    for (Task task : tasks) {
                        if (task.getName().toUpperCase().contains(filterPattern)) {
                            filteredList.add(task);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredTasks.clear();
                if (results.values != null) {
                    filteredTasks.addAll((List<Task>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView taskName;
        private TextView taskDescription;
        private TextView taskState;
        private Task currentTask;

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
            taskName.setText(task.getName());
            taskDescription.setText(task.getDescription());
            taskState.setText(task.getStateTask().getStatue());
        }
    }

    public void updateTasks(List<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        filteredTasks.clear();
        filteredTasks.addAll(newTasks);
        notifyDataSetChanged();
    }
}
