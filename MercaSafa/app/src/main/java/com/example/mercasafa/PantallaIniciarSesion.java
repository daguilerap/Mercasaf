package com.example.mercasafa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PantallaIniciarSesion extends AppCompatActivity {
    ObjectOutputStream outObjeto;
    String mail;
    String nombreu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_iniciar_sesion);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        Button botonVolver = findViewById(R.id.btnIniciarSesionVolver);
        Button botonAceptar = findViewById(R.id.btnIniciarSesionAceptar);
        TextView campoUsu = findViewById(R.id.campoIniciarSesionUsuario);
        TextView campoPass = findViewById(R.id.campoIniciarSesionContraseña);

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(PantallaIniciarSesion.this,MainActivity.class);

                startActivity(i);
            }
        });
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = String.valueOf(campoUsu.getText());
                String pass = String.valueOf(campoPass.getText());
                Boolean registra=login(nombre,pass);
                if(registra==false) {
                    campoUsu.setText("");
                    campoPass.setText("");
                }else {
                    Intent i =  new Intent(PantallaIniciarSesion.this,Menu.class);
                    i.putExtra("usuario", nombreu);
                    i.putExtra("email",mail );
                    startActivity(i);
                }

            }
        });




    }
    private Boolean login(String nombre, String pass) {
        Boolean pasa=false;

        try {
            Socket sk;

            sk = new Socket("213.194.142.166", 10578);


            ClienteRegistro cl = new ClienteRegistro();
            cl.setNombre(nombre);
            cl.setPass(pass);
            cl.setAccion("login");
            // Se prepara un flujo de salida para objetos
            outObjeto = new ObjectOutputStream(sk.getOutputStream());
            // Se prepara un objeto y se env�a
            outObjeto.writeObject(cl);
            ObjectInputStream inObjeto;
            inObjeto = new ObjectInputStream(sk.getInputStream());

            cl = (ClienteRegistro) inObjeto.readObject();
            outObjeto.close();
            inObjeto.close();
            sk.close();
            mail=cl.getMail();
            nombreu=cl.getNombre();
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            cl.getAccion(), Toast.LENGTH_SHORT);
            if(cl.getAccion().equals("Login correcto")){
                pasa=true;
            }
            toast1.show();




        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(PantallaRegistro.class.getName()).log(Level.SEVERE, null, ex);
            pasa=false;
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Error de conexión", Toast.LENGTH_SHORT);

            toast1.show();


        }


        return pasa;



    }

}