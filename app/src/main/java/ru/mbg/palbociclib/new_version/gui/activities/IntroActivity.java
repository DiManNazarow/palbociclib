package ru.mbg.palbociclib.new_version.gui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ru.mbg.palbociclib.R;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        findViewById(R.id.i_am_a_doctor_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.i_am_not_a_doctor_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, NotForPatientActivity.class);
                startActivity(intent);
            }
        });


    }

}
