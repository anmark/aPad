package com.anmark.dialpad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

public class DialPadView extends TableLayout implements OnClickListener{

	private SoundPool soundPool;
	//private HashMap<Integer, Integer> soundPoolMap;
	boolean mExternalStorageAvailable = false;
	boolean mExternalStorageWriteable = false;
	private String state;
	private String baseFilePath;
	private String currentSoundFolder;
	private String numbers;
	private EditText pressedNumbers;

	private int sound1ID, sound2ID, sound3ID,sound4ID,sound5ID,sound6ID,sound7ID, sound8ID, sound9ID, soundSID, sound0ID, soundPID;

	private boolean soundLoaded;

	public DialPadView(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_dial_pad_view, this, true);
		init(context);
	}

	public DialPadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void checkMediaAvailability(){
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but all we need
			//  to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		System.out.println(Environment.getExternalStorageDirectory());
	}

	@SuppressLint("UseSparseArrays")
	public void init(Context context){



		state = Environment.getExternalStorageState();
		checkMediaAvailability();

		if(mExternalStorageAvailable){
			baseFilePath = Environment.getExternalStorageDirectory() + "/dialpad/sounds/";
		}

		//TODO: set smarter
		currentSoundFolder = "mamacita_us/";


		sound1ID = 1;
		sound2ID = 2;
		sound3ID = 3;
		sound4ID = 4;
		sound5ID = 5;
		sound6ID = 6;
		sound7ID = 7;
		sound8ID = 8;
		sound9ID = 9;
		soundSID = 10;
		sound0ID = 11;
		soundPID = 12;


		Button b1 = (Button) findViewById(R.id.button1);
		Button b2 = (Button) findViewById(R.id.button2); 
		Button b3 = (Button) findViewById(R.id.button3); 
		Button b4 = (Button) findViewById(R.id.button4); 
		Button b5 = (Button) findViewById(R.id.button5); 
		Button b6 = (Button) findViewById(R.id.button6); 
		Button b7 = (Button) findViewById(R.id.button7); 
		Button b8 = (Button) findViewById(R.id.button8);
		Button b9 = (Button) findViewById(R.id.button9); 
		Button bS = (Button) findViewById(R.id.buttonS); 
		Button b0 = (Button) findViewById(R.id.button0); 
		Button bP = (Button) findViewById(R.id.buttonP);
		Button barrow = (Button) findViewById(R.id.buttonarrow);
		Button bcall = (Button) findViewById(R.id.buttoncall);

		pressedNumbers = (EditText)findViewById(R.id.pressedNumbers);
		numbers = "";
		// Get pressed numbers
		//numbers = pressedNumbers.getText().toString();

		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		b3.setOnClickListener(this);
		b4.setOnClickListener(this);
		b5.setOnClickListener(this);
		b6.setOnClickListener(this);
		b7.setOnClickListener(this);
		b8.setOnClickListener(this);
		b9.setOnClickListener(this);
		bS.setOnClickListener(this);
		b0.setOnClickListener(this);
		bP.setOnClickListener(this);
		barrow.setOnClickListener(this);
		bcall.setOnClickListener(this);
		
		barrow.setOnLongClickListener(new OnLongClickListener() { 
	        @Override
	        public boolean onLongClick(View v) {
	        	numbers = "";
	        	pressedNumbers.setText(numbers);
	            return true;
	        }
	    });

		//this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// Load the sound

		/*soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded = true;
			}
		});
		 */



		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		//soundPoolMap = new HashMap<Integer, Integer>();

		// Initialize sounds
		// Example of a path
		//Environment.getExternalStorageDirectory() + /dialpad/sounds/ + mamacita_us/ + filename

		if(mExternalStorageAvailable){
			soundPool.load(baseFilePath + currentSoundFolder + "one.mp3", 1);
			soundPool.load(baseFilePath + currentSoundFolder + "two.mp3", 1);
			soundPool.load(baseFilePath + currentSoundFolder + "three.mp3", 1);
			soundPool.load(baseFilePath + currentSoundFolder + "four.mp3", 1);
			soundPool.load(baseFilePath + currentSoundFolder + "five.mp3", 1);
			soundPool.load(baseFilePath + currentSoundFolder + "six.mp3", 1);
			soundPool.load(baseFilePath + currentSoundFolder + "seven.mp3", 1);
			soundPool.load(baseFilePath + currentSoundFolder + "eight.mp3", 1);
			soundPool.load(baseFilePath + currentSoundFolder + "nine.mp3", 1);
			soundPool.load(baseFilePath + currentSoundFolder + "star.mp3", 1);
			soundPool.load(baseFilePath + currentSoundFolder + "zero.mp3", 1);
			soundPool.load(baseFilePath + currentSoundFolder + "pound.mp3", 1);
			soundLoaded = true;
		}
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setMessage("No external sd card found, sound will not be played")
			.setCancelable(false)
			.setIcon(R.drawable.ic_launcher)
			.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// do nothing
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
		/*
		soundPoolMap.put(sound1ID, soundPool.load(context, R.raw.one, 1));
		soundPoolMap.put(sound2ID, soundPool.load(context, R.raw.two, 1));
		soundPoolMap.put(sound3ID, soundPool.load(context, R.raw.three, 1));
		soundPoolMap.put(sound4ID, soundPool.load(context, R.raw.four, 1));
		soundPoolMap.put(sound5ID, soundPool.load(context, R.raw.five, 1));
		soundPoolMap.put(sound6ID, soundPool.load(context, R.raw.six, 1));
		soundPoolMap.put(sound7ID, soundPool.load(context, R.raw.seven, 1));
		soundPoolMap.put(sound8ID, soundPool.load(context, R.raw.eight, 1));
		soundPoolMap.put(sound9ID, soundPool.load(context, R.raw.nine, 1));
		soundPoolMap.put(soundSID, soundPool.load(context, R.raw.star, 1));
		soundPoolMap.put(sound0ID, soundPool.load(context, R.raw.zero, 1));
		soundPoolMap.put(soundPID, soundPool.load(context, R.raw.pound, 1));
		 */
	}



	public void onClick(View v) {
		if(soundLoaded){
			// Get user sound settings
			AudioManager mgr = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
			int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);

			switch (v.getId()) {
			case R.id.button1:
				soundPool.play(sound1ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "1";
				break;
			case R.id.button2:
				soundPool.play(sound2ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "2";
				break;
			case R.id.button3:
				soundPool.play(sound3ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "3";
				break;
			case R.id.button4:
				soundPool.play(sound4ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "4";
				break;
			case R.id.button5:
				soundPool.play(sound5ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "5";
				break;
			case R.id.button6:
				soundPool.play(sound6ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "6";
				break;
			case R.id.button7:
				soundPool.play(sound7ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "7";
				break;
			case R.id.button8:
				soundPool.play(sound8ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "8";
				break;
			case R.id.button9:
				soundPool.play(sound9ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "9";
				break;
			case R.id.buttonS:
				soundPool.play(soundSID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "*";
				break;
			case R.id.button0:
				soundPool.play(sound0ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "0";
				break;
			case R.id.buttonP:
				soundPool.play(soundPID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "#";
				break;
			case R.id.buttonarrow:
				if(numbers.length() != 0)
					numbers = numbers.substring(0 , numbers.length()-1);
				break;
			case R.id.buttoncall:
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:" + numbers));
				((Activity)getContext()).startActivity(intent);
				break;
			}
			pressedNumbers.setText(numbers);
		}
		else{// sound not loaded, just get pressed number
			switch (v.getId()) {
			case R.id.button1:
				numbers = numbers + "1";
				break;
			case R.id.button2:
				numbers = numbers + "2";
				break;
			case R.id.button3:
				numbers = numbers + "3";
				break;
			case R.id.button4:
				numbers = numbers + "4";
				break;
			case R.id.button5:
				numbers = numbers + "5";
				break;
			case R.id.button6:
				numbers = numbers + "6";
				break;
			case R.id.button7:
				numbers = numbers + "7";
				break;
			case R.id.button8:
				numbers = numbers + "8";
				break;
			case R.id.button9:
				numbers = numbers + "9";
				break;
			case R.id.buttonS:
				numbers = numbers + "*";
				break;
			case R.id.button0:

				numbers = numbers + "0";
				break;
			case R.id.buttonP:
				numbers = numbers + "#";
				break;
			case R.id.buttonarrow:
				if(numbers.length() != 0)
					numbers = numbers.substring(0 , numbers.length()-1);
				break;
			case R.id.buttoncall:
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:" + numbers));
				((Activity)getContext()).startActivity(intent);
				break;
			}
			pressedNumbers.setText(numbers);
		}
	}
}
