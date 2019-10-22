package com.jnu.student.myfirstapplication;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jszx on 2019/9/24.
 */

public class Book {
    private String name;

    private int imageId;


    public Book (String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
