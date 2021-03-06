package com.anmark.dialpad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

public class DialPadView extends TableLayout implements OnClickListener{

	//private HashMap<Integer, Integer> soundPoolMap;
	public static boolean soundFolderExist;
	public static boolean mExternalStorageAvailable = false;
	boolean mExternalStorageWriteable = false;

	public static String baseFilePath;
	public static String destinationSoundFolder = "destinationSoundFolder";
	public static final String PREFS_NAME = "Prefs";
	static final String soundUrl = "soundUrl";
	private String soundUrlData = "http://dt031g.programvaruteknik.nu/dialpad/sounds/";
	static final int RESULT_SETTINGS = 1;

	private String state;
	private Context context;
	private SoundPool soundPool;
	private String currentSoundFolderData = null;
	private String destinationSoundFolderData;
	private String numbers;
	private EditText pressedNumbers;
	private Button b1, b2, b3, b4, b5, b6, b7, b8, b9, bS, b0, bP, bArrow, bCall;

	private int sound1ID, sound2ID, sound3ID,sound4ID,sound5ID,sound6ID,sound7ID, sound8ID, sound9ID, soundSID, sound0ID, soundPID;

	private boolean soundLoaded;

	public DialPadView(Context context) {
		super(context);
		this.context = context;
		/*LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_dial_pad_view, this, true);
		 */
		View.inflate(context, R.layout.activity_dial_pad_view, this);
		init(context);

	}

