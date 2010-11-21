package com.v2s;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class reviewAndSend extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewandsend);

        Button reRecordButton = 
        	(Button) findViewById(R.id.reRecordButton);
        
        Button acceptButton = 
        	(Button) findViewById(R.id.acceptButton);
        
        reRecordButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View view){
	    		lauchActivity(1);
	    	}
	    });
        
        acceptButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View view){
	    		lauchActivity(2);
	    	}
	    });
        
        
        //get bundle from intent and assign it to a string;
        Bundle incomingBundle = this.getIntent().getExtras();
        String text = incomingBundle.getString("DEFAULTTEXT");
        
       
        TextView textForReview = (TextView) findViewById(R.id.textForReview);

        //assign string to our textview
        textForReview.setText(text);
        
    }
    
    private void lauchActivity(int activity){
    	
    	Intent intent = null;
		Bundle bundle = new Bundle();
	
		switch(activity){
		
			case 1:
				//bundle.putString("DEFAULTTEXT", "Twitter");
				intent = new Intent(this , mainMenu.class);
				break;
			case 2:
				//bundle.putString("DEFAULTTEXT", "Facebook");
				intent = new Intent(this , onAir.class);
				break;
//			case 3:
//				bundle.putString("DEFAULTTEXT", "Google Buzz");
//				intent = new Intent(this , onAir.class);
//				break;
				
		}//end switch
		
		//intent.putExtras(bundle);
		startActivity(intent);
    	
    }
}