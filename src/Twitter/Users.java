/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Twitter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Eduardo
 */
public class Users {
    private File users;
    private RandomAccessFile rusers;
    private ArrayList<String> encontrarusuarios;
    
    public Users(){
        try{
            users = new File("Accounts");
            users.mkdir();
            
            rusers = new RandomAccessFile("Accounts/users.twc","rw");
            encontrarusuarios = new ArrayList();
        }catch(IOException e){
            System.out.println("Esto no deberia pasar");
        }
    }
    
    /*
    Estructura:
    String nombre completo
    String username
    String password
    int edad
    char genero
    Long fecha de entrada
    boolean cuenta activa
    */
    
    public boolean buscarUser(String username)throws IOException{
        rusers.seek(0);
        while(rusers.getFilePointer()<rusers.length()){
            rusers.readUTF();
            String usuario = rusers.readUTF();
            rusers.readUTF();
            rusers.readInt();
            rusers.readChar();
            rusers.readLong();
            rusers.readBoolean();
            if(usuario.equalsIgnoreCase(username)){
                return true;
            }
        }
        return false;
    }
    
    public boolean crearUser(String nombre, String username, String password, int edad, char genero)throws IOException{
        if(!buscarUser(username)){
            rusers.seek(rusers.length());
            rusers.writeUTF(nombre);
            rusers.writeUTF(username);
            rusers.writeUTF(password);
            rusers.writeInt(edad);
            rusers.writeChar(genero);
            rusers.writeLong(Calendar.getInstance().getTimeInMillis());
            rusers.writeBoolean(true);
            
            createUserFolder(username);
        }
        return false;
    }
    
    private String userFolder(String username){
        return "Accounts/"+username;
    }
    
    public RandomAccessFile userFollowing(String username)throws IOException{
        return new RandomAccessFile(userFolder(username)+"/following.twc","rw");
    }
    
    public RandomAccessFile userFollowers(String username)throws IOException{
        return new RandomAccessFile(userFolder(username)+"/followers.twc","rw");
    }
    
    public RandomAccessFile userTwits(String username)throws IOException{
        return new RandomAccessFile(userFolder(username)+"/twits.twc","rw");
    }
    
    private void createUserFolder(String username)throws IOException{
        File udir = new File(userFolder(username));
        udir.mkdir();
        
        RandomAccessFile following = userFollowing(username);
        RandomAccessFile followers = userFollowers(username);
        RandomAccessFile twits = userTwits(username);
    }
    
    public boolean login(String username, String password)throws IOException{
        rusers.seek(0);
        while(rusers.getFilePointer()<rusers.length()){
            rusers.readUTF();
            String usuario = rusers.readUTF();
            String pass = rusers.readUTF();
            rusers.readInt();
            rusers.readChar();
            rusers.readLong();
            rusers.readBoolean();
            if(usuario.equalsIgnoreCase(username) && pass.equals(password)){
                return true;
            }
        }
        return false;
    }
    
    public String encontrarUsers(String cadena, String currentuser)throws IOException{
        rusers.seek(0);
        String encontrados="";
        encontrarusuarios.clear();
        int comparacion=0;
        while(rusers.getFilePointer()<rusers.length()){
            rusers.readUTF();
            String usuario = rusers.readUTF();
            rusers.readUTF();
            rusers.readInt();
            rusers.readChar();
            rusers.readLong();
            boolean active = rusers.readBoolean();
            if(!usuario.equals(currentuser) && active){
                compare: for(int i=0;i<usuario.length();i++){
                    for(int j=0;j<cadena.length();j++){
                        if(usuario.substring(i).length()<cadena.length())
                            break compare;

                        if(cadena.charAt(j)==usuario.charAt(i+j)){
                            comparacion++;
                            if(comparacion==cadena.length())
                                break compare;
                        }else{
                            comparacion=0;
                            break;
                        }
                    }
                comparacion=0;
                }
                if(comparacion==cadena.length()){
                    encontrarusuarios.add(usuario);
                    encontrados+=usuario+"\n";
                }
            }
        }
        return encontrados;
    }
    
    public ArrayList<String> lista(String algo, String currentuser)throws IOException{
        encontrarUsers(algo,currentuser);
        return encontrarusuarios;
    }
    
    public ArrayList<String> getActiveUsers()throws IOException{
        ArrayList<String> userList = new ArrayList();
        rusers.seek(0);
        while(rusers.getFilePointer()<rusers.length()){
            rusers.readUTF();
            String usuario = rusers.readUTF();
            rusers.readUTF();
            rusers.readInt();
            rusers.readChar();
            rusers.readLong();
            boolean active = rusers.readBoolean();
            if(active){
                userList.add(usuario);
            }
        }
        return userList;
    }
    
    public Date getAntiquity(String currentuser)throws IOException{
        rusers.seek(0);
        long fechainicio=0;
        while(rusers.getFilePointer()<rusers.length()){
            rusers.readUTF();
            String usuario = rusers.readUTF();
            rusers.readUTF();
            rusers.readInt();
            rusers.readChar();
            long inicio = rusers.readLong();
            rusers.readBoolean();
            if(currentuser.equals(usuario)){
                fechainicio=inicio;
            }
        }
        Date fecha = new Date(fechainicio);
        return fecha;
    }
    
    public boolean estadoCuenta(String currentuser)throws IOException{
        rusers.seek(0);
        while(rusers.getFilePointer()<rusers.length()){
            rusers.readUTF();
            String usuario = rusers.readUTF();
            rusers.readUTF();
            rusers.readInt();
            rusers.readChar();
            rusers.readLong();
            boolean active = rusers.readBoolean();
            if(usuario.equalsIgnoreCase(currentuser)){
                return active;
            }
        }
        return false;
    }
    
    public void cambiarEstadoCuenta(String currentuser)throws IOException{
        rusers.seek(0);
        while(rusers.getFilePointer()<rusers.length()){
            rusers.readUTF();
            String usuario = rusers.readUTF();
            rusers.readUTF();
            rusers.readInt();
            rusers.readChar();
            rusers.readLong();
            long pos = rusers.getFilePointer();
            boolean active = rusers.readBoolean();
            if(usuario.equalsIgnoreCase(currentuser)){
                rusers.seek(pos);
                if(active)
                    rusers.writeBoolean(false);
                else
                    rusers.writeBoolean(true);
                
                break;
            }
        }
    }
}