	public DialPadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		View.inflate(context, R.layout.activity_dial_pad_view, this);
		init(context);
	}

	public void checkMediaAvailability(){
		state = Environment.getExternalStorageState();
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
	}

	@SuppressLint("UseSparseArrays")
	public void init(Context context){
		
		checkMediaAvailability();
		
		System.out.println("test again");
		

		if(mExternalStorageAvailable){
			//baseFilePath = Environment.getExternalStorageDirectory() + "/dialpad/sounds/";
			//destinationSoundFolderData = baseFilePath;
			
			createDirIfNotExists(Environment.getExternalStorageDirectory() + "/dialpad/sounds");
			
			/*
			File folder = new File(Environment.getExternalStorageDirectory() + "/dialpad/sounds");
			
			//TODO: why dir not created?
			if (!folder.exists()) {
				
				soundFolderExist = folder.mkdir();
				System.out.println(soundFolderExist);
				
			}
			/*
			if (success) {
			    // Do something on success
			} else {
			    // Do something else on failure 
			}
			*/
			
			loadSharedPrefs();
			savePrefs();
		}

		//TODO: set smarter
		//currentSoundFolderData = "mamacita_us/";

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


		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.button2); 
		b3 = (Button) findViewById(R.id.button3); 
		b4 = (Button) findViewById(R.id.button4); 
		b5 = (Button) findViewById(R.id.button5); 
		b6 = (Button) findViewById(R.id.button6); 
		b7 = (Button) findViewById(R.id.button7); 
		b8 = (Button) findViewById(R.id.button8);
		b9 = (Button) findViewById(R.id.button9); 
		bS = (Button) findViewById(R.id.buttonS); 
		b0 = (Button) findViewById(R.id.button0); 
		bP = (Button) findViewById(R.id.buttonP);
		bArrow = (Button) findViewById(R.id.buttonarrow);
		bCall = (Button) findViewById(R.id.buttoncall);

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
		bArrow.setOnClickListener(this);
		bCall.setOnClickListener(this);

		bArrow.setOnLongClickListener(new OnLongClickListener() { 
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
		// Environment.getExternalStorageDirectory() + /dialpad/sounds/ + mamacita_us/ + filename

		System.out.println("cur: " + currentSoundFolderData);

		if(currentSoundFolderData == null){
			// Prompt user if no voice specified

			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context,
					"No voice specified, sound will not be played", duration);
			toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);
			toast.show();	

		}
		else {
			
		if(mExternalStorageAvailable){
			String workaroundcurrentSoundFolderData = currentSoundFolderData + "/";
			soundPool.load(workaroundcurrentSoundFolderData + "one.mp3", 1);
			soundPool.load(workaroundcurrentSoundFolderData + "two.mp3", 1);
			soundPool.load(workaroundcurrentSoundFolderData + "three.mp3", 1);
			soundPool.load(workaroundcurrentSoundFolderData + "four.mp3", 1);
			soundPool.load(workaroundcurrentSoundFolderData + "five.mp3", 1);
			soundPool.load(workaroundcurrentSoundFolderData + "six.mp3", 1);
			soundPool.load(workaroundcurrentSoundFolderData + "seven.mp3", 1);
			soundPool.load(workaroundcurrentSoundFolderData + "eight.mp3", 1);
			soundPool.load(workaroundcurrentSoundFolderData + "nine.mp3", 1);
			soundPool.load(workaroundcurrentSoundFolderData + "star.mp3", 1);
			soundPool.load(workaroundcurrentSoundFolderData + "zero.mp3", 1);
			soundPool.load(workaroundcurrentSoundFolderData + "pound.mp3", 1);
			soundLoaded = true;
		}
		else{
			// Prompt user if no external storage available

			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context,
					"No external sd card found, sound will not be played", duration);
			toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);
			toast.show();	
		}

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("keyevent: " + keyCode);
		//super.onWKeyDown(keyCode, event);

		System.out.println(event.getUnicodeChar());
		System.out.println((char) event.getUnicodeChar());
		char pressedKey = (char) event.getUnicodeChar();

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			//TODO: Solve menu button settings opened

			//getParent().getMenuInflater().inflate(R.menu.main);
			System.out.println("menu : " + keyCode);      
		}

		if(soundLoaded){
			// Get user sound settings
			AudioManager mgr = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
			int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);

			switch (pressedKey) {
			case '1':
				b1.setPressed(true);
				soundPool.play(sound1ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "1";
				break;
			case '2':
				b2.setPressed(true);
				soundPool.play(sound2ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "2";
				break;
			case '3':
				b3.setPressed(true);
				soundPool.play(sound3ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "3";
				break;
			case '4':
				b4.setPressed(true);
				soundPool.play(sound4ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "4";
				break;
			case '5':
				b5.setPressed(true);
				soundPool.play(sound5ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "5";
				break;
			case '6':
				b6.setPressed(true);
				soundPool.play(sound6ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "6";
				break;
			case '7':
				b7.setPressed(true);
				soundPool.play(sound7ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "7";
				break;
			case '8':
				b8.setPressed(true);
				soundPool.play(sound8ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "8";
				break;
			case '9':
				b9.setPressed(true);
				soundPool.play(sound9ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "9";
				break;
			case '*':
				bS.setPressed(true);
				soundPool.play(soundSID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "*";
				break;
			case '0':
				b0.setPressed(true);
				soundPool.play(sound0ID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "0";
				break;
			case '#':
				bP.setPressed(true);
				soundPool.play(soundPID, streamVolume, streamVolume, 1, 0, 1f);
				numbers = numbers + "#";
				break;
			default:
				//pressedNumbers.setText(numbers);
				switch(keyCode){
				case KeyEvent.KEYCODE_CLEAR:
					bArrow.setPressed(true);
					if(numbers.length() != 0)
						numbers = numbers.substring(0 , numbers.length()-1);
					break;
				case KeyEvent.KEYCODE_CALL:
					bCall.setPressed(true);
					//tryCall();
					break;
				}
			}
			//pressedNumbers.setText(numbers);
			return true;
		}// sound loaded
		else{// sound not loaded, just get pressed number
			switch (pressedKey) {
			case '1':
				System.out.println("triggad");
				b1.setPressed(true);
				numbers = numbers + "1";
				return true;
			case '2':
				b2.setPressed(true);
				numbers = numbers + "2";
				return true;
			case '3':
				b3.setPressed(true);
				numbers = numbers + "3";
				return true;
			case '4':
				b4.setPressed(true);
				numbers = numbers + "4";
				return true;
			case '5':
				b5.setPressed(true);
				numbers = numbers + "5";
				return true;
			case '6':
				b6.setPressed(true);
				numbers = numbers + "6";
				return true;
			case '7':
				b7.setPressed(true);
				numbers = numbers + "7";
				return true;
			case '8':
				b8.setPressed(true);
				numbers = numbers + "8";
				return true;
			case '9':
				b9.setPressed(true);
				numbers = numbers + "9";
				return true;
			case '*':
				bS.setPressed(true);
				numbers = numbers + "*";
				return true;
			case '0':
				b0.setPressed(true);
				numbers = numbers + "0";
				return true;
			case '#':
				bP.setPressed(true);
				numbers = numbers + "#";
				break;

			default:
				switch(keyCode){
				case KeyEvent.KEYCODE_CLEAR:
					bArrow.setPressed(true);
					if(numbers.length() != 0)
						numbers = numbers.substring(0 , numbers.length()-1);
					break;
				case KeyEvent.KEYCODE_CALL:
					bCall.setPressed(true);
					break;
				}
			}
			//pressedNumbers.setText(numbers);
			return true;
		}// sound not loaded
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		System.out.println("keyevent: " + keyCode);
		pressedNumbers.setText(numbers);

		char pressedKey = (char) event.getUnicodeChar();
		switch (pressedKey) {
		case '1':
			b1.setPressed(false);
			break;
		case '2':
			b2.setPressed(false);
			break;
		case '3':
			b3.setPressed(false);
			break;
		case '4':
			b4.setPressed(false);
			break;
		case '5':
			b5.setPressed(false);
			break;
		case '6':
			b6.setPressed(false);
			break;
		case '7':
			b7.setPressed(false);
			break;
		case '8':
			b8.setPressed(false);
			break;
		case '9':
			b9.setPressed(false);
			break;
		case '*':
			bS.setPressed(false);
			break;
		case '0':
			b0.setPressed(false);
			break;
		case '#':
			bP.setPressed(false);
			break;
		default:
			switch(keyCode){
			case KeyEvent.KEYCODE_CLEAR:
				bArrow.setPressed(false);
				break;
			case KeyEvent.KEYCODE_CALL:
				bCall.setPressed(false);
				tryCall();
				break;
			}
		}
		return true;
	}// key up

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
				tryCall();
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
				tryCall();
				break;
			}
			pressedNumbers.setText(numbers);
		}
	}
	public void tryCall(){
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel: " + Uri.encode(numbers)));
			callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(callIntent);
		} catch (ActivityNotFoundException e) {
			Log.e("ACTION CALL", "Call failed", e);
		}

	}
	// Save preferences
	private void savePrefs() {
		/*
		 * SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, 0);
		 
		
		*/
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		SharedPreferences.Editor editor = sharedPrefs.edit();

		editor.putString("prefVoicelist", currentSoundFolderData);
		editor.putString("prefSource", soundUrlData);
		editor.putString("prefStorage", destinationSoundFolderData);

		// Commit the edits!
		editor.commit();
	}
	private void loadSharedPrefs() {
		//SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, 0);
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

		
		currentSoundFolderData = sharedPrefs.getString("prefVoicelist", null);
		soundUrlData = sharedPrefs.getString("prefSource", "http://dt031g.programvaruteknik.nu/dialpad/sounds/");
		destinationSoundFolderData = sharedPrefs.getString("prefStorage", Environment.getExternalStorageDirectory() + "/dialpad/sounds");
		 	
		System.out.println("a"+ currentSoundFolderData);
		System.out.println("b" + soundUrlData);
		System.out.println("c" +destinationSoundFolderData);
		
		
	}

	public boolean ismExternalStorageAvailable() {
		return mExternalStorageAvailable;
	}

	public boolean isSoundLoaded() {
		return soundLoaded;
	}

	public boolean ismExternalStorageWriteable() {
		return mExternalStorageWriteable;
	}

	public String getCurrentSoundFolderData() {
		return currentSoundFolderData;
	}

	public String getDestinationSoundFolderData() {
		return destinationSoundFolderData;
	}

	public String getSoundUrlData() {
		return soundUrlData;
	}

	public void setSoundUrlData(String soundUrlData) {
		this.soundUrlData = soundUrlData;
	}

	public void setDestinationSoundFolderData(String destinationSoundFolderData) {
		this.destinationSoundFolderData = destinationSoundFolderData;
	}

	public void setCurrentSoundFolderData(String currentSoundFolderData) {
		this.currentSoundFolderData = currentSoundFolderData;
	}

	public String [] getVoiceListEntries(String path){

		List<String> folders = new ArrayList<String>();

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles(); 

		//String [] folders = {""};


		for (int i = 0; i < listOfFiles.length; i++) 
		{

			if (listOfFiles[i].isDirectory()) 
			{
				folders.add(listOfFiles[i].getName());
				//folders.add(listOfFiles[i].getName());
				//System.out.println(files);
			}
		}
		String[] simpleArray = new String[ folders.size() ];
		return folders.toArray(simpleArray);
	}

	public String [] getVoiceListEntryValues(String path){

		List<String> folders = new ArrayList<String>();

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles(); 

		//String [] folders = {""};


		for (int i = 0; i < listOfFiles.length; i++) 
		{

			if (listOfFiles[i].isDirectory()) 
			{
				folders.add(listOfFiles[i].getAbsolutePath());
				//folders.add(listOfFiles[i].getName());
				//System.out.println(files);
			}
		}
		String[] simpleArray = new String[ folders.size() ];
		return folders.toArray(simpleArray);
	}
	
	public static void createDirIfNotExists(String path) {
	    //boolean ret = true;

	    File file = new File(path);
	    if (!file.exists()) {
	        if (!file.mkdirs()) {
	            System.out.println("Problem creating folder");
	            //ret = false;
	        }
	    }
	   // return ret;
	}
}
