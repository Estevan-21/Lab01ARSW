/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int n) throws InterruptedException{
                       
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        int count=0;
        int ocurrencesCount=0;
        
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        
        int rango=skds.getRegisteredServersCount()/n;
        
         /* Creo lista de hilos */
        BlackListThread[] hilos=new BlackListThread[n];
        for (int i=0;i<n;i++) {
            hilos[i] = new BlackListThread(count,count+rango,ipaddress);
            count=count+rango;
        }
        
        int checkedListsCount=0;
        /*
        for (int i=0;i<skds.getRegisteredServersCount() && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){
            checkedListsCount++;
            
            if (skds.isInBlackListServer(i, ipaddress)){
                
                blackListOcurrences.add(i);
                
                ocurrencesCount++;
            }
        }
        */
        
        //Inicio todos los hilos del arreglo
        for (int i=0;i<n;i++) {
            hilos[i].start();            
        }
        hilos[0].join(10000);
        //Sumo las listas contadas y ocurrencias encontradas en los hilos
         for (int i=0;i<n;i++) {
             checkedListsCount=checkedListsCount+hilos[i].getCountList();
             ocurrencesCount=ocurrencesCount+hilos[i].getOcurrences();
        }                 
         
        //Añado las ocurrencias de cada lista de cada hilo a una sola lista
         for (int i=0;i<n;i++) {
             LinkedList<Integer> lista=hilos[i].getBlackList();
             for (int j=0;j<lista.size();j++){
                 // blackListOcurrences.add(lista[j]);
             }
         }
        
        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }                
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        
        return blackListOcurrences;
    }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    
}
