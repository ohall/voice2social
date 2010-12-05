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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class mediaSelect extends Activity implements TextToSpeech.OnInitListener,TextToSpeech.OnUtteranceCompletedListener {
	/** Called when the activity is first created. */

	private static final 	int VOICE_RECOGNITION_REQUEST_CODE 	= 1234;
	private static final 	String INSTRUCTIONS 				= "Please say: Twitter, Facebook or Buzz";
	private static final	int GOTO_TWITTER					= 1;
	private static final 	int GOTO_FACEBOOK 					= 2;
	private static final 	int GOTO_BUZZ 						= 3;
	private 				TextToSpeech mTts;
	private 				SharedPreferences prefs;
	private 				Boolean voiceEnabled;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mediaselect);

		// get bundle from intent and assign it to a string;
		Bundle incomingBundle = this.getIntent().getExtras();
		Button twitterButton = (Button) findViewById(R.id.twitterButton);
		Button fbButton = (Button) findViewById(R.id.fbButton);
		Button buzzButton = (Button) findViewById(R.id.buzzButton);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		voiceEnabled = prefs.getBoolean("voice_on", false);

		twitterButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// start review and send activity
				launchActivity(GOTO_TWITTER);
			}
		});

		fbButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// start review and send activity
				launchActivity(GOTO_FACEBOOK);
			}
		});

		buzzButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// start review and send activity
				launchActivity(GOTO_BUZZ);
			}
		});

		/**
		 * Text to Speech for Instructions
		 */
		mTts = new TextToSpeech(this, this); // init text to speech

	}

	private void startVoiceRecognitionActivity() {

		Intent commandIntent = new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		commandIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        try {
        	startActivityForResult(commandIntent, VOICE_RECOGNITION_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
        	// say the exception!!! :-)
        	sayit("Voice recognizer not present!");
        }
	}

	private void launchActivity(int activity) {

		Intent intent = null;
		Bundle bundle = new Bundle();

		switch (activity) {

		case GOTO_TWITTER:
			bundle.putString("DEFAULTTEXT", "Twitter");
			intent = new Intent(this, onAir.class);
			break;
		case GOTO_FACEBOOK:
			bundle.putString("DEFAULTTEXT", "Facebook");
			intent = new Intent(this, onAir.class);
			break;
		case GOTO_BUZZ:
			bundle.putString("DEFAULTTEXT", "Buzz");
			intent = new Intent(this, onAir.class);
			break;

		}// end switch

		intent.putExtras(bundle);
		startActivity(intent);

	}// end launchActivity

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
		startVoiceRecognitionActivity();
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

			if (selectCommand.compareToIgnoreCase("Twitter") == 0) {
				selection = GOTO_TWITTER;
			} else if (selectCommand.compareToIgnoreCase("Facebook") == 0) {
				selection = GOTO_FACEBOOK;
			} else if (selectCommand.compareToIgnoreCase("Buzz") == 0) {
				selection = GOTO_BUZZ;
			}

			super.onActivityResult(requestCode, resultCode, data);

			if (selection != 0) {
				launchActivity(selection);
			} else {
				startVoiceRecognitionActivity();
			}
		}
	}

	@Override
	public void onInit(int status) {

			if (status == TextToSpeech.SUCCESS) {
				int result = mTts.setLanguage(Locale.getDefault());
				if (result == TextToSpeech.LANG_MISSING_DATA
						|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
					mTts.setLanguage(Locale.US);
				}
				sayit(INSTRUCTIONS);
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
}