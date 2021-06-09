import com.example.mercasafa.ClienteRegistro;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.logging.*;
import java.sql.SQLException;

public class ServidorHilo extends Thread {
    private Socket socket;
    private DataOutputStream flujoSalida;
    private DataInputStream flujoEntrada;

    private InputStream entrada;
    private int idSessio;
    static String  ENCRYPT_KEY="0123456789abcdef";
    public ServidorHilo(Socket socket) {
        this.socket = socket;

        try {
            flujoSalida = new DataOutputStream(socket.getOutputStream());
            flujoEntrada = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void desconnectar() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void registro(ClienteRegistro cr) throws IOException {

        try {

            PreparedStatement preparedStatment;
            Conexion conex = new Conexion();
            String sql = "INSERT INTO registro (usuario,password,email) VALUES(?,?,?)";
            preparedStatment = conex.getConnection().prepareStatement(sql);


            preparedStatment.setString(1, cr.getNombre());
            preparedStatment.setString(2, cr.getPass());
            preparedStatment.setString(3, cr.getMail());

        preparedStatment.executeUpdate();

            preparedStatment.close();


            cr.setAccion("Registro correcto");

            // Se prepara un flujo de salida para objetos
            ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
            // Se prepara un objeto y se env�a
            outObjeto.writeObject(cr);
            outObjeto.close();


        } catch (SQLException | IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Numero repetido");
            cr.setAccion("El nombre de usuario ya existe");

            // Se prepara un flujo de salida para objetos
            ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
            // Se prepara un objeto y se env�a
            outObjeto.writeObject(cr);
            outObjeto.close();
        }


    }
    public void login(ClienteRegistro cr){

        try {

            Conexion conex = new Conexion();
            Statement st = conex.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM registro where usuario='"+(cr.getNombre())+"' and password='"+(cr.getPass())+"'");

            ClienteRegistro cr2 = new ClienteRegistro();
            while (rs.next()) {
                cr2.setNombre(rs.getString("usuario"));
                cr2.setPass(rs.getString("password"));
                cr2.setMail(rs.getString("email"));
            }
            rs.close();
            st.close();




            if(cr2.getNombre()==null){
                cr2.setAccion("Login incorrecto");
            }else {
                cr2.setAccion("Login correcto");
            }


            // Se prepara un flujo de salida para objetos
            ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
            // Se prepara un objeto y se env�a
            outObjeto.writeObject(cr2);
            outObjeto.close();


        } catch (SQLException | IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Numero repetido");
        }


    }

    private void subirObj(ClienteRegistro cr) {


        try {
            PreparedStatement preparedStatment;
            Conexion conex = new Conexion();
            String sql = "INSERT INTO objetos (nombre,descripcion,urlimagen,direccionfb,usuarioobj) VALUES(?,?,?,?,?)";
            preparedStatment = conex.getConnection().prepareStatement(sql);

            preparedStatment.setString(1, cr.getNombreObj());
            preparedStatment.setString(2, cr.getDescript());
            preparedStatment.setString(3, cr.getUrlImg());
            preparedStatment.setString(4, cr.getGsFirebase());
            preparedStatment.setString(5, cr.getNombre());

            preparedStatment.executeUpdate();

            preparedStatment.close();


            cr.setAccion("Subido correctamente");

            // Se prepara un flujo de salida para objetos
            ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
            // Se prepara un objeto y se env�a
            outObjeto.writeObject(cr);
            outObjeto.close();


        } catch (SQLException | IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error al subir");


        }


    }
    private String encripta(ClienteRegistro cr) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Key aesKey = new SecretKeySpec(ENCRYPT_KEY.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);

        byte[] encrypted = cipher.doFinal(cr.getPass().getBytes());
        return Base64.encode(encrypted);
    }

    private String desencrip(String contraencr) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] encryptedBytes=Base64.decode( contraencr.replace("\n", "") );

        Key aesKey = new SecretKeySpec(ENCRYPT_KEY.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);

        String decrypted = new String(cipher.doFinal(encryptedBytes));

