package com.anmark.dialpad;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;

public class DialPadView extends TableLayout implements OnClickListener{

	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;

	private int sound1ID, sound2ID, sound3ID,sound4ID,sound5ID,sound6ID,sound7ID, sound8ID, sound9ID, soundSID, sound0ID, soundPID;

	boolean loaded = true;
	
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

	@SuppressLint("UseSparseArrays")
	public void init(Context context){

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
		
		// Initialize sounds
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
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

	}
	
	/*
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			MediaPlayer.create(context, R.raw.one).start();
			break;
		case R.id.button2:
			MediaPlayer.create(context, R.raw.two).start();
			break;
		case R.id.button3:
			MediaPlayer.create(context, R.raw.three).start();
			break;
		case R.id.button4:
			MediaPlayer.create(context, R.raw.four).start();
			break;
		case R.id.button5:
			MediaPlayer.create(context, R.raw.five).start();
			break;
		case R.id.button6:
			MediaPlayer.create(context, R.raw.six).start();
			break;
		case R.id.button7:
			MediaPlayer.create(context, R.raw.seven).start();
			break;
		case R.id.button8:
			MediaPlayer.create(context, R.raw.eight).start();
			break;
		case R.id.button9:
			MediaPlayer.create(context, R.raw.nine).start();
			break;
		case R.id.buttonS:
			MediaPlayer.create(context, R.raw.star).start();
			break;
		case R.id.button0:
			MediaPlayer.create(context, R.raw.zero).start();
			break;
		case R.id.buttonP:
			MediaPlayer.create(context, R.raw.pound).start();
			break;
		}

	}
	 */

	public void onClick(View v) {
		if(loaded){
			
			// Get user sound settings
			AudioManager mgr = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
			int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);

			switch (v.getId()) {
			case R.id.button1:
				soundPool.play(sound1ID, 50, 50, 1, 0, 1f);
				break;
			case R.id.button2:
				soundPool.play(sound2ID, streamVolume, streamVolume, 1, 0, 1f);
				break;
			case R.id.button3:
				soundPool.play(sound3ID, streamVolume, streamVolume, 1, 0, 1f);
				break;
			case R.id.button4:
				soundPool.play(sound4ID, streamVolume, streamVolume, 1, 0, 1f);
				break;
			case R.id.button5:
				soundPool.play(sound5ID, streamVolume, streamVolume, 1, 0, 1f);
				break;
			case R.id.button6:
				soundPool.play(sound6ID, streamVolume, streamVolume, 1, 0, 1f);
				break;
			case R.id.button7:
				soundPool.play(sound7ID, streamVolume, streamVolume, 1, 0, 1f);
				break;
			case R.id.button8:
				soundPool.play(sound8ID, streamVolume, streamVolume, 1, 0, 1f);
				break;
			case R.id.button9:
				soundPool.play(sound9ID, streamVolume, streamVolume, 1, 0, 1f);
				break;
			case R.id.buttonS:
				soundPool.play(soundSID, streamVolume, streamVolume, 1, 0, 1f);
				break;
			case R.id.button0:
				soundPool.play(sound0ID, streamVolume, streamVolume, 1, 0, 1f);
				break;
			case R.id.buttonP:
				soundPool.play(soundPID, streamVolume, streamVolume, 1, 0, 1f);
				break;
			}
		}
	}
}
