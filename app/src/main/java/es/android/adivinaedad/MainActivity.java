package es.android.adivinaedad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView edadText;
    private EditText tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edadText = findViewById(R.id.edad);
        tv = findViewById(R.id.nombre);

        findViewById(R.id.consultarEdad).setOnClickListener(view -> {
            mostrarEdad();
        });
    }

    private void mostrarEdad() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.agify.io/")
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().serializeNulls().create()
                ))
                .build();
        EdadAPIService edadApiService =
                retrofit.create(EdadAPIService.class);
        Call<EdadEstimada> call = edadApiService.getEdad(tv.getText());
        call.enqueue(new Callback<EdadEstimada>() {
            @Override
            public void onResponse(Call<EdadEstimada> call,
                                   Response<EdadEstimada> response) {
                try {
                    if (response.isSuccessful()) {
                        int edad = response.body().getAge();
                        edadText.setText(Integer.toString(edad));
                    } else {
                        edadText.setText("Ha ocurrido un error");
                        return;
                    }                }
                catch(Exception e) {
                    edadText.setText("No es un nombre Valido");
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Error", t.toString());
            }
        });
    }
}