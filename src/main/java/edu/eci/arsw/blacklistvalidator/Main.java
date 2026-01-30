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
        
        int numberOfThreads = 4;
        
        System.out.println("===== Probando con 200.24.34.55 =====");
        long startTime = System.currentTimeMillis();
        List<Integer> blackListOccurrences = hblv.checkHost("200.24.34.55", numberOfThreads);
        long endTime = System.currentTimeMillis();
        System.out.println("The host was found in the following blacklists: " + blackListOccurrences);
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
        
        System.out.println("\n===== Probando con 202.24.34.55 (disperso) =====");
        startTime = System.currentTimeMillis();
        blackListOccurrences = hblv.checkHost("202.24.34.55", numberOfThreads);
        endTime = System.currentTimeMillis();
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
    } 
}
