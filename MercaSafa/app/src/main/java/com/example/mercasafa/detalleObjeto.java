package com.example.mercasafa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class detalleObjeto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_objeto);

        String obj = getIntent().getStringExtra("objeto");
        String des = getIntent().getStringExtra("descripcion");
        String imagen = getIntent().getStringExtra("img");
        Uri uri = Uri.parse(imagen);
        String contact = getIntent().getStringExtra("contacto");

        TextView campoNombreObje = findViewById(R.id.textNombreObj);
        TextView campoDescripcion = findViewById(R.id.textDetalle);
        ImageView campoImage = findViewById(R.id.photoObj);
        TextView campocontacto = findViewById(R.id.textContacto);


        campoNombreObje.setText(obj);
        campoDescripcion.setText(des);

        Glide.with(detalleObjeto.this)
                .load(uri)
                .fitCenter()
                .centerCrop()
                .into(campoImage);
        //campoImage.setImageURI(uri);
        campocontacto.setText(contact);


        Button botonContactar = findViewById(R.id.btnContactar);


        botonContactar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", contact, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my subject text");
                startActivity(Intent.createChooser(emailIntent, null));


            }
        });

    }


}