package com.jnu.student.myfirstapplication.data;

import android.content.Context;

import com.jnu.student.myfirstapplication.data.model.Book;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by jszx on 2019/10/14.
 */

public class FileDataSource {
    private Context context;
    private ArrayList<Book> books = new ArrayList<Book>();

    public FileDataSource(Context context) {
        this.context = context;
    }

    public ArrayList<Book> getBoods() {
        return books;
    }

    public void save () {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput("Serializable.txt",Context.MODE_PRIVATE)
            );
            outputStream.writeObject(books);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Book> load () {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput("Serializable.txt")
            );
            books = (ArrayList<Book>) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }
}
