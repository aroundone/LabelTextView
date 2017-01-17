package com.foxmail.aroundme.labeltextview;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.foxmail.aroundme.library.RoundRectLabelView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*List<String> list = new ArrayList<>();
        list.add("Hot");
        list.add("Hot");
        list.add("Hot");
        list.add("Hot");
        list.add("Hot");
        list.add("Hot");
        list.add("Hot");
        list.add("Hot");
        GridView gridView = (GridView)findViewById(R.id.grid);
        gridView.setAdapter(new GridViewChannelAdapter(MainActivity.this, list));*/
    }
}
