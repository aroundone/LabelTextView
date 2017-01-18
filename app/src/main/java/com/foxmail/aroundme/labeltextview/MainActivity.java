package com.foxmail.aroundme.labeltextview;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.foxmail.aroundme.library.LabelTextView;
import com.foxmail.aroundme.library.RoundRectLabelView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager);

        /*LabelTextView labelTextView = (LabelTextView)findViewById(R.id.labelTextView);
        labelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
            }
        });*/


        List<String> list = new ArrayList<>();
        list.add("Hot");
        list.add("Hot");
        list.add("Hot");
        list.add("Hot");
        list.add("Hot");
        list.add("Hot");
        list.add("Hot");
        list.add("Hot");
        GridView gridView = (GridView)findViewById(R.id.grid);
        gridView.setAdapter(new GridViewChannelAdapter(MainActivity.this, list));
    }
}
