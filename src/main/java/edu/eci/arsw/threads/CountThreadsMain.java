/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThreadsMain {
    
    public static void main(String a[]){
        CountThread count= new CountThread(0,99);
        CountThread count2= new CountThread(100,199);
        CountThread count3= new CountThread(200,299);
        count.run();
        count2.run();
        count3.run();
    }
}
