package com.example.mercasafa;

import android.net.Uri;

public class Objetos {
    private String nombre;
    private Uri url;
    private String descripcion;
    private String email;
    private String ubicacion;

    public Objetos(String nombre, Uri url, String descripcion,String email,String ubicacion) {
        this.nombre = nombre;
        this.url = url;
        this.descripcion = descripcion;
        this.email = email;
        this.ubicacion = ubicacion;

    }
    public Objetos(){

    }


    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uri getUrl() {
        return url;
    }

    public void setUrl(Uri url) {
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
