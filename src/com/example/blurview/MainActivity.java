package com.example.blurview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Space;


public class MainActivity extends Activity
{
	private Space header;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        header=(Space)findViewById(R.id.header);
        header.setMinimumHeight(getResources().getDisplayMetrics().heightPixels /2);
    }
}
