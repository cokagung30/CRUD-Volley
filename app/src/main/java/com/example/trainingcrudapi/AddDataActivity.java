package com.example.trainingcrudapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class AddDataActivity extends AppCompatActivity {

    private Button btnAction;
    private TextView tvTitleActivity;
    private EditText edtNim, edtName, edtClass;
    private String status = null;
    private String name, nim, classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        status = getIntent().getStringExtra("status");

        btnAction = findViewById(R.id.btn_action);
        edtNim = findViewById(R.id.edt_nim);
        edtName = findViewById(R.id.edt_name);
        edtClass = findViewById(R.id.edt_class);
        tvTitleActivity = findViewById(R.id.tv_title_activity);

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });

        initActivity();
    }

    private void validation() {
        name = edtName.getText().toString();
        nim = edtNim.getText().toString();
        classes = edtClass.getText().toString();

        if (name.equals("") || name == null) {
            edtName.setError("Nama Kosong !!!");
            edtName.setFocusable(true);
        } else if (nim.equals("") || nim == null) {
            edtNim.setError("Nim Kosong !!!");
            edtNim.setFocusable(true);
        } else if (classes.equals("") || classes == null) {
            edtClass.setError("Class Kosong !!!");
            edtClass.setFocusable(true);
        } else {
            if (status.equals("update")) {
                updateAction(name, nim, classes);
            } else if (status.equals("added")) {
                addAction(name, nim, classes);
            }
        }
    }

    private void updateAction(final String name, final String nim, final String classes) {
        StringRequest request = new StringRequest(Request.Method.POST, BuildConfig.URL + "edit.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String message = object.getString("kondisi");
                    if (message.equals("Berhasil")) {
                        Toast.makeText(AddDataActivity.this, "Data Berhasil Diperbaharui !!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddDataActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (message.equals("Gagal")) {
                        Toast.makeText(AddDataActivity.this, "Data Gagal Diperbaharui !!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddDataActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("nama", name);
                param.put("kelas", classes);
                param.put("nim", nim);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void addAction(final String name, final String nim, final String classes) {
        StringRequest request = new StringRequest(Request.Method.POST, BuildConfig.URL + "add.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String message = object.getString("kondisi");
                    if (message.equals("Berhasil")) {
                        Toast.makeText(AddDataActivity.this, "Data Berhasil Ditambah !!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddDataActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (message.equals("Gagal")) {
                        Toast.makeText(AddDataActivity.this, "Data Gagal Ditambah !!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddDataActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("nama", name);
                param.put("kelas", classes);
                param.put("nim", nim);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void initActivity() {
        if (status.equals("update")) {
            edtNim.setEnabled(false);
            nim = getIntent().getStringExtra("nim");
            name = getIntent().getStringExtra("name");
            classes = getIntent().getStringExtra("classes");

            edtName.setText(name);
            edtNim.setText(nim);
            edtClass.setText(classes);
            tvTitleActivity.setText(getResources().getString(R.string.title_update));
            btnAction.setText(getResources().getString(R.string.title_update));
        } else if (status.equals("added")) {
            tvTitleActivity.setText(getResources().getString(R.string.title_added));
            btnAction.setText(getResources().getString(R.string.title_added));
        }
    }
}
