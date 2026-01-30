/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;

/**
 *
 * @author hcadavid
 */
public class Main {
    
    public static void main(String a[]) {
        HostBlackListsValidator hblv = new HostBlackListsValidator();
        
        int numberOfThreads = 200;
        
        System.out.println("===== Probando con 202.24.34.55 (disperso) =====");
        long startTime = System.currentTimeMillis();
        List<Integer> blackListOccurrences = hblv.checkHost("200.24.34.55", numberOfThreads);
        long endTime = System.currentTimeMillis();
        System.out.println("The host was found in the following blacklists: " + blackListOccurrences);
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
        
        
        System.out.println("\n===== Probando con 212.24.24.55 (no malicioso) =====");
        startTime = System.currentTimeMillis();
        blackListOccurrences = hblv.checkHost("212.24.24.55", numberOfThreads);
        endTime = System.currentTimeMillis();
        System.out.println("The host was found in the following blacklists: " + blackListOccurrences);
        System.out.println("Time taken: " + (endTime - startTime) + " ms");

        System.out.println("=== 2.1 Threads optimizados ===");
        startTime = System.currentTimeMillis();
        blackListOccurrences = hblv.checkHostOptimized("202.24.34.55", numberOfThreads);
        endTime = System.currentTimeMillis();
        System.out.println("Occurrences found: " + blackListOccurrences.size());
        System.out.println("Time taken: " + (endTime - startTime) + " ms");

        System.out.println("===== Un Nucleo=====");
        startTime = System.currentTimeMillis();
        blackListOccurrences = hblv.checkHost("200.24.34.55", 1);
        endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");

        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("===== Nucleos disponibles =====");
        startTime = System.currentTimeMillis();
        blackListOccurrences = hblv.checkHost("200.24.34.55", cores);
        endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");

        System.out.println("===== Nucleos disponibles * 2 =====");
        startTime = System.currentTimeMillis();
        blackListOccurrences = hblv.checkHost("200.24.34.55", 2* cores);
        endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");

        System.out.println("===== 50 hilos =====");
        startTime = System.currentTimeMillis();
        blackListOccurrences = hblv.checkHost("200.24.34.55", 50);
        endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");

        System.out.println("===== 100 hilos =====");
        startTime = System.currentTimeMillis();
        blackListOccurrences = hblv.checkHost("200.24.34.55", 100);
        endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");

    } 
}
