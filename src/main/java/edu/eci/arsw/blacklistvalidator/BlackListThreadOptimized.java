package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author ivanCubillos
 */
public class BlackListThreadOptimized extends Thread {
    
    private int startIndex; 
    private int endIndex;       
    private String ipAddress;   
    private int occurrencesCount; 
    private List<Integer> blackListOccurrences;
    private int checkedServersCount;

    private AtomicInteger sharedOccurrencesCount;
    
    private static final int BLACK_LIST_ALARM_COUNT = 5;
    
    public BlackListThreadOptimized(int startIndex, int endIndex, String ipAddress, AtomicInteger sharedOccurrencesCount) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.ipAddress = ipAddress;
        this.occurrencesCount = 0;
        this.blackListOccurrences = new LinkedList<>();
        this.checkedServersCount = 0;
        this.sharedOccurrencesCount = sharedOccurrencesCount;
    }
    
    @Override
    public void run() {
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();
        
        for (int i = startIndex; i < endIndex; i++) {
            
            if (sharedOccurrencesCount.get() >= BLACK_LIST_ALARM_COUNT) {
                break;
            }
            
            checkedServersCount++; 
            
            if (skds.isInBlackListServer(i, ipAddress)) {
                blackListOccurrences.add(i);
                occurrencesCount++;
                
                sharedOccurrencesCount.incrementAndGet();
            }
        }
    }

    public int getOccurrencesCount() {
        return occurrencesCount;
    }
    

    public List<Integer> getBlackListOccurrences() {
        return blackListOccurrences;
    }

    public int getCheckedServersCount() {
        return checkedServersCount;
    }
}
