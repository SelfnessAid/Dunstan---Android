package mobidev.com.dunstan;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FirstSetupActivity extends AppCompatActivity {

    TextView firstSetupTextView;
    ImageView firstSetupLogoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setup);
        firstSetupTextView = (TextView)findViewById(R.id.first_step_btn);
        firstSetupLogoView = (ImageView) findViewById(R.id.app_setting);

        firstSetupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstSetupActivity.this, AddNewDoorActivity.class);
                startActivity(i);
            }
        });

        firstSetupLogoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstSetupActivity.this, AddNewDoorActivity.class);
                startActivity(i);
            }
        });
    }
}
