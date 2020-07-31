/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Twitter;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author Eduardo
 */
public class Twits{
    public final int caracteresMaximo=140;
    public Users user;
    private final ArrayList<Tweet> tweets;
    
    
    public Twits(){
        user = new Users();
        tweets = new ArrayList();
    }
    public boolean mandarTweer(String contenido, String username)throws IOException{
        if(contenido.length()<=caracteresMaximo){
            RandomAccessFile twit = user.userTwits(username);
            twit.seek(twit.length());
            twit.writeUTF(username);
            twit.writeUTF(contenido);
            twit.writeLong(Calendar.getInstance().getTimeInMillis());
            return true;
        }
        return false;
    }
    
    public ArrayList<Tweet> timeLine(String username) throws IOException{
        tweets.clear();
        if(user.estadoCuenta(username)){
            RandomAccessFile twit = user.userTwits(username);
            twit.seek(0);
            while(twit.getFilePointer()<twit.length()){
                String usuario = twit.readUTF();
                String contenido = twit.readUTF();
                Date fecha = new Date(twit.readLong());

                tweets.add(new Tweet(usuario,contenido,fecha));

            }
            RandomAccessFile following = user.userFollowing(username);
            ArrayList<String> isfollowing = new ArrayList();
            following.seek(0);
            while(following.getFilePointer()<following.length()){
                String target = following.readUTF();
                boolean isfollower = following.readBoolean();
                if(isfollower){
                    isfollowing.add(target);
                }
            }
            for(String followUser: isfollowing){
                twit = user.userTwits(followUser);
                twit.seek(0);
                while(twit.getFilePointer()<twit.length()){
                    String usuario = twit.readUTF();
                    String contenido = twit.readUTF();
                    Date fecha = new Date(twit.readLong());
                    if(user.estadoCuenta(usuario))
                        tweets.add(new Tweet(usuario,contenido,fecha));

                }
            }
        }
        Collections.sort(tweets);
        return tweets;
    }
    
    public ArrayList<Tweet> getTweets(String username) throws IOException{
        tweets.clear();
        RandomAccessFile twit = user.userTwits(username);
        twit.seek(0);
        while(twit.getFilePointer()<twit.length()){
            String usuario = twit.readUTF();
            String contenido = twit.readUTF();
            Date fecha = new Date(twit.readLong());
            
            tweets.add(new Tweet(usuario,contenido,fecha));
            
        }
        Collections.sort(tweets);
        return tweets;
    }
    
    public ArrayList<Tweet> getMentions(String username) throws IOException{
        tweets.clear();
        if(user.estadoCuenta(username)){
            String nombre = "@"+username;
            int comparacion=0;
            for(String s: user.getActiveUsers()){
                RandomAccessFile twit = user.userTwits(s);
                twit.seek(0);
                while(twit.getFilePointer()<twit.length()){
                    String usuario = twit.readUTF();
                    String contenido = twit.readUTF();
                    Date fecha = new Date(twit.readLong());

                    compare: for(int i=0;i<contenido.length();i++){
                        for(int j=0;j<nombre.length();j++){
                            if(contenido.substring(i).length()<nombre.length())
                                break compare;

                            if(nombre.charAt(j)==contenido.charAt(i+j)){
                                comparacion++;
                                if(comparacion==nombre.length())
                                    break compare;
                            }else{
                                comparacion=0;
                                break;
                            }
                        }
                    comparacion=0;
                    }
                    if(comparacion==nombre.length()){
                        tweets.add(new Tweet(usuario,contenido,fecha));
                    }

                }
            }
        }
        Collections.sort(tweets);
        return tweets;
    }
    
    public ArrayList<Tweet> getHash(String hashtag) throws IOException{
        tweets.clear();
        String nombre = "";
        if(hashtag.charAt(0)=='#')
            nombre=hashtag;
        else
            nombre="#"+hashtag;
        int comparacion=0;
        for(String s: user.getActiveUsers()){
            RandomAccessFile twit = user.userTwits(s);
            twit.seek(0);
            while(twit.getFilePointer()<twit.length()){
                String usuario = twit.readUTF();
                String contenido = twit.readUTF();
                Date fecha = new Date(twit.readLong());

                if(contenido.contains(nombre) && user.estadoCuenta(usuario)){
                    tweets.add(new Tweet(usuario,contenido,fecha));
                }

            }
        }
        
        Collections.sort(tweets);
        return tweets;
    }
}
