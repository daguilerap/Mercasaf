package com.example.mercasafa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PantallaDetalleSeleccionPersonal extends AppCompatActivity {

    private ProgressDialog dialog;
    ObjectOutputStream outObjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_detalle_seleccion_personal);

        String usu = getIntent().getStringExtra("usuario");
        String obj = getIntent().getStringExtra("objeto");
        String des = getIntent().getStringExtra("descripcion");
        String imagen = getIntent().getStringExtra("img");
        Uri uri= Uri.parse(imagen);
        String contact = getIntent().getStringExtra("contacto");
        String ubi = getIntent().getStringExtra("ubicacion");
        dialog = new ProgressDialog(this);

        TextView nombreObjeto=findViewById(R.id.nombDeta);
        TextView detalleObjeto=findViewById(R.id.descDeta);
        ImageView img=findViewById(R.id.photopersonal);
        Button btnBorrar=findViewById(R.id.Borrar);
        Glide.with(PantallaDetalleSeleccionPersonal.this)
                .load(uri)
                .fitCenter()
                .centerCrop()
                .into(img);
        nombreObjeto.setText(obj);
        detalleObjeto.setText(des);

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // para borrar de surefire IMPORTANTE
                        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(ubi);

                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                                borrado(imagen);
                                Intent i =  new Intent(PantallaDetalleSeleccionPersonal.this,Menu.class);
                                i.putExtra("usuario",usu);

                                startActivity(i);


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                                borrado(imagen);
                                Intent i =  new Intent(PantallaDetalleSeleccionPersonal.this,Menu.class);
                                i.putExtra("usuario",usu);

                                startActivity(i);


                            }
                        });



            }
        });





    }


    private Boolean borrado(String imagen) {
        Boolean pasa=false;
        String acc;

        try {
            Socket sk;

            sk = new Socket("213.194.142.166", 10578);

            ClienteRegistro cl = new ClienteRegistro();
            cl.setUrlImg(imagen);

            cl.setAccion("borrado");
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
            if(cl.getAccion().equals("Borrado Correctamente")){
                pasa=true;
                toast1.show();
            }


        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(PantallaRegistro.class.getName()).log(Level.SEVERE, null, ex);
            pasa=false;
        }
        return pasa;



    }


}