package com.anmark.dialpad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SettingsPreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{


	private ListPreference voice; 
	private EditTextPreference source;
	private EditTextPreference storage;


	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);
		

		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

		voice = (ListPreference)getPreferenceScreen().findPreference("prefVoicelist");
		source = (EditTextPreference)getPreferenceScreen().findPreference("prefSource");
		storage = (EditTextPreference)getPreferenceScreen().findPreference("prefStorage");

		if(DialPadView.mExternalStorageAvailable) {
			
			
			System.out.println("mExternalStorageAvailable: " +DialPadView.mExternalStorageAvailable );
			
			String [] entries = getVoiceListEntries(Environment.getExternalStorageDirectory() + "/dialpad/sounds/");
			String [] entryValues = getVoiceListEntryValues(Environment.getExternalStorageDirectory() + "/dialpad/sounds/");
			voice.setEntries(entries);
			voice.setEntryValues(entryValues);
		

		//source.setDefaultValue("http://dt031g.programvaruteknik.nu/dialpad/sounds/");

		//storage.setText(Environment.getExternalStorageDirectory() + "/dialpad/sounds/");
		//source.setText("http://dt031g.programvaruteknik.nu/dialpad/sounds/");

		//storage.setDefaultValue(Environment.getExternalStorageDirectory() + "/dialpad/sounds/");
		
		}
		

		// set current input as summary
		if (voice.getEntry() != null) {
			voice.setSummary(voice.getEntry());
		}
		voice.setSummary(voice.getEntry());
		source.setSummary(source.getText());
		storage.setSummary(storage.getText());


		//voice.getValue();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub		  

		/*voice.setSummary(voice.getEntry());
		source.setSummary(source.getText());
		storage.setSummary(storage.getText());

		String [] entries = getVoiceListEntries(Environment.getExternalStorageDirectory() + "/dialpad/sounds/");
		String [] entryValues = getVoiceListEntryValues(Environment.getExternalStorageDirectory() + "/dialpad/sounds/");
		voice.setEntries(entries);
		voice.setEntryValues(entryValues);
		 */

		System.out.println("1 " +sharedPreferences.getString("prefVoicelist", ""));
		System.out.println("2 " +sharedPreferences.getString("prefSource", ""));
		System.out.println("3 " + sharedPreferences.getString("prefStorage", ""));

		updatePrefSummary(findPreference(key));

	}

	private void updatePrefSummary(Preference p) {
		if (p instanceof ListPreference) {
			ListPreference listPref = (ListPreference) p;
			p.setSummary(listPref.getEntry());
		}
		if (p instanceof EditTextPreference) {
			EditTextPreference editTextPref = (EditTextPreference) p;
			p.setSummary(editTextPref.getText());
		}
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
}