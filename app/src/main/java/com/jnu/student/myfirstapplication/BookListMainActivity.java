package com.jnu.student.myfirstapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jnu.student.myfirstapplication.data.BookFragmentPagerAdapter;
import com.jnu.student.myfirstapplication.data.FileDataSource;
import com.jnu.student.myfirstapplication.data.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {

    public static final int CONTENT_NEW = 1;
    public static final int CONTENT_UPDATE = CONTENT_NEW + 1;
    public static final int CONTENT_DELETE = CONTENT_UPDATE + 1;
    public static final int CONTENT_ABOUT = CONTENT_DELETE + 1;
    public static final int REQUEST_CODE_NEW_GOOD = 901;
    public static final int REQUEST_CODE_UPDATE_GOOD = 902;
    private List<Book> books = new ArrayList<>();
    private BookAdapter adapter;
    private FileDataSource fileDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        init();

        // bookListFragment的数据填充
        adapter = new BookAdapter(BookListMainActivity.this, R.layout.book_item, books);
        BookListFragment bookListFragment = new BookListFragment(adapter);

        // 两个webViewFragment的创建
        WebFragment newsFragment = new WebFragment();
        MapFragment sellersFragment = new MapFragment();
        //初始化地图 SDK
        SDKInitializer.initialize(getApplicationContext());

        // 创建并设置Fragment和titles数组
        ArrayList<Fragment> datas = new ArrayList<Fragment>();
        ArrayList<String> titles = new ArrayList<String>();
        datas.add(bookListFragment);
        datas.add(newsFragment);
        datas.add(sellersFragment);
        titles.add("图书");
        titles.add("新闻");
        titles.add("卖家");
        BookFragmentPagerAdapter myPagerAdapter = new BookFragmentPagerAdapter(getSupportFragmentManager());
        myPagerAdapter.setDatas(datas);
        myPagerAdapter.setTitles(titles);

        // 将adapter、tableLayout和viewPager关联起来
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public List<Book> getListBooks() {
        return books;
    }

    class BookAdapter extends ArrayAdapter<Book> {

        private int resourceId;

        public BookAdapter(Context context, int resource, List<Book> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Book book = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ((ImageView) view.findViewById(R.id.image_view_book_cover)).setImageResource(book.getImageId());
            ((TextView) view.findViewById(R.id.text_view_book_title)).setText(book.getName());
            return view;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v == this.findViewById(R.id.list_view_books)) {
            int itemPosition = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
            menu.setHeaderTitle(books.get(itemPosition).getName());
            menu.add(0, CONTENT_NEW, 0, "新建");
            menu.add(0, CONTENT_UPDATE, 0, "修改");
            menu.add(0, CONTENT_DELETE, 0, "删除");
            menu.add(0, CONTENT_ABOUT, 0, "关于...");
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_NEW_GOOD:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra("edit_position", 0);
                    String name = data.getStringExtra("book_name");
                    books.add(position, new Book(name, R.drawable.book_no_name));
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "新建成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_UPDATE_GOOD:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra("edit_position", 0);
                    String name = data.getStringExtra("book_name");
                    books.get(position).setName(name);
                    adapter.notifyDataSetChanged();

                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTENT_NEW: {
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Intent intent = new Intent(BookListMainActivity.this, EditBookActivity.class);
                intent.putExtra("edit_position", menuInfo.position);
                startActivityForResult(intent, REQUEST_CODE_NEW_GOOD);

                break;
            }
            case CONTENT_UPDATE: {
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                Book book = books.get(menuInfo.position);

                Intent intent = new Intent(BookListMainActivity.this, EditBookActivity.class);
                intent.putExtra("edit_position", menuInfo.position);
                intent.putExtra("book_name", book.getName());
                startActivityForResult(intent, REQUEST_CODE_UPDATE_GOOD);

                break;
            }
            case CONTENT_DELETE: {
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                final int itemPosition = menuInfo.position;
                new android.app.AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("询问")
                        .setMessage("你确定要删除这条吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                books.remove(itemPosition);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(BookListMainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create().show();
                break;
            }
            case CONTENT_ABOUT:
                Toast.makeText(this, "版权所有by snowy!", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        fileDataSource.save();
        super.onDestroy();
    }

    private void init() {
        fileDataSource = new FileDataSource(this);
        books = fileDataSource.load();
        if(books.size()==0) {
            books.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2));
            books.add(new Book("创新工程实践", R.drawable.book_no_name));
            books.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));
        }
    }
}