        return decrypted;
    }

    @Override
    public void run() {

        //desde aqui se operan todas las recepciones de objetos de los hilos

            try {
                ObjectInputStream inObjeto;

                inObjeto = new ObjectInputStream(socket.getInputStream());
                ClienteRegistro cr = (ClienteRegistro) inObjeto.readObject();
                String accion =cr.getAccion();
                if(accion.equals("registro")){
                    registro(cr);
                }else if(accion.equals("login")){
                    login(cr);
                }else if(accion.equals("nuevoObj")){
                    subirObj(cr);
                }else if(accion.equals("listaGeneral")){
                    cargarlista();
                }else if(accion.equals("listaPersonal")){
                    String usuario =cr.getNombre();
                    cargarlistaPer(usuario);
                }else if(accion.equals("borrado")){
                    String img =cr.getUrlImg();
                    Borrar(img);
                }
                inObjeto.close();
                desconnectar();


            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
            }


    }

    private void Borrar(String img) throws IOException {
        try {
            Conexion conex = new Conexion();
            Statement st = conex.getConnection().createStatement();

            ResultSet rs=st.executeQuery("select idobjetos from objetos where urlimagen=" + "'" + img + "'");
            int id = 0;
            while (rs.next()) {

                id=(rs.getInt("idobjetos"));
            }
            st.executeUpdate("delete from objetos where idobjetos=" + id);



            ClienteRegistro cr2 = new ClienteRegistro();
            cr2.setAccion("Borrado Correctamente");
            ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
            // Se prepara un objeto y se env�a
            outObjeto.writeObject(cr2);
            outObjeto.close();

        }catch (SQLException | IOException e){
            ClienteRegistro cr2 = new ClienteRegistro();
            cr2.setAccion("Error al borrar");
            ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
            // Se prepara un objeto y se env�a
            outObjeto.writeObject(cr2);
            outObjeto.close();

        }


    }

    private void cargarlistaPer(String usuario) {

        int n=0;

        try {
            Conexion conex = new Conexion();
            Statement st = conex.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT count(*) FROM objetos where usuarioobj="+"'"+usuario+"'");


            if(rs.next()) {
                //Si hay resultados obtengo el valor.
                n= rs.getInt(1);
            }


            ArrayList<ClienteRegistro> listado = new ArrayList<>();
            //tenemos que enviar n

            rs = st.executeQuery("SELECT * FROM objetos join registro where usuarioobj=usuario and usuarioobj="+"'"+usuario+"'");

            DataOutputStream flujoSalida;
            OutputStream salida = null;
            salida = socket.getOutputStream();
            flujoSalida = new DataOutputStream(salida);
            System.out.println("Enviamos el conteo de objeto ... "+n);
            flujoSalida.writeInt(n);
            ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());

            while (rs.next()) {

                ClienteRegistro cr2 = new ClienteRegistro();
                cr2.setNombreObj(rs.getString("nombre"));
                cr2.setDescript(rs.getString("descripcion"));
                cr2.setUrlImg(rs.getString("urlimagen"));
                cr2.setGsFirebase(rs.getString("direccionfb"));
                cr2.setNombre(rs.getString("usuarioobj"));
                cr2.setMail(rs.getString("email"));
                // Se prepara un flujo de salida para objetos

                // Se prepara un objeto y se env�a
                outObjeto.writeObject(cr2);

                //listado.add(cr2);

            }
            salida.close();
            flujoSalida.close();

            rs.close();
            st.close();
            outObjeto.close();

            System.out.println(n);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }


    }

    private void cargarlista() {
        int n=0;

        try {
            Conexion conex = new Conexion();
            Statement st = conex.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT count(*) FROM objetos");

            if(rs.next()) {
                //Si hay resultados obtengo el valor.
                n= rs.getInt(1);
            }


            ArrayList<ClienteRegistro> listado = new ArrayList<>();
            //tenemos que enviar n

            rs = st.executeQuery("SELECT * FROM objetos join registro where usuarioobj=usuario");

            DataOutputStream flujoSalida;
            OutputStream salida = null;
            salida = socket.getOutputStream();
            flujoSalida = new DataOutputStream(salida);
            System.out.println("Enviamos el conteo de objeto ... "+n);
            flujoSalida.writeInt(n);
            ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
            while (rs.next()) {
                ClienteRegistro cr2 = new ClienteRegistro();
                cr2.setNombreObj(rs.getString("nombre"));
                cr2.setDescript(rs.getString("descripcion"));
                cr2.setUrlImg(rs.getString("urlimagen"));
                cr2.setGsFirebase(rs.getString("direccionfb"));
                cr2.setNombre(rs.getString("usuarioobj"));
                cr2.setMail(rs.getString("email"));
                // Se prepara un flujo de salida para objetos

                // Se prepara un objeto y se env�a
                outObjeto.writeObject(cr2);

            }


            salida.close();
            flujoSalida.close();

            rs.close();
            st.close();
            outObjeto.close();


            System.out.println(n);
            } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }


}