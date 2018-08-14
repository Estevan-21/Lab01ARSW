/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.logging.Level;

/**
 *
 * @author 2109950
 */
public class BlackListThread extends Thread {
    
    int ini=0;
    int fin=0;
    int checkedListsCount=0;
    String ip;
    int ocurrencesCount;
    LinkedList<Integer> blackListOcurrences=new LinkedList<>();                
        
    
    public BlackListThread(int ini, int fin,String ip){
        this.ini=ini;
        this.fin=fin;
        this.ip=ip;
    }
    
    
    /*
    Hace las búsquedas en las listas y guarda en las variables el respectivo contenido
    
    */
    public void run(){        
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();             
        for (int i=ini;i<fin;i++){
            checkedListsCount++;
            
            if (skds.isInBlackListServer(i, ip)){
                
                blackListOcurrences.add(i);
                
                ocurrencesCount++; 
                
            }
        }         
    }
    
    /*
        Retorna la lista de ocurrencias encontradas
    */
    public LinkedList<Integer> getBlackList(){        
        return blackListOcurrences;
    }
    
    
    /*
        Retorna el número de ocurrencias encontradas
    */
    public int getOcurrences(){        
        return ocurrencesCount;
    }
    
    /*
        Retorna el número de listas revisadas
    */
     public int getCountList(){
        return checkedListsCount;
    }
}
