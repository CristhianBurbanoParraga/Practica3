package com.example.practica3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.practica3.Interface.JsonPlaceHolderApi;
import com.example.practica3.Model.Posts;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView JsonText;
    private String urlApiPost = "https://jsonplaceholder.typicode.com/";
    private String urlApiContact = "https://api.androidhive.info/contacts/";
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JsonText = findViewById(R.id.txtJsonText);

        //getPosts();
    }

    public void getRetrofit(View view){
        JsonText.setText("");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlApiPost)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Posts>> call = jsonPlaceHolderApi.getPosts();
        call.enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                try {
                    if (response.isSuccessful()) {
                        List<Posts> postsList = response.body();
                        for (Posts post: postsList){
                            String content = "";
                            content += "userId: " + post.getUserId() + "\n";
                            content += "id: " + post.getId() + "\n";
                            content += "title: " + post.getTitle() + "\n";
                            content += "body: " + post.getBody() + "\n\n";
                            JsonText.append(content);
                        }
                    }
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Posts>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getVolley(View view){
        JsonText.setText("");
        queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlApiContact, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("contacts");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        //Toast.makeText(MainActivity.this, "Nombre: " + name, Toast.LENGTH_SHORT).show();
                        JsonText.append("Nombre: " + name + "\n");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }
}