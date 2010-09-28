package com.ttsDemo2;

import android.app.Activity;
import android.os.Bundle;

/* this gives us Text to speech capability */
import android.speech.tts.TextToSpeech;

/* This is the path to R.java, which is an auto generated file in Eclipse */
import com.ttsDemo2.R;

/* for the view... and onClick functionality */
import android.view.View;

/* for the button widget */
import android.widget.Button;

/* for the Edit text widget */
import android.widget.EditText;

/* TextView */
import android.widget.TextView;

/* This is for the locale that's feed into TTS */
import java.util.Locale;


public class ttsDemo2 extends Activity implements TextToSpeech.OnInitListener {

// variables
	private TextToSpeech mTts;
	private Button mButton;
	private EditText mEditText;
	private TextView mTextView;

	// private String hello = "Say!";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Init TTS
		mTts = new TextToSpeech(this, this);

		// The button is disabled in the layout.
		// It will be enabled upon initialization of the TTS engine.
		
		// find the button in the view
		mButton = (Button) findViewById(R.id.button);
		// find textview for error messages
		mTextView = (TextView) findViewById(R.id.TextView01);
		mTextView.setText("no errors");
		mEditText = (EditText) findViewById(R.id.EditText01);
		mEditText.setText(R.string.hello);
		// mButton.setText(hello);

		// bind the button to submit data to sayit()
		mButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				sayit(mEditText.getText().toString());
			}
		});

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

	// Implements TextToSpeech.OnInitListener.
	public void onInit(int status) {
		// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
		if (status == TextToSpeech.SUCCESS) {

			// Use the default language, then if fail, fall back to US english.

			//mButton.setText(Locale.getDefault());

			mTextView.setText(Locale.getDefault().toString());
			
			int result = mTts.setLanguage(Locale.getDefault());
			

			//int result = mTts.setLanguage(Locale.US);
			//int result = mTts.setLanguage(Locale.IT);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				mTts.setLanguage(Locale.US);
			} 
				
				// Enable the button, for more presses
				mButton.setEnabled(true);
				// Say it once, before the user presses the button.
				sayit(mEditText.getText().toString());
			
		} else {
			// Initialization failed.
			mTextView.setText("Could not initialize TextToSpeech.");
		}
	}

	// the name says it all...
	private void sayit(String x) {
		mTts.speak(x, TextToSpeech.QUEUE_FLUSH, null);
	}

}