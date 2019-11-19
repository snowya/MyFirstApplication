package com.jnu.student.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditTextActivity extends AppCompatActivity {

    private Button button;
    private TextView text;
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        button = findViewById(R.id.button_change_language);
        text = findViewById(R.id.text_view_language);
        edit = findViewById(R.id.edit_text_country);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String s = edit.getText().toString();
                int id_text = getResources().getIdentifier("language_"+s, "string", getPackageName());
                int id_button = getResources().getIdentifier("change_language_"+s, "string", getPackageName());
                button.setText(id_button);
                text.setText(id_text);
            }
        });
    }
}
