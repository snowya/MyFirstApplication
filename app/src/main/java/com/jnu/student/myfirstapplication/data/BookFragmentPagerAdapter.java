package com.jnu.student.myfirstapplication.data;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class BookFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> datas;
    private ArrayList<String> titles;

    public BookFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return datas == null ? null : datas.get(i);
    }

    @Override
    public CharSequence getPageTitle(int i) {
        return titles == null ? null : titles.get(i);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    public void setDatas(ArrayList<Fragment> datas) {
        this.datas = datas;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }
}
