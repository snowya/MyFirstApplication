package com.jnu.student.myfirstapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
    private ListView booksList; //ListView对象
    private BookAdapter adapter;
    private FileDataSource fileDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        init();

        booksList = (ListView) findViewById(R.id.list_view_books);
        adapter = new BookAdapter(BookListMainActivity.this, R.layout.book_item, books);
        booksList.setAdapter(adapter);

        this.registerForContextMenu(booksList);
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
        if (v == booksList) {
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
