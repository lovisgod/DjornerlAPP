package com.example.ayo.mygjornerl;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;


import com.example.ayo.mygjornerl.sampledata.AppDatabase;
import com.example.ayo.mygjornerl.sampledata.AppExecutors;
import com.example.ayo.mygjornerl.sampledata.EventEntry;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class journalViewActivity extends AppCompatActivity implements EventAdapter.ItemClickListener {
    // Constant for logging
    private static final String TAG = journalViewActivity.class.getSimpleName();
    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerView;
    private EventAdapter mAdapter;

    private AppDatabase mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_journalview);


        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.recyclerViewTasks);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new EventAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<EventEntry> tasks = mAdapter.getTasks();
                        mdb.taskDao().deleteTask(tasks.get(position));
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);

        /*
         Set the Floating Action Button (FAB) to its corresponding View.
         Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
         to launch the AddEventActivity.
         */
        FloatingActionButton fabButton = findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddEventActivity
                Intent addTaskIntent = new Intent(journalViewActivity.this, journalActivity.class);
                startActivity(addTaskIntent);
            }
        });
        //initialize the mdb
        mdb = AppDatabase.getInstance(getApplicationContext());
        retrieveTask();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //here we call the querry from task dao
        //mAdapter.setTasks(mdb.taskDao().loadAllTasks());
    }

    private void retrieveTask() {

        final LiveData<List<EventEntry>> tasks = mdb.taskDao().loadAllTasks();
        tasks.observe(this, new Observer<List<EventEntry>>() {
            @Override
            public void onChanged(@Nullable List<EventEntry> eventEntries) {
                mAdapter.setTasks(eventEntries);
            }
        });
    }


    public void onItemClickListener(int itemId) {
        // Launch AddEventActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(journalViewActivity.this, journalActivity.class);
        intent.putExtra(journalActivity.EXTRA_TASK_ID, itemId);
        startActivity(intent);
    }
}


