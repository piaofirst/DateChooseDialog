package com.piaofire.datechoosedialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.piaofire.datechooselibrary.widget.DateChooseDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    private Button test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test = (Button) findViewById(R.id.test);
        text = (TextView) findViewById(R.id.text);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = text.getText().toString().trim();
                DateChooseDialog dateChooseDialog = new DateChooseDialog(MainActivity.this, DateChooseDialog.Type.YEAR_MONTH_DAY, new DateChooseDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time) {
                        text.setText(time);
                    }
                });
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    dateChooseDialog.setTime(sdf.parse(s));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dateChooseDialog.showDateChooseDialog();
            }
        });
    }
}
