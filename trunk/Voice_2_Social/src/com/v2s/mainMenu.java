package com.v2s;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.ActivityNotFoundException;

public class mainMenu extends Activity implements TextToSpeech.OnInitListener {

	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	private static final String MAIN_MENU_INSTRUCTIONS = "Please say: Media, New User, Friends or Disable.";

	private TextToSpeech mTts;

	// flag lets us know if we've disabled voice recog in mainMenu
	private Boolean _speechIsDisabled = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/*
		 * Create Buttons
		 */
		Button mediaSelectButton = (Button) findViewById(R.id.mediaSelectButton);

		Button newUserLoginButton = (Button) findViewById(R.id.newUserLoginButton);

		Button viewFriends = (Button) findViewById(R.id.viewFriends);

		Button reviewAndSendButton = (Button) findViewById(R.id.reviewAndSendButton);

		/*
		 * Set Button Click Listeners
		 */
		mediaSelectButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// start review and send activity
				launchActivity(1);
			}
		});

		newUserLoginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// start review and send activity
				launchActivity(3);
			}
		});

		reviewAndSendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// start review and send activity
				launchActivity(4);
			}
		});

		viewFriends.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// start review and send activity
				launchActivity(5);
			}
		});

		/**
		 * Text to Speech for Instructions
		 */
		mTts = new TextToSpeech(this, this); // init text to speech

	}// end onCreate

	private void launchActivity(int activity) {

		Intent intent = null;
		Boolean _noLaunch = false;
		Bundle b = null;

		switch (activity) {
		case 1:
			intent = new Intent(this, mediaSelect.class);
			break;
		case 3:
			intent = new Intent(this, newUserLogin.class);
			break;
		case 4:
			if (b == null) {
				b = new Bundle();
			}
			b
					.putString("DEFAULTTEXT",
							"ATTENTION DEVELOPER!  NO TEXT INPUT IF SELECTED FROM MAIN MENU!!");
			intent = new Intent(this, reviewAndSend.class);
			intent.putExtras(b);
			break;
		case 5:
			intent = new Intent(this, viewFriends.class);
			break;
		case 6:
			_speechIsDisabled = true;
			if (b == null) {
				b = new Bundle();
			}
			b.putBoolean("VR_DISABLED", true);
			_noLaunch = true;
			break;
		}// end switch

		if (_noLaunch) {
			_noLaunch = false;
		} else {
			if (b != null) {
				intent.putExtras(b);
			}
			startActivity(intent);
		}

	}// end launchActivity

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
				selection = 1;
			} else if (selectCommand.compareToIgnoreCase("new user") == 0) {
				selection = 3;
			} else if (selectCommand.compareToIgnoreCase("friends") == 0) {
				selection = 5;
			} else if (selectCommand.compareToIgnoreCase("disable") == 0) {
				selection = 6;
			}

			super.onActivityResult(requestCode, resultCode, data);

			if (selection != 0) {
				launchActivity(selection);
			} else {
				startVoiceRecognitionActivity();
			}
		}

	}

	private void sayit(String x) {
		mTts.speak(x, TextToSpeech.QUEUE_FLUSH, null);
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

			startVoiceRecognitionActivity();
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

}// end activity