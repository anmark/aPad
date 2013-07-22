package com.anmark.dialpad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
 
public class SettingsPreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{
 
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.settings);
        setupActionBar();
        
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        
        ListPreference voice = (ListPreference)getPreferenceScreen().findPreference("prefVoicelist");
        //  String [] entries = {"elin_se", "erik_se"};
        
       // String [] entryValues = {"/mnt/sdcard/dialpad/sounds/elin_se/", "/mnt/sdcard/dialpad/sounds/erik_se/"};
        
        String [] entries = getVoiceListEntries(Environment.getExternalStorageDirectory() + "/dialpad/sounds/");
        String [] entryValues = getVoiceListEntryValues(Environment.getExternalStorageDirectory() + "/dialpad/sounds/");
        voice.setEntries(entries);
        voice.setEntryValues(entryValues);
        
        
        //voice.getValue();
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
    
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Workaround: Does not restart activity
			finish();
			// This ID represents the Home or Up button
			//NavUtils.navigateUpFromSameTask(this);
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub
		
	}
}