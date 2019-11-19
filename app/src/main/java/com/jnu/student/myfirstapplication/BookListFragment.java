package com.jnu.student.myfirstapplication;


import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

@SuppressLint("ValidFragment")
public class BookListFragment extends Fragment {
    private BookListMainActivity.BookAdapter bookAdapter;

    BookListFragment(BookListMainActivity.BookAdapter adapter) {
        this.bookAdapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        ListView listView = (ListView)view.findViewById(R.id.list_view_books);
        listView.setAdapter(bookAdapter);
        this.registerForContextMenu(listView);
        return view;
    }
}
