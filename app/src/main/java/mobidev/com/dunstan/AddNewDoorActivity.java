package mobidev.com.dunstan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddNewDoorActivity extends AppCompatActivity {

    TextView addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_door);

        addBtn = (TextView)findViewById(R.id.add_door_label);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddNewDoorActivity.this, SecurityQuestionActivity.class);
                startActivity(i);

                finish();
            }
        });
    }
}
