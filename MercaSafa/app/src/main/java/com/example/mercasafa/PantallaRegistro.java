package com.example.mercasafa;

import java.io.*;
import java.net.*;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PantallaRegistro extends AppCompatActivity {



    ObjectOutputStream outObjeto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_registro);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button botonVolver = findViewById(R.id.btnRegistroVolver);
        Button botonAceptar = findViewById(R.id.btnRegistroAceptar);
        TextView regisUsu=findViewById(R.id.campoRegistroUsuario);
        TextView regiMail=findViewById(R.id.campoRegistroCorreo);
        TextView regiContra=findViewById(R.id.campoRegistroContraseña);
        TextView regiContraconf=findViewById(R.id.campoRegistroConfirmarContraseña);

        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");


        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(PantallaRegistro.this,MainActivity.class);

                startActivity(i);
            }
        });

        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = String.valueOf(regisUsu.getText());
                String mail = String.valueOf(regiMail.getText());
                String pass = String.valueOf(regiContra.getText());
                String passConfirm = String.valueOf(regiContraconf.getText());

                if(!nombre.equals("") && !mail.equals("") && !pass.equals("")) {
                    Matcher mather = pattern.matcher(mail);
                    if(mather.find() == true) {
                        if (pass.equals(passConfirm)) {
                            Boolean registra = probando(nombre, mail, pass);
                            if (registra) {
                                regisUsu.setText("");
                                regiMail.setText("");
                                regiContra.setText("");
                                regiContraconf.setText("");
                            }

                        } else {
                            Toast toastError = Toast.makeText(getApplicationContext(), "No coinciden las contraseñas", Toast.LENGTH_SHORT);
                            toastError.show();
                        }
                    }else{
                        Toast toastError = Toast.makeText(getApplicationContext(), "Introduce un mail valido", Toast.LENGTH_SHORT);
                        toastError.show();

                    }
                }else{
                    Toast toastError = Toast.makeText(getApplicationContext(), "Faltan campos por rellenar", Toast.LENGTH_SHORT);
                    toastError.show();

                }


            }
        });
    }
    private Boolean probando(String nombre, String mail, String pass) {
                    Boolean pasa=false;

                    try {
                        Socket sk;
                        sk = new Socket("213.194.142.166", 10578);

                        ClienteRegistro cl = new ClienteRegistro();
                        cl.setNombre(nombre);
                        cl.setMail(mail);
                        cl.setPass(pass);
                        cl.setAccion("registro");
                        // Se prepara un flujo de salida para objetos
                        outObjeto = new ObjectOutputStream(sk.getOutputStream());
                        // Se prepara un objeto y se env�a
                        outObjeto.writeObject(cl);
                        ObjectInputStream inObjeto;

                        inObjeto = new ObjectInputStream(sk.getInputStream());
                        cl = (ClienteRegistro) inObjeto.readObject();
                        sk.close();
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(),
                                        cl.getAccion(), Toast.LENGTH_SHORT);

                        toast1.show();
                        pasa=true;



                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(PantallaRegistro.class.getName()).log(Level.SEVERE, null, ex);
                        pasa=false;
                    }
                    return pasa;



    }
}