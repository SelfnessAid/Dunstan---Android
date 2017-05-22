package mobidev.com.dunstan;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mobidev.com.dunstan.classes.APIManager;
import mobidev.com.dunstan.classes.AppManager;
import mobidev.com.dunstan.classes.Door;
import okhttp3.Response;

public class DoorListActivity extends AppCompatActivity {

    ListView doorList;
    private ProgressDialog dialog;
    String errorStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_list);

        dialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getDoors();

        doorList = (ListView) findViewById(R.id.door_list);
        doorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getDoors() {
        dialog.show();
        dialog.setContentView(R.layout.custom_progress_dialog);
        AppManager appData = (AppManager) getApplication();
        APIManager.getInstance().getDoors(appData.getToken(), new APIManager.APISuccessListener() {
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
                            progressData(jsonObject.getJSONArray("data"));
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
                        errorStr = e.getLocalizedMessage();
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

    private void progressData(JSONArray data) {
        AppManager appManager = (AppManager) getApplication();
        appManager.getDoors().clear();
        for (int i=0; i<data.length(); i++) {
            try {
                JSONObject item = data.getJSONObject(i);
                Door door = new Door();
                door.id = item.getString("id");
                door.number = item.getString("number");
                door.code = item.getString("code");
                door.state = item.getString("status");
                door.name = item.getString("name");
                door.password = item.getString("password");

                appManager.getDoors().add(door);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
