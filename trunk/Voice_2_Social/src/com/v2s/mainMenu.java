package com.v2s;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class mainMenu extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button reviewAndSendButton = 
        	(Button) findViewById(R.id.reviewAndSendButton);
        
        

	    reviewAndSendButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View view){
	    		//start review and send activity
	    		startReviewAndSendActivity();
	    	}
	    });


        }
    

	private void startReviewAndSendActivity() {
	Intent reviewAndSendIntent = new Intent(this , reviewAndSend.class);
	startActivity(reviewAndSendIntent);
	}

}
