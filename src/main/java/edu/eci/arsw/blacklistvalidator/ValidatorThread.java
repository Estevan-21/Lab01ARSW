/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

/**
 *
 * @author Estevan
 */
public class ValidatorThread extends Thread {
    int ocurrencesCount=0;
    int n;
    BlackListThread[] hilos;
    int checkedListsCount=0;
    
    public ValidatorThread(BlackListThread[] hilos,int n){
        this.hilos=hilos;
        this.n=n;
    }
    
    
    /*
        Valida que el host no haya sido encontrado en almenos 5 listas o ya se haya buscado en todas las listas,
        si esto ocurre detiene los hilos.
    */
    public void run(){
        while(ocurrencesCount<5 && checkedListsCount<80000 ){               
            ocurrencesCount=0;
            checkedListsCount=0;
            for (int i=0;i<n;i++) {
                ocurrencesCount=ocurrencesCount+hilos[i].getOcurrences();
                checkedListsCount=checkedListsCount+hilos[i].getCountList();
            }              
        }        
        for (int i=0;i<n;i++) {
           hilos[i].stop();
        }
    }
}
