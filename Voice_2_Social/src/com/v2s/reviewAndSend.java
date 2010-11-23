package com.v2s;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class reviewAndSend extends Activity implements TextToSpeech.OnInitListener {
	
	private EditText textForReview;
    private Button speakButton;
    private TextToSpeech mTts; 
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewandsend);
        //get bundle from intent and assign it to a string;
        Bundle incomingBundle = this.getIntent().getExtras();
        String text = incomingBundle.getString("DEFAULTTEXT");
        
        textForReview = (EditText) findViewById(R.id.EditText01);

        //assign string to our textview
        textForReview.setText(text);
        
        speakButton = 
        	(Button) findViewById(R.id.button1);
        
        speakButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View view){
	    		sayit(textForReview.getText().toString());
	    	}
	    	});
        mTts = new TextToSpeech(this,this); //init text to speech
    }
    
    private void sayit(String x) {
		mTts.speak(x, TextToSpeech.QUEUE_FLUSH, null);
    }
	
    
 //   @Override
	public void onInit(int status) {
		
		if (status == TextToSpeech.SUCCESS) {
			
			int result = mTts.setLanguage(Locale.getDefault());
			
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				mTts.setLanguage(Locale.US);
			} 
		//	test=textForReview;
			sayit(textForReview.getText().toString());
			
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
	    		
        
       
        
       
        