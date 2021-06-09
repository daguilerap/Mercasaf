package com.example.mercasafa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        String usu = getIntent().getStringExtra("usuario");
        String em = getIntent().getStringExtra("email");


        Button nuevoObj = findViewById(R.id.btnNueviObj);
        Button listar = findViewById(R.id.btnListarObj);
        Button listarPer = findViewById(R.id.btnListaPer);


        nuevoObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =  new Intent(Menu.this,nuevoObjeto.class);
                i.putExtra("usuario", usu);
                i.putExtra("email",em );
                startActivity(i);

            }
        });
        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =  new Intent(Menu.this,pantallaSeleccion.class);
                i.putExtra("usuario", usu);
                i.putExtra("email",em );
                startActivity(i);

            }
        });

        listarPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =  new Intent(Menu.this,pantallaSeleccionPersonal.class);
                i.putExtra("usuario", usu);
                i.putExtra("email",em );
                startActivity(i);

            }
        });
    }



}