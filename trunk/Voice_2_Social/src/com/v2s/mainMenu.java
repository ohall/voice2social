package com.v2s;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class mainMenu extends Activity implements TextToSpeech.OnInitListener,
TextToSpeech.OnUtteranceCompletedListener {

	private static final 	int VOICE_RECOGNITION_REQUEST_CODE 	= 1234;
	private static final 	String MAIN_MENU_INSTRUCTIONS 		= "Please press button or say: media to continue";
	private static final 	int GOTO_MEDIA_SELECT 				= 1;
	private static final 	int GOTO_REVIEW_SEND 				= 4;
	private static final 	int GOTO_VIEW_FRIENDS 				= 5;
	private static final 	int QUIT 							= 6;
	
	private		SharedPreferences prefs;
	private 	Boolean voiceEnabled;
	private 	TextToSpeech mTts;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button mediaSelectButton = (Button) findViewById(R.id.mediaSelectButton);
		
		prefs=PreferenceManager.getDefaultSharedPreferences(this);
//		prefs.registerOnSharedPreferenceChangeListener(prefListener);
		// read prefs for voice (keep it here)
		voiceEnabled = prefs.getBoolean("voice_on", false);
		

		/*
		 * Set Button Click Listeners
		 */
		mediaSelectButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// start review and send activity
				launchActivity(GOTO_MEDIA_SELECT);
			}
		});


	
		/**
		 * Text to Speech for Instructions
		 */
		mTts = new TextToSpeech(this, this); // init text to speech

	}// end onCreate

	private void launchActivity(int activity) {

		Intent intent = null;
		Bundle b = null;

		switch (activity) {
		case GOTO_MEDIA_SELECT:
			intent = new Intent(this, mediaSelect.class);
			break;
		case GOTO_REVIEW_SEND:
			if (b == null) {
				b = new Bundle();
			}
			b.putString("DEFAULTTEXT",
							"ATTENTION DEVELOPER!  NO TEXT INPUT IF SELECTED FROM MAIN MENU!!");
			intent = new Intent(this, reviewAndSend.class);
			intent.putExtras(b);
			break;
		case GOTO_VIEW_FRIENDS:
			intent = new Intent(this, viewFriends.class);
			break;
		case QUIT:
			onTerminate();
			break;
		}// end switch

		if (b != null) {
			intent.putExtras(b);			
		}
		startActivity(intent);

	}// end launchActivity

	private void startVoiceRecognitionActivity() {
		
		if(voiceEnabled){
			Intent commandIntent = new Intent(
					RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			commandIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
	        try {
	        	startActivityForResult(commandIntent, VOICE_RECOGNITION_REQUEST_CODE);
	        } catch (ActivityNotFoundException e) {
	        	// say the exception!!! :-)
	        	sayit("Voice recognizer not present!");
	        	//prefs.edit().putBoolean("voice_on", false);
	        	//Toast.makeText(this, "No voice recognizer!", Toast.LENGTH_SHORT).show();
	        	voiceEnabled = false;
	        }
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		int selection = 0;

		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {

			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String selectCommand = "";

			selectCommand = matches.get(0);

			if (selectCommand.compareToIgnoreCase("media") == 0) {
				selection = GOTO_MEDIA_SELECT;
			} else if (selectCommand.compareToIgnoreCase("friends") == 0) {
				selection = GOTO_VIEW_FRIENDS;
			} else if (selectCommand.compareToIgnoreCase("quit") == 0) {
				selection = QUIT;
			}

			super.onActivityResult(requestCode, resultCode, data);

			if (selection != 0) {
				launchActivity(selection);
			} else {
				if(voiceEnabled){
					startVoiceRecognitionActivity();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(getApplication()).inflate(R.menu.option, menu);

		return(super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.prefs) {
			//sayit("do the prefs now!");
			startActivity(new Intent(this, prefsActivity.class));
			
			return(true);
		}
		return(super.onOptionsItemSelected(item));
	}		
	
	private SharedPreferences.OnSharedPreferenceChangeListener prefListener=
		new SharedPreferences.OnSharedPreferenceChangeListener() {
		public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
			if (key.equals("user") || key.equals("password")) {
				//resetClient();
			}
		}
	};
	
	private void sayit(String x) {

		if(voiceEnabled){
			HashMap<String, String> myHashAlarm = new HashMap();
			myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
			        String.valueOf(AudioManager.STREAM_ALARM));		
			
			mTts.setOnUtteranceCompletedListener(this);
			myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
			        String.valueOf(AudioManager.STREAM_ALARM));
			myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
			        "end of voice intructions");
			// myHashAlarm now contains two optional parameters
			mTts.speak(x, TextToSpeech.QUEUE_ADD, myHashAlarm);
		}
	}
	
	public void onUtteranceCompleted(String uttId) {
		/* if voice recognition disabled or not present, there's no point in wasting CPU */	
		//if(voiceEnabled){
				startVoiceRecognitionActivity();
		//}
	}

	@Override
	public void onInit(int status) {

		if (status == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(Locale.getDefault());
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				mTts.setLanguage(Locale.US);
			}
			sayit(MAIN_MENU_INSTRUCTIONS);
		}
	}

	@Override
	public void onDestroy() {
		// kill it!
		if (mTts != null) {
			mTts.stop();
			mTts.shutdown();
		}
		super.onDestroy();
	}
	
	public void onTerminate() {	  
        onDestroy();
        this.finish(); // close the application
    }

}// end activity