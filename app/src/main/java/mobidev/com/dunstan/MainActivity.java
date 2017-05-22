package mobidev.com.dunstan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView selectDoor, administrator, manageDoor, alerts, support, faq, website, settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectDoor = (TextView) findViewById(R.id.door_name_label);
        administrator = (TextView) findViewById(R.id.administrator_label);
        manageDoor = (TextView) findViewById(R.id.manage_door_label);
        alerts = (TextView) findViewById(R.id.alert_label);
        support = (TextView) findViewById(R.id.support_label);
        faq = (TextView) findViewById(R.id.faq_label);
        website = (TextView) findViewById(R.id.website_label);
        settings = (TextView) findViewById(R.id.setting_label);

        selectDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DoorListActivity.class);
                startActivity(i);
            }
        });
    }
}
