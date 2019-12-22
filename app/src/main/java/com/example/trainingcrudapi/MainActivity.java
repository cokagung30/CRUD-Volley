package com.example.trainingcrudapi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.trainingcrudapi.adapter.StudentAdapter;
import com.example.trainingcrudapi.interfaces.StudentClickListener;
import com.example.trainingcrudapi.model.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements StudentClickListener {

    private RecyclerView rvStudent;
    private FloatingActionButton fabAddData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvStudent = findViewById(R.id.rv_student);
        fabAddData = findViewById(R.id.fab_add_data);

        fabAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddDataActivity.class);
                intent.putExtra("status", "added");
                startActivity(intent);
            }
        });

        rvStudent.setHasFixedSize(true);
        showData();
    }

    private void showData() {
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, BuildConfig.URL + "read.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Student> students = new ArrayList<>();
                    JSONArray array = response.getJSONArray("student");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject data = array.getJSONObject(i);
                        Student student = new Student();
                        student.setName(data.getString("name"));
                        student.setNim(data.getString("nim"));
                        student.setClasses(data.getString("class"));
                        Log.d("Name", "" + student.getName());
                        Log.d("NIM", "" + student.getNim());
                        Log.d("Class", "" + student.getClasses());
                        students.add(student);

                    }
                    StudentAdapter adapter = new StudentAdapter(students, MainActivity.this, MainActivity.this);
                    rvStudent.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonRequest);

    }

    @Override
    public void onClick(Integer position, ArrayList<Student> students) {
        Intent intent = new Intent(this, AddDataActivity.class);
        intent.putExtra("status", "update");
        intent.putExtra("nim", students.get(position).getNim());
        intent.putExtra("name", students.get(position).getName());
        intent.putExtra("classes", students.get(position).getClasses());
        startActivity(intent);
    }

    @Override
    public void longClick(Integer position, ArrayList<Student> students) {
        showDialogDelete(position, students);
    }

    private void showDialogDelete(final Integer position, final ArrayList<Student> students) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle("Hapus Data");
        alertDialogBuilder
                .setMessage("Apakah anda yakin menghapus data "+students.get(position).getName()+" ?")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        deleteData(position, students, dialog);
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteData(final Integer position, final ArrayList<Student> students, final DialogInterface dialog) {
        StringRequest request = new StringRequest(Request.Method.POST, BuildConfig.URL + "delete.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String message = object.getString("kondisi");
                    if (message.equals("Berhasil")) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Data Berhasil Dihapus !!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (message.equals("Gagal")) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Data Gagal Dihapus !!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("nim", students.get(position).getNim());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
