package com.example.todolistapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private EditText ed;
    private ListView item_list;
    private ArrayList<String> task_list; //ArrayList takes the user input
    private ArrayAdapter<String> arrayAdapter; // ArrayAdapter is used to insert a value in ListView
    DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed = findViewById(R.id.text_edit);
        //ImageView adding_items = findViewById(R.id.add_text);
        item_list = findViewById(R.id.listview);

        helper = new DatabaseHelper(this);
        task_list = new ArrayList<>();
        task_list = helper.getAllTask();
        arrayAdapter = new ArrayAdapter(this, R.layout.item_details, R.id.taskShow, task_list);
        arrayAdapter.notifyDataSetChanged();
        item_list.setAdapter(arrayAdapter);

        //adding_items.setOnClickListener(this);
        //item_list.setOnItemLongClickListener(this); // Giving the reference of the onclick function
        FloatingActionButton insertBtn = findViewById(R.id.add_text);
        insertBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addTask();
            }
        });


        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentTask = (String) parent.getItemAtPosition(position);
                ed.setText(currentTask);

                insertBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String newTask = ed.getText().toString();
                        helper.updateTask(currentTask, newTask);

                        task_list.clear();
                        task_list.addAll(helper.getAllTask());
                        arrayAdapter.notifyDataSetChanged();
                        item_list.invalidate();
                        item_list.refreshDrawableState();
                        ed.setText("");
                    }
                });

            }
        });
    }


    public void addTask() {
        String add_item = ed.getText().toString();

        helper.insert(add_item);
        arrayAdapter.notifyDataSetChanged();
        task_list.clear();
        task_list.addAll(helper.getAllTask());
        item_list.invalidate();
        item_list.refreshDrawableState();
        ed.setText(" ");
        Toast toast = Toast.makeText(MainActivity.this, "Task Added", Toast.LENGTH_SHORT);
        toast.show();

    }

    public void deleteTask(View view) { //for delete functionality, that is when the DONE Button is clicked
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.taskShow);
        String task = String.valueOf(taskTextView.getText());

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Complete Task ?");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                // Write your code here to invoke YES event
                helper.deleteInfo(task);
                arrayAdapter.notifyDataSetChanged();
                task_list.clear();
                task_list.addAll(helper.getAllTask());
                item_list.invalidate();
                item_list.refreshDrawableState();
                Toast.makeText(getApplicationContext(), "Well Done , Task Completed", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(getApplicationContext(), "Please Complete Your Work", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }


}
