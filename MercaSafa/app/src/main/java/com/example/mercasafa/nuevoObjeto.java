package com.example.mercasafa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.FileDownloadTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class nuevoObjeto extends AppCompatActivity {

    private ImageView imagen;
    private Button buscaImage;
    private Button subir;
    private Uri path;
    private String imageUrl;
    private String gsFb;
    private StorageReference mStorage;
    private ProgressDialog dialog;
    ObjectOutputStream outObjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_objeto);
        String usu = getIntent().getStringExtra("usuario");
        String em = getIntent().getStringExtra("email");

        mStorage = FirebaseStorage.getInstance().getReference();

        imagen = (ImageView) findViewById(R.id.ImagenObj);
        buscaImage = (Button) findViewById(R.id.btnbuscaImage);
        subir = (Button) findViewById(R.id.btnSubirObj);
        dialog = new ProgressDialog(this);
        TextView nombreObjeto=findViewById(R.id.textNuevoObj);
        TextView descripcion=findViewById(R.id.txtDescripccion);


        buscaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cargarImagen();


            }
        });

        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre = nombreObjeto.getText().toString();
                String descrip = descripcion.getText().toString();
                if(!nombre.equals("")&&!descrip.equals("")) {

                    if (path != null) {

                        dialog.setTitle("Subiendo...");
                        dialog.setMessage("Procesando la petición");
                        dialog.setCancelable(false);
                        dialog.show();
                        //subir imagen a firebase

                        StorageReference filepath = mStorage.child("fotos").child(path.getLastPathSegment());
                        filepath.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                dialog.dismiss();

                                if (taskSnapshot.getMetadata() != null) {
                                    if (taskSnapshot.getMetadata().getReference() != null) {
                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                gsFb = taskSnapshot.getMetadata().getReference().toString();

                                                //string de la imagen que se encuentra en firebase
                                                imageUrl = uri.toString();
                                                //createNewPost(imageUrl);

                                                /*
                                                Glide.with(nuevoObjeto.this)
                                                        .load(uri)
                                                        .fitCenter()
                                                        .centerCrop()
                                                        .into(imagen);

                                                 */

                                                try {
                                                    cargarObjeto(usu, nombreObjeto.getText().toString(), descripcion.getText().toString());




                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                    Toast toast1 =
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Error al subir", Toast.LENGTH_SHORT);

                                                    toast1.show();
                                                } catch (ClassNotFoundException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                    }
                                }


                            }
                        });

                    } else {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(),
                                        "Debe seleccionar una imagen", Toast.LENGTH_SHORT);

                        toast1.show();

                    }
                }else {
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "Faltan campos por rellenar", Toast.LENGTH_SHORT);

                    toast1.show();

                }


            }
        });


    }

    private void cargarObjeto(String usu,String nombreObjeto, String descripcion) throws IOException, ClassNotFoundException {
        Socket sk;
        sk = new Socket("213.194.142.166", 10578);

        ClienteRegistro cl = new ClienteRegistro();

        cl.setAccion("nuevoObj");
        cl.setNombre(usu);
        cl.setNombreObj(nombreObjeto);
        cl.setDescript(descripcion);
        cl.setUrlImg(imageUrl);
        cl.setGsFirebase(gsFb);

        // Se prepara un flujo de salida para objetos
        outObjeto = new ObjectOutputStream(sk.getOutputStream());
        // Se prepara un objeto y se env�a
        outObjeto.writeObject(cl);
        ObjectInputStream inObjeto;
        inObjeto = new ObjectInputStream(sk.getInputStream());
        cl = (ClienteRegistro) inObjeto.readObject();


        if(cl.getAccion().equals("Subido correctamente")){
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            cl.getAccion(), Toast.LENGTH_SHORT);
            toast1.show();
        }else {
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Error al subir el objeto", Toast.LENGTH_SHORT);
            toast1.show();

        }



        sk.close();

    }




    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicación"),10);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //muestra la imagen del la galeria

        if(resultCode==RESULT_OK){
            path=data.getData();
            imagen.setImageURI(path);

        }


    }
}