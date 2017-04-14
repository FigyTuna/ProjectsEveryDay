package com.figytuna.projectseveryday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TableLayout projectListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        projectListLayout = (TableLayout) findViewById(R.id.projectListLayout);


        //Test addListItem
        for (int i = 0; i < 100; ++i)
        {
            addListItem ("List Item #" + i);
        }

    }

    private void addListItem (String title)
    {
        TableRow addTableRow = new TableRow(this);
        addTableRow.setOrientation (TableRow.VERTICAL);
        TableRow.LayoutParams rowContainerParams
                = new TableRow.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0.0f);
        addTableRow.setLayoutParams (rowContainerParams);
        projectListLayout.addView(addTableRow);

        TextView addTextView = new TextView (this);
        addTextView.setText(title);
        addTextView.setGravity(Gravity.START);
        TableRow.LayoutParams rowWidgetParams
                = new TableRow.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        addTextView.setLayoutParams(rowWidgetParams);
        addTableRow.addView(addTextView);
    }
}
