package mobidev.com.dunstan;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import mobidev.com.dunstan.classes.APIManager;
import mobidev.com.dunstan.classes.AppManager;
import mobidev.com.dunstan.classes.Question;
import okhttp3.Response;

public class SecurityQuestionActivity extends AppCompatActivity {

    TextView continueBtn;
    Spinner first_question, second_question, third_question;
    EditText answer1, answer2, answer3;
    ProgressDialog dialog;
    ArrayAdapter<String> adapter;
    JSONArray answers = new JSONArray();

    private ArrayList<String> questions = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);

        continueBtn = (TextView) findViewById(R.id.continue_label);
        first_question = (Spinner)findViewById(R.id.first_spinner);
        second_question = (Spinner)findViewById(R.id.second_spinner);
        third_question = (Spinner)findViewById(R.id.third_spinner);
        answer1 = (EditText) findViewById(R.id.answer1);
        answer2 = (EditText) findViewById(R.id.answer2);
        answer3 = (EditText) findViewById(R.id.answer3);

        dialog = new ProgressDialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getDoors();

        adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, questions);
        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        first_question.setAdapter(adapter);
        second_question.setAdapter(adapter);
        third_question.setAdapter(adapter);
        first_question.setOnItemSelectedListener(new MyOnItemSelectedListener());
        second_question.setOnItemSelectedListener(new MyOnItemSelectedListener());
        third_question.setOnItemSelectedListener(new MyOnItemSelectedListener());

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check()) {
                    return;
                }

                AppManager appManager = (AppManager) getApplication();

                dialog.show();
                dialog.setContentView(R.layout.custom_progress_dialog);
                APIManager.getInstance().saveAnswers(appManager.getToken(), answers, new APIManager.APISuccessListener() {
                    @Override
                    public void onFailure(String error) {
                        dialog.dismiss();
                        showAlert("Notice", error);
                    }

                    @Override
                    public void onSuccess(Response response) {
                        dialog.dismiss();
                        try {
                            String res = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                String resultString = jsonObject.getString("data");
                                if (jsonObject.getInt("status") == 1) {
                                    Intent i = new Intent(SecurityQuestionActivity.this, MainActivity.class);
                                    startActivity(i);
                                } else {
                                    showAlert("Error", resultString);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                showAlert("Error", e.getLocalizedMessage());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            showAlert("Error", e.getLocalizedMessage());
                        }
                    }
                });
                Intent i = new Intent(SecurityQuestionActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private String getIDFromQuestions(String value) {
        AppManager appManager = (AppManager) getApplication();
        for (int i=0; i<appManager.getQuestions().size(); i++) {
            Question question = appManager.getQuestions().get(i);
            if (value == question.question)
                return question.id;
        }
        return "";
    }

    private void collectAnswers(String question_id, String answer) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("question_id", question_id);
            obj.put("answer", answer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        answers.put(obj);
    }

    private boolean check() {
        String answer1Str = answer1.getText().toString();
        String answer2Str = answer2.getText().toString();
        String answer3Str = answer3.getText().toString();

        if (answer1Str.isEmpty() || answer2Str.isEmpty() || answer3Str.isEmpty()) {
            showAlert("Notice", "Please answer all questions");
            return false;
        }
        String question1ID = getIDFromQuestions(first_question.getSelectedItem().toString());
        collectAnswers(question1ID, answer1Str);
        String question2ID = getIDFromQuestions(second_question.getSelectedItem().toString());
        collectAnswers(question2ID, answer2Str);
        String question3ID = getIDFromQuestions(third_question.getSelectedItem().toString());
        collectAnswers(question3ID, answer3Str);
        return true;
    }

    private void getDoors(){
        dialog.show();
        dialog.setContentView(R.layout.custom_progress_dialog);
        APIManager.getInstance().getQuestions(new APIManager.APISuccessListener() {
            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                showAlert("Notice", error);
            }

            @Override
            public void onSuccess(Response response) {
                dialog.dismiss();
                try {
                    String res = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String resultString = jsonObject.getString("data");
                        if (jsonObject.getInt("status") == 1) {
                            progressData(jsonObject.getJSONArray("data"));
                        } else {
                            showAlert("Error", resultString);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showAlert("Error", e.getLocalizedMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", e.getLocalizedMessage());
                }
            }
        });
    }

    private void progressData(JSONArray data) {
        questions.clear();
        AppManager appManager = (AppManager) getApplication();
        appManager.getQuestions().clear();
        for (int i=0; i<data.length(); i++) {
            try {
                JSONObject item = data.getJSONObject(i);
                Question question = new Question();
                question.id = item.getString("id");
                question.question = item.getString("question");
                questions.add(question.question);
                appManager.getQuestions().add(question);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String string = parent.getItemAtPosition(pos).toString();
            Log.e("------------", string);
        }

        public void onNothingSelected(AdapterView parent) {

        }
    }
}
