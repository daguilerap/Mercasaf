package com.example.mercasafa;
import java.io.Serializable;

public class ClienteRegistro implements Serializable {

    private String nombre;
    private String pass;
    private String mail;
    private String accion;
    private String nombreObj;
    private String urlImg;
    private String gsFirebase;
    private String descript;

    public ClienteRegistro(String nombre, String pass, String mail, String accion, String nombreObj, String urlImg, String gsFirebase,String descript) {

        this.nombre = nombre;
        this.pass = pass;
        this.mail = mail;
        this.accion=accion;
        this.nombreObj=nombreObj;
        this.urlImg=urlImg;
        this.gsFirebase=gsFirebase;
        this.descript=descript;
    }
    public ClienteRegistro() {

    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getNombreObj() {
        return nombreObj;
    }

    public void setNombreObj(String nombreObj) {
        this.nombreObj = nombreObj;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getGsFirebase() {
        return gsFirebase;
    }

    public void setGsFirebase(String gsFirebase) {
        this.gsFirebase = gsFirebase;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
