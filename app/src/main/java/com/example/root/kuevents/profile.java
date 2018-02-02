package com.example.root.kuevents;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.root.kuevents.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class profile extends Fragment {
    View view;
    ViewPager viewPager;
    TabLayout tabLayout;

    private sliderAdapter msliderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sample, container, false);


        msliderAdapter = new sliderAdapter(getChildFragmentManager());

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager){
        sliderAdapter adapter = new sliderAdapter(getChildFragmentManager());
        adapter.addFragment(new tab1(), "Going");
        adapter.addFragment(new tab2(), "Events posts");
        adapter.addFragment(new tab3(), "Attended");
        viewPager.setAdapter(adapter);
    }





}