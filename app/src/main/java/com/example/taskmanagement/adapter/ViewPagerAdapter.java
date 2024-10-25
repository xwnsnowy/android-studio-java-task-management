package com.example.taskmanagement.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.taskmanagement.activities.CompletedFragment;
import com.example.taskmanagement.activities.InProgressFragment;
import com.example.taskmanagement.activities.MyTasksFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyTasksFragment();
            case 1:
                return new InProgressFragment();
            case 2:
                return new CompletedFragment();
            default:
                return new MyTasksFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
