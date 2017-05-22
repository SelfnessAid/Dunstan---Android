package mobidev.com.dunstan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import mobidev.com.dunstan.classes.APIManager;
import mobidev.com.dunstan.classes.AppManager;
import okhttp3.Response;

public class AddNewDoorActivity extends AppCompatActivity {

    TextView addBtn;
    EditText doorNameEdit, phoneEdit, doorCodeEdit, passwordEdit, emailEdit;
    ProgressDialog dialog;
    String errorStr = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_door);

        addBtn = (TextView)findViewById(R.id.add_door_label);
        doorNameEdit = (EditText)findViewById(R.id.door_name_edit);
        phoneEdit = (EditText)findViewById(R.id.phone_edit);
        doorCodeEdit = (EditText)findViewById(R.id.door_code_edit);
        passwordEdit = (EditText)findViewById(R.id.app_password_edit);
        emailEdit = (EditText)findViewById(R.id.email_edit);

        doorNameEdit.setText("New Door");
        phoneEdit.setText("+442039284948");
        doorCodeEdit.setText("12341234");
        passwordEdit.setText("password");
        emailEdit.setText("man@test.com");

        dialog = new ProgressDialog(AddNewDoorActivity.this, R.style.MyAlertDialogStyle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = doorNameEdit.getText().toString();
                String phoneStr = phoneEdit.getText().toString();
                String codeStr = doorCodeEdit.getText().toString();
                String passwordStr = passwordEdit.getText().toString();
                String emailStr = emailEdit.getText().toString();

                if (nameStr.isEmpty() || phoneStr.isEmpty() || codeStr.isEmpty() || passwordStr.isEmpty() || emailStr.isEmpty()) {
                    showAlert("Notice", "Please fill in all fields!");
                    return;
                }

                if (!isValidEmail(emailStr)) {
                    showAlert("Notice", "Invalid Email Address");
                    return;
                }

                final SharedPreferences preferences = getSharedPreferences("dunstan", Context.MODE_PRIVATE);
                Boolean gsm = preferences.getBoolean("GSM", false);
                if (gsm) {
                    showAlert("Notice", "GSM is blocked now. Please enable it on Administrator");
                    return;
                }

                dialog.show();
                dialog.setContentView(R.layout.custom_progress_dialog);

                APIManager.getInstance().addNewDoor(nameStr, phoneStr, codeStr, passwordStr, emailStr, new APIManager.APISuccessListener() {
                    @Override
                    public void onFailure(String error) {
                        dialog.dismiss();
                        errorStr = error;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showAlert("Notice", errorStr);
                            }
                        });
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
                                    String tokenStr = new JSONObject(resultString).getString("token");
                                    AppManager appData = (AppManager) getApplication();
                                    appData.setToken(tokenStr);
                                    Boolean isLoggedIn = preferences.getBoolean("loggedIn", false);
                                    if (isLoggedIn) {
                                        Intent i = new Intent(AddNewDoorActivity.this, MainActivity.class);
                                        startActivity(i);
                                    } else {
                                        Intent i = new Intent(AddNewDoorActivity.this, SecurityQuestionActivity.class);
                                        startActivity(i);
                                    }
                                } else {
                                    errorStr = resultString;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showAlert("Notice", errorStr);
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                try {
                                    errorStr = new JSONObject(res).getString("error");
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showAlert("Notice", errorStr);
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            errorStr = e.getLocalizedMessage();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showAlert("Notice", errorStr);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
                .show();
    }
}
