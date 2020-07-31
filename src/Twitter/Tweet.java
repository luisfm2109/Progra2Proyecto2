/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Twitter;

import java.util.Date;

/**
 *
 * @author Eduardo
 */
public final class Tweet implements Comparable<Tweet>{
    public String username;
    public String contenido;
    public Date fecha;
    private long date;
    
    public Tweet(String username, String contenido, Date fecha){
        this.username=username;
        this.contenido=contenido;
        this.fecha=fecha;
        date = fecha.getTime();
    }
    
    @Override
    public int compareTo(Tweet twit){
            return (this.fecha.after(twit.fecha) ? -1 :(this.fecha == twit.fecha ? 0 : 1));     
    }
}
