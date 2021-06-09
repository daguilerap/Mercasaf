package com.example.mercasafa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class pantallaSeleccion extends AppCompatActivity {

    private ObjectOutputStream outObjeto;
    Adapter.RecyclerViewClickListener listener;
    List<Objetos> mutList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_seleccion);


        cargarLista();




        }
    private void cargarLista() {
        int n=0;

        ClienteRegistro cl = new ClienteRegistro();


        try {
            Socket sk;
            sk = new Socket("213.194.142.166", 10578);



            cl.setAccion("listaGeneral");

            // Se prepara un flujo de salida para objetos
            outObjeto = new ObjectOutputStream(sk.getOutputStream());
            // Se prepara un objeto y se env�a
            outObjeto.writeObject(cl);

            //entrada numero de objetos
            DataInputStream flujoEntrada;
            InputStream entrada = null;
            entrada = sk.getInputStream();
            flujoEntrada = new DataInputStream(entrada);
            n=flujoEntrada.readInt();

            //sacamos todo lo referente con la bbdd y pasamos un objeto aparte

            ObjectInputStream inObjeto = new ObjectInputStream(sk.getInputStream());

            while(n>0) {

                cl = (ClienteRegistro) inObjeto.readObject();
                Objetos ob= new Objetos();
                ob.setNombre(cl.getNombreObj());
                ob.setDescripcion(cl.getDescript());
                ob.setUrl(Uri.parse(cl.getUrlImg()));
                ob.setEmail(cl.getMail());

                mutList.add(ob);
                n--;
            }

            entrada.close();
            flujoEntrada.close();
            inObjeto.close();
            outObjeto.close();


            setOnClickListener();
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            Adapter adapter = new Adapter(mutList, getApplication(),listener);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));





            sk.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }




    private void setOnClickListener() {
        listener = new Adapter.RecyclerViewClickListener() {
            @Override
            public void OnClick(View v, int position) {

                Intent i =  new Intent(pantallaSeleccion.this,detalleObjeto.class);
                i.putExtra("objeto", mutList.get(position).getNombre());
                i.putExtra("descripcion",mutList.get(position).getDescripcion());
                i.putExtra("img",mutList.get(position).getUrl().toString());
                i.putExtra("contacto",mutList.get(position).getEmail());
                startActivity(i);

            }
        };

    }
}
