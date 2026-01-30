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
    public List<Integer> checkHost(String ipaddress, int N) {
        
        LinkedList<Integer> blackListOccurrences = new LinkedList<>();
        int occurrencesCount = 0;
        
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();
        int totalServers = skds.getRegisteredServersCount();
        
        int segmentSize = totalServers / N;
        int remainder = totalServers % N;
        
        BlackListThread[] threads = new BlackListThread[N];
        
        int startIndex = 0;
        for (int i = 0; i < N; i++) {
            int endIndex = startIndex + segmentSize;
            
            if (i < remainder) {
                endIndex++;
            }
            
            threads[i] = new BlackListThread(startIndex, endIndex, ipaddress);
            startIndex = endIndex;
        }
        
        for (int i = 0; i < N; i++) {
            threads[i].start();
        }
        
        try {
            for (int i = 0; i < N; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        for (int i = 0; i < N; i++) {
            occurrencesCount += threads[i].getOccurrencesCount();
            blackListOccurrences.addAll(threads[i].getBlackListOccurrences());
        }
        
        if (occurrencesCount >= BLACK_LIST_ALARM_COUNT) {
            skds.reportAsNotTrustworthy(ipaddress);
        } else {
            skds.reportAsTrustworthy(ipaddress);
        }

        int checkedListsCount = 0;
        for (int i = 0; i < N; i++) {
            occurrencesCount += threads[i].getOccurrencesCount();
            blackListOccurrences.addAll(threads[i].getBlackListOccurrences());
            checkedListsCount += threads[i].getCheckedServersCount();
        }
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, totalServers});
        
        return blackListOccurrences;
    }
    
    public List<Integer> checkHostOptimized(String ipaddress, int N){
        LinkedList<Integer> blackListOccurrences = new LinkedList<>();
        
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();
        int totalServers = skds.getRegisteredServersCount();
        
        java.util.concurrent.atomic.AtomicInteger sharedOccurrencesCount = new java.util.concurrent.atomic.AtomicInteger(0);
        
        int segmentSize = totalServers / N;
        int remainder = totalServers % N;
        
        BlackListThreadOptimized[] threads = new BlackListThreadOptimized[N];
        
        int startIndex = 0;
        for (int i = 0; i < N; i++) {
            int endIndex = startIndex + segmentSize;
            
            if (i < remainder) {
                endIndex++;
            }
            
            threads[i] = new BlackListThreadOptimized(startIndex, endIndex, ipaddress, sharedOccurrencesCount);
            startIndex = endIndex;
        }
        
        for (int i = 0; i < N; i++) {
            threads[i].start();
        }
        
        try {
            for (int i = 0; i < N; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        int checkedListsCount = 0;
        int occurrencesCount = 0;
        for (int i = 0; i < N; i++) {
            occurrencesCount += threads[i].getOccurrencesCount();
            blackListOccurrences.addAll(threads[i].getBlackListOccurrences());
            checkedListsCount += threads[i].getCheckedServersCount();
        }
        
        if (occurrencesCount >= BLACK_LIST_ALARM_COUNT) {
            skds.reportAsNotTrustworthy(ipaddress);
        } else {
            skds.reportAsTrustworthy(ipaddress);
        }
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, totalServers});
        
        return blackListOccurrences;
    }
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
}
