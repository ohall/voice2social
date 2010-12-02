package com.v2s;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class mediaSelect extends Activity implements
		TextToSpeech.OnInitListener {
	/** Called when the activity is first created. */

	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	private static final String INSTRUCTIONS = "Please say: Twitter, Facebook or Buzz";
	private TextToSpeech mTts;

	// flag lets us know if we've disabled voice recog in mainMenu
	private Boolean _speechIsDisabled = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mediaselect);

		// get bundle from intent and assign it to a string;
		Bundle incomingBundle = this.getIntent().getExtras();
		// _speechIsDisabled = incomingBundle.getBoolean("VR_DISABLED",false);

		Button twitterButton = (Button) findViewById(R.id.twitterButton);

		Button fbButton = (Button) findViewById(R.id.fbButton);

		Button buzzButton = (Button) findViewById(R.id.buzzButton);

		twitterButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// start review and send activity
				launchActivity(1);
			}
		});

		fbButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// start review and send activity
				launchActivity(2);
			}
		});

		buzzButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// start review and send activity
				launchActivity(3);
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
		// commandIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
		// "Speech recognition demo");
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

		case 1:
			bundle.putString("DEFAULTTEXT", "Twitter");
			intent = new Intent(this, onAir.class);
			break;
		case 2:
			bundle.putString("DEFAULTTEXT", "Facebook");
			intent = new Intent(this, onAir.class);
			break;
		case 3:
			bundle.putString("DEFAULTTEXT", "Buzz");
			intent = new Intent(this, onAir.class);
			break;

		}// end switch

		intent.putExtras(bundle);
		startActivity(intent);

	}// end launchActivity

	private void sayit(String x) {
		mTts.speak(x, TextToSpeech.QUEUE_FLUSH, null);
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
				selection = 1;
			} else if (selectCommand.compareToIgnoreCase("Facebook") == 0) {
				selection = 2;
			} else if (selectCommand.compareToIgnoreCase("Buzz") == 0) {
				selection = 3;
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

		if (!_speechIsDisabled) {

			if (status == TextToSpeech.SUCCESS) {

				int result = mTts.setLanguage(Locale.getDefault());

				if (result == TextToSpeech.LANG_MISSING_DATA
						|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
					mTts.setLanguage(Locale.US);
				}

				sayit(INSTRUCTIONS);

				startVoiceRecognitionActivity();
			}
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