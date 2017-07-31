package ru.mbg.palbociclib.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.mbg.palbociclib.R;

public class ShortInstructionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_instruction);
        setNavigationButton();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void setNavigationButton(){
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

}
