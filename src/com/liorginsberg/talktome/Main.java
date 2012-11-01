package com.liorginsberg.talktome;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Main extends Activity {

	private Button btnTalkToMe;
	private ListView lvTasks;
	private TaskAdapter taskAdapter;
	private ArrayList<Task> arrayTaskList;
	private GestureDetector gestureDetector;
	static final private int voice = 1121;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//TODO remove initialization list
		arrayTaskList = new ArrayList<Task>(Arrays.asList(new Task[] { 
				new Task("first", false),
				new Task("second", false),
				new Task("third", false),
				new Task("forth", false),
				new Task("first", false),
				new Task("second", false),
				new Task("third", false),
				new Task("forth", false),
				new Task("first", false),
				new Task("second", false),
				new Task("third", false),
				new Task("forth", false),
				new Task("first", false),
				new Task("second", false),
				new Task("third", false),
				new Task("forth", false),
				new Task("first", false),
				new Task("second", false),
				new Task("third", false),
				new Task("forth", false),
				new Task("first", false),
				new Task("second", false),
				new Task("third", false),
				new Task("forth", false)
		}));
		
		taskAdapter = new TaskAdapter(this, R.layout.task_item, arrayTaskList);
		lvTasks = (ListView) findViewById(R.id.lvTasks);
		btnTalkToMe = (Button) findViewById(R.id.btnTalkToMe);
		lvTasks.setAdapter(taskAdapter);

		btnTalkToMe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "he");
				intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "דבר אליי");
				startActivityForResult(intent, voice);
			}
		});

		lvTasks.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				Log.i("DEBUG", "onLongClick()");
				arrayTaskList.remove(position);
				taskAdapter.notifyDataSetChanged();
				return false;
			}

		});

		lvTasks.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.i("DEBUG", "onTouch()");
				return gestureDetector.onTouchEvent(event);
			}
		});

		gestureDetector = new GestureDetector(this, new OnGestureListener() {

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {

			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {

			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velX, float velY) {
				Log.i("DEBUG", "onFling()");
				boolean value = false;
				float x = e1.getX();
				float y = e1.getY();
				int valx = Math.round(x);
				int valy = Math.round(y);
				final int posStart = lvTasks.pointToPosition(valx, valy);
				final int posEnd = lvTasks.pointToPosition(Math.round(e2.getX()), Math.round(e2.getY()));
				if (posStart == -1 || posEnd == -1) {
					return false;
				}
				if (posStart == posEnd && velX > 0) {
					Log.i("DEBUG", "onFling: start = end && velX > 0 fling -->");
					Animation anim = AnimationUtils.loadAnimation(Main.this, android.R.anim.slide_out_right);
					anim.setDuration(500);

					Log.i("DEBUG", "after anim setup");
					lvTasks.getChildAt(posStart-lvTasks.getFirstVisiblePosition()).startAnimation(anim);
					Log.i("DEBUG", "animation started");
					new Handler().postDelayed(new Runnable() {

						public void run() {
							Log.i("DEBUG", "after animation");
							arrayTaskList.remove(posStart);
							taskAdapter.notifyDataSetChanged();			
						}
					}, anim.getDuration());
				} else if (posStart == posEnd && velX < 0) {
					Log.i("DEBUG", "onFling: start = "+posStart +" end = "+posEnd+" && velX > 0 fling <--");
					LinearLayout tempLinearLayout = (LinearLayout) lvTasks.getChildAt(posStart-lvTasks.getFirstVisiblePosition());
					Log.i("DEBUG", "collected the linarView");
					TextView tempTextView = (TextView)tempLinearLayout.findViewById(R.id.tvTask);
					Log.i("DEBUG", "collected the TextView");
					tempTextView.setPaintFlags(tempTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
					arrayTaskList.get(posStart).setCrossed(true);
					taskAdapter.notifyDataSetChanged();		
					
				} else {
					
				}
				return value;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == voice && resultCode == RESULT_OK) {
			ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			arrayTaskList.add(new Task(results.get(0), false));
			taskAdapter.notifyDataSetChanged();
			lvTasks.setSelection(lvTasks.getBottom());
		}
	}

}

class TaskAdapter extends ArrayAdapter<Task> {

	private ArrayList<Task> tasks;
	
	public TaskAdapter(Context context, int textViewResourceId, ArrayList<Task> tasks) {
		super(context, textViewResourceId, tasks);
		this.tasks = tasks;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.task_item, null);
		}
		
		Task t = tasks.get(position);

		if (t != null) {
		
			TextView tv = (TextView) v.findViewById(R.id.tvTask);

			if (tv != null) {
				tv.setText(t.getText());
				if(t.isCrossed()) {
					tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);	
				}else {
					tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));	
				}
				
			}
		}
		return v;
	}
}
