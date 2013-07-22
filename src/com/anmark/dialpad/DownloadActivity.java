package com.anmark.dialpad;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DownloadActivity extends Activity {

	public String destinationSoundFolderData() {
		return destinationSoundFolderData;
	}
	public void destinationSoundFolderData(String destinationSoundFolderData) {
		this.destinationSoundFolderData = destinationSoundFolderData;
	}
	// declare the dialog as a member field of your activity
	ProgressDialog mProgressDialog;

	private String fileExtenstion;
	private String fileName;
	private String destinationSoundFolderData;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		// instantiate it within the onCreate method
		mProgressDialog = new ProgressDialog(DownloadActivity.this);
		mProgressDialog.setTitle("Downloading sounds");
		
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		Bundle extras = getIntent().getExtras();
		setContentView(R.layout.activity_download);	

		WebView myWebView = (WebView) findViewById(R.id.webview);
		myWebView.setWebViewClient(new MyWebViewClient());

		if (extras != null) {
			// load url
			
			if (extras.getString(DialPadView.soundUrl) != null) {
				myWebView.loadUrl(extras.getString(DialPadView.soundUrl));
			}
			if (extras.getString(DialPadView.destinationSoundFolder) != null) {
				destinationSoundFolderData = extras.getString(DialPadView.destinationSoundFolder) ;
			}
			
		}


		myWebView.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,long contentLength) {
				//Uri uri = Uri.parse(url);
				//System.out.println("download" + url + userAgent + contentDisposition + mimetype + contentLength);

				// execute this when the downloader must be fired
				DownloadFile downloadFile = new DownloadFile();
				fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(url.toString());
				fileName = URLUtil.guessFileName(url.toString(), null, fileExtenstion);
				mProgressDialog.setMessage(fileName);
				downloadFile.execute(url);


			}
		});

		/*
		PreferenceCategory mainCategory = new PreferenceCategory(this);  
		
		 CharSequence[] entries = {"Two", "One", "Other"};  
		
		
		
		final ListPreference listPref = new ListPreference(this);  
		 listPref.setKey("keyDevice"); // Refer to get the pref value  
		 listPref.setDefaultValue("One");  
		 listPref.setEntries(entries);  
		 listPref.setEntryValues(entries);  
		 listPref.setDialogTitle("Title");  
		 listPref.setTitle("Title 2");  
		 listPref.setSummary("Summary");  
		 mainCategory.addPreference(listPref); // Adding under the category  
		 listPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {  
		     public boolean onPreferenceChange(Preference preference, Object newValue) {  
		     //newValue will be the entryValue for the entry selected  
		     return true;  
		 }  
		 });  
		*/
		
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);
			return false;
			/*
			  if (Uri.parse(url).getHost().equals(DialPadView.soundUrlHost)) {
				  System.out.println("true");
	            // This is my web site, so do not override; let my WebView load the page
	            return false;
	        }
	        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
	        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	        startActivity(intent);
	        System.out.println("false");
	        return true;
			 */
		}

	}
	// usually, subclasses of AsyncTask are declared inside the activity class.
	// that way, you can easily modify the UI thread from here
	private class DownloadFile extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... sUrl) {
			try {
				URL url = new URL(sUrl[0]);
				URLConnection connection = url.openConnection();
				connection.connect();
				// this will be useful so that you can show a typical 0-100% progress bar
				int fileLength = connection.getContentLength();

				// download the file
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(destinationSoundFolderData + fileName);
				//System.out.println(connection.getFileNameMap());

				byte data[] = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			}
			catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			mProgressDialog.setProgress(progress[0]);
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			// unzip downloaded file into currently used directory
			ZIP.decompress(destinationSoundFolderData + fileName, destinationSoundFolderData);

			// Delete zip file after unzip
			File file = new File(destinationSoundFolderData + fileName);
			file.delete();
			
			//mProgressDialog.dismiss();
		}

	}
	
	private List<File> getListFiles(File parentDir) {
	    ArrayList<File> inFiles = new ArrayList<File>();
	    File[] files = parentDir.listFiles();
	    for (File file : files) {
	        if (file.isDirectory()) {
	            inFiles.addAll(getListFiles(file));
	        } else {
	            if(file.getName().endsWith(".csv")){
	                inFiles.add(file);
	            }
	        }
	    }
	    return inFiles;
	}
}
