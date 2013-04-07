package com.chipotlebanditos.spacebanditos.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.chipotlebanditos.spacebanditos.R;

public class SettingsMenuActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);
    }
    
    public void onReturnButtonClick(View view) {
        finish();
    }
}
