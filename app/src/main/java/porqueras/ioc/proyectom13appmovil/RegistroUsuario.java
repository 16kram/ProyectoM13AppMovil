package porqueras.ioc.proyectom13appmovil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import porqueras.ioc.proyectom13appmovil.modelos.UsuarioResponse;
import porqueras.ioc.proyectom13appmovil.utilidades.ApiUtils;
import porqueras.ioc.proyectom13appmovil.utilidades.InstanciaRetrofit;
import porqueras.ioc.proyectom13appmovil.utilidades.NullConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroUsuario extends AppCompatActivity {
    EditText nombre, apellidos, telefono, email, direccion, username, password, confirmPassword;
    Button botonRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        //Añadimos el título de la Activity en la barra superior
        setTitle("Registro de usuarios");

        //Añadimos los campos de texto y los botones
        nombre = (EditText) findViewById(R.id.editTextNombre);
        apellidos = (EditText) findViewById(R.id.editTextApellidos);
        telefono = (EditText) findViewById(R.id.editTextTelefono);
        email = (EditText) findViewById(R.id.editTextTelefono);
        direccion = (EditText) findViewById(R.id.editTextDireccion);
        username = (EditText) findViewById(R.id.editTextUserName);
        password = (EditText) findViewById(R.id.editTextPassword);
        confirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        botonRegistro = (Button) findViewById(R.id.buttonRegistro);

        //Instanciomos la incerfaz de APIService mediante Retrofit
        APIService apiService= InstanciaRetrofit.getApiService();

        //Acción del botón Añadir
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si el campo del usuario o la contraseña está vacío muestra un Toast
                if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, "El usuario o la contraseña esta vacío", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    //Si las contraseñas no coinciden muestra un Toast
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    //Las contraseñas coinciden
                    UsuarioResponse user = new UsuarioResponse(
                            email.getText().toString(),
                            username.getText().toString(),
                            password.getText().toString(),
                            nombre.getText().toString(),
                            apellidos.getText().toString(),
                            telefono.getText().toString(),
                            direccion.getText().toString()
                            );
                    Call<UsuarioResponse> callUsuario = apiService.setUsuario(user);

                    callUsuario.enqueue(new Callback<UsuarioResponse>() {
                        @Override
                        public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                            if (response.isSuccessful()) {
                                Log.d("response", "Response nuevo usuario=" + response.code());
                                //Se muestra un Toast conforme el usuario se ha añadido correctamente
                                Context context = getApplicationContext();
                                Toast toast = Toast.makeText(context, "El usuario "+username.getText()+" se ha añadido correctamente", Toast.LENGTH_SHORT);
                                toast.show();
                                //Salimos de la actividad
                                finish();
                            } else {
                                Log.d("response", "Ocurrió un error en la petición de usuario-->" + response.code());
                            }

                        }

                        @Override
                        public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                            Log.d("response", "Error de red-->" + t.getMessage());
                        }
                    });
                }
            }
        });
    }
}