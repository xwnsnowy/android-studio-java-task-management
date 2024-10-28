package com.example.taskmanagement.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskmanagement.R;
import com.example.taskmanagement.adapter.TaskListAdapter;
import com.example.taskmanagement.models.Task;
import com.example.taskmanagement.sql.DatabaseHelper;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyTasksFragment extends Fragment implements TaskListActivity.TaskSearchListener {

    private RecyclerView recyclerView;
    private TaskListAdapter taskAdapter;
    private DatabaseHelper dbHelper;

    public static MyTasksFragment newInstance() {
        return new MyTasksFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_tasks, container, false);
        recyclerView = view.findViewById(R.id.rcv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DatabaseHelper(getContext());
        List<Task> taskList = dbHelper.getTasksByState("To Do");
        taskAdapter = new TaskListAdapter(taskList);
        recyclerView.setAdapter(taskAdapter);

        return view;
    }

    @Override
    public void onSearchQuery(String query) {
        if (taskAdapter != null) {
            taskAdapter.getFilter().filter(query);
        }
    }

    public void refreshTasks() {
        List<Task> updatedTasks = dbHelper.getTasksByState("To Do");
        taskAdapter.updateTasks(updatedTasks);
    }

}
