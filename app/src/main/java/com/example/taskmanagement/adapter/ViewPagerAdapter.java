package com.example.taskmanagement.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.taskmanagement.activities.CompletedFragment;
import com.example.taskmanagement.activities.InProgressFragment;
import com.example.taskmanagement.activities.MyTasksFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final Fragment[] fragments;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragments = new Fragment[]{new MyTasksFragment(), new InProgressFragment(), new CompletedFragment()};
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }

    // Get a fragment by position
    public Fragment getFragment(int position) {
        return fragments[position];
    }
}
