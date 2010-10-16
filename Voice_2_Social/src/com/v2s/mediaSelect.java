package com.v2s;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class mediaSelect extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mediaselect);
        
        
        Button twitterButton = 
        	(Button) findViewById(R.id.twitterButton);
        
        Button fbButton = 
        	(Button) findViewById(R.id.fbButton);
        
        Button buzzButton = 
        	(Button) findViewById(R.id.buzzButton);
        
        twitterButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View view){
        		//start review and send activity
        		launchActivity(1);
        	}
        });
    	
        fbButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View view){
        		//start review and send activity
        		launchActivity(2);
        	}
        });
        
        buzzButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View view){
        		//start review and send activity
        		launchActivity(3);
        	}
        });
        
    }
    
    
    
    private void launchActivity(int activity) {
    	
		Intent intent = null;
		Bundle bundle = new Bundle();
	
		switch(activity){
		
			case 1:
				bundle.putString("DEFAULTTEXT", "Twitter");
				intent = new Intent(this , onAir.class);
				break;
			case 2:
				bundle.putString("DEFAULTTEXT", "Facebook");
				intent = new Intent(this , onAir.class);
				break;
			case 3:
				bundle.putString("DEFAULTTEXT", "Google Buzz");
				intent = new Intent(this , onAir.class);
				break;
				
		}//end switch
		
		intent.putExtras(bundle);
		startActivity(intent);
	
	}//end launchActivity
    
    
    
    
}