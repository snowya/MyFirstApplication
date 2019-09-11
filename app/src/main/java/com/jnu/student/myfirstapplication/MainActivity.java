package com.jnu.student.myfirstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = findViewById(R.id.text_view_hello_world);
        int hello_id = getResources().getIdentifier("hello","string",getPackageName());
        text.setText(this.getString(hello_id));
    }
}
