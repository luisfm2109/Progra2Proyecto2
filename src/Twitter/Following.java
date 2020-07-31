/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Twitter;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Eduardo
 */
public class Following {
    RandomAccessFile following;
    RandomAccessFile followers;
    Users user = new Users();
    
    private boolean isInFollowing(String currentuser, String target) throws IOException{
        following = user.userFollowing(currentuser);
        following.seek(0);
        while(following.getFilePointer()<following.length()){
            String targetuser = following.readUTF();
            boolean followinguser = following.readBoolean();
            if(targetuser.equals(target)){
                return true;
            }
        }
        return false;
    }
    
    private boolean isInFollowers(String target, String currentuser)throws IOException{
        followers = user.userFollowers(target);
        followers.seek(0);
        while(followers.getFilePointer()<followers.length()){
            String thisuser = followers.readUTF();
            boolean isfollower = followers.readBoolean();
            if(thisuser.equals(currentuser)){
                return true;
            }
        }
        
        return false;
    }
    
    public boolean isFollowing(String currentuser, String target) throws IOException{
        following = user.userFollowing(currentuser);
        following.seek(0);
        while(following.getFilePointer()<following.length()){
            String targetuser = following.readUTF();
            boolean followinguser = following.readBoolean();
            if(targetuser.equals(target) && followinguser){
                return true;
            }
        }
        return false;
    }
    
    public boolean isFollower(String target, String currentuser)throws IOException{
        followers = user.userFollowers(target);
        followers.seek(0);
        while(followers.getFilePointer()<followers.length()){
            String thisuser = followers.readUTF();
            boolean isfollower = followers.readBoolean();
            if(thisuser.equals(currentuser) && isfollower){
                return true;
            }
        }
        return false;
    }
    
    public void followUnfollow(String currentuser, String target)throws IOException{
        following = user.userFollowing(currentuser);
        followers = user.userFollowers(target);
        
        
        if(isInFollowing(currentuser,target)){
            following.seek(0);
            while(following.getFilePointer()<following.length()){
                String targetuser = following.readUTF();
                long pos = following.getFilePointer();
                boolean followinguser = following.readBoolean();
                if(targetuser.equals(target) && followinguser){
                    following.seek(pos);
                    following.writeBoolean(false);
                }
                else if(targetuser.equals(target) && !followinguser){
                    following.seek(pos);
                    following.writeBoolean(true);
                }
            }
        }else{
            following.seek(following.length());
            following.writeUTF(target);
            following.writeBoolean(true);
        }
        
        if(isInFollowers(target,currentuser)){
            followers.seek(0);
            while(followers.getFilePointer()<followers.length()){
                String thisuser = followers.readUTF();
                long pos = followers.getFilePointer();
                boolean isfollower = followers.readBoolean();
                if(thisuser.equals(currentuser) && isfollower){
                    followers.seek(pos);
                    followers.writeBoolean(false);
                }else if(thisuser.equals(currentuser) && !isfollower){
                    followers.seek(pos);
                    followers.writeBoolean(true);
                }
            }
        }else{
            followers.seek(followers.length());
            followers.writeUTF(currentuser);
            followers.writeBoolean(true);
        }
    }
    
    public int cantidadFollowers(String username)throws IOException{
        followers = user.userFollowers(username);
        int contador=0;
        followers.seek(0);
        while(followers.getFilePointer()<followers.length()){
            String nombre = followers.readUTF();
            boolean isfollowing= followers.readBoolean();
            if(isfollowing && user.estadoCuenta(nombre)){
                contador++;
            }
        }
        return contador;
    }
    
    public int cantidadFollowing(String username)throws IOException{
        following = user.userFollowing(username);
        int contador=0;
        following.seek(0);
        while(following.getFilePointer()<following.length()){
            String nombre = following.readUTF();
            boolean isfollowing= following.readBoolean();
            if(isfollowing && user.estadoCuenta(nombre)){
                contador++;
            }
        }
        return contador;
    }
}
