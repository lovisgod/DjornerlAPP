package com.example.ayo.mygjornerl;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.ayo.mygjornerl.sampledata.AppDatabase;
import com.example.ayo.mygjornerl.sampledata.AppExecutors;
import com.example.ayo.mygjornerl.sampledata.EventEntry;

import java.util.Date;

public class journalActivity extends AppCompatActivity {

    // Extra for the task ID to be received in the intent
    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;
    // Constant for logging
    private static final String TAG = journalActivity.class.getSimpleName();
    // Fields for views
    EditText mEditText;
    // RadioGroup mRadioGroup;
    Button mButton;

    private int mTaskId = DEFAULT_TASK_ID;

    //Create AppDatabase member variable for the Database
    // Member variable for the Database
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);


        initViews();

        //  Initialize member variable for the data base
        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.setText(R.string.update_button);
            if (mTaskId == DEFAULT_TASK_ID) {
                // populate the UI
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);
                final LiveData<EventEntry> task = mDb.taskDao().loadTaskById(mTaskId);
                task.observe(this, new Observer<EventEntry>() {
                    @Override
                    public void onChanged(@Nullable EventEntry eventEntry) {
                        task.removeObserver(this);
                        populateUI(eventEntry);
                    }
                });
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId);
        super.onSaveInstanceState(outState);
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mEditText = findViewById(R.id.editText);


        mButton = findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param task the taskEntry to populate the UI
     */
    private void populateUI(EventEntry task) {
        if (task == null) {
            return;
        }
        mEditText.setText(task.getDescription());

    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onSaveButtonClicked() {
        // Create a description variable and assign to it the value in the edit text
        String description = mEditText.getText().toString();

        //  Create a date variable and assign to it the current Date
        Date date = new Date();

        //  Create eventEntry variable using the variables defined above
        final EventEntry eventEntry = new EventEntry(description, date);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mTaskId == DEFAULT_TASK_ID) {
                    mDb.taskDao().insertTask(eventEntry);
                } else {
                    //update task
                    eventEntry.setId(mTaskId);
                    mDb.taskDao().updateTask(eventEntry);
                }

                finish();
            }
        });
        //  Use the taskDao in the AppDatabase variable to insert the eventEntry
        //mDb.taskDao().insertTask(eventEntry);
        // call finish() to come back to MainActivity
        // finish();
    }

}

