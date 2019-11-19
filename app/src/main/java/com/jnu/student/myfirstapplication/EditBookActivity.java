package com.jnu.student.myfirstapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditBookActivity extends AppCompatActivity {

    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        edit = findViewById(R.id.editName);
        Button button_ok = findViewById(R.id.button_confirm);
        Button button_cancel = findViewById(R.id.button_cancel);

        final int position = getIntent().getIntExtra("edit_position",0);
        String book_name = getIntent().getStringExtra("book_name");

        if(book_name != null) {
            edit.setText(book_name);
        }

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("book_name",edit.getText().toString());
                intent.putExtra("edit_position",position);
                setResult(RESULT_OK,intent);
                EditBookActivity.this.finish();
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditBookActivity.this.finish();
            }
        });

    }
}
