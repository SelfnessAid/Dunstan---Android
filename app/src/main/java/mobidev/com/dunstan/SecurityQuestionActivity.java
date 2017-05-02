package mobidev.com.dunstan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SecurityQuestionActivity extends AppCompatActivity {

    TextView continueBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);
        continueBtn = (TextView) findViewById(R.id.continue_label);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SecurityQuestionActivity.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        });
    }
}
