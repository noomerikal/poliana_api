package com.poliana.core.politicianFinance;

import com.poliana.core.politicianFinance.entities.IndustryPoliticianContributions;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * The PoliticianFinance Service aggregates PAC contribution sums, individual contribution sums, and getIdeologyMatrix
 * analysis for every congressional cycle a given politician has been in Federal office as a representative or senator.
 * @author David Gilmore
 * @date 12/26/13
 */
@Service
public class PoliticianFinanceService {

    private PoliticianIndustryMongoRepo politicianIndustryMongoRepo;
    private PoliticianIndustryHadoopRepo politicianIndustryHadoopRepo;

    private static final Logger logger = Logger.getLogger(PoliticianFinanceService.class);


    /**
     * Get a list of industry to politician contributions summed over a given congressional cycle
     * @param bioguideId
     * @param congress
     * @return
     */
    public List<IndustryPoliticianContributions> getIndustryToPoliticianTotals(String bioguideId, int congress) {

        List<IndustryPoliticianContributions> totalsList = politicianIndustryMongoRepo.getIndustryToPoliticianContributions(bioguideId, congress);

        if (totalsList.size() > 0)
            return totalsList;

        //If MongoDB didn't return, fall back to Impala.
        totalsList = politicianIndustryHadoopRepo.getIndustryToPoliticianContributions(bioguideId, congress);

        //If Impala had something to return, save it to MongoDB
        if (totalsList != null && totalsList.size() > 0)
            politicianIndustryMongoRepo.saveIndustryToPoliticianContributions(totalsList);

        return totalsList;
    }

    /**
     * Get a list of industry to politician contributions summed over a given congressional cycle
     * @param bioguideId
     * @param beginTimestamp
     * @param endTimestamp
     * @return
     */
    public List<IndustryPoliticianContributions> getIndustryToPoliticianTotals(String bioguideId, long beginTimestamp, long endTimestamp) {

        List<IndustryPoliticianContributions> totalsList =
                politicianIndustryMongoRepo.getIndustryToPoliticianContributions(bioguideId, beginTimestamp, endTimestamp);

        if (totalsList.size() > 0)
            return totalsList;

        //If MongoDB didn't return, fall back to Impala.
        totalsList = politicianIndustryHadoopRepo.getIndustryToPoliticianContributions(bioguideId, beginTimestamp, endTimestamp);

        //If Impala had something to return, save it to MongoDB
        if (totalsList.size() > 0)
            politicianIndustryMongoRepo.saveIndustryToPoliticianContributions(totalsList);

        return totalsList;
    }

    /**
     * Get a list of industry to politician contribution sums for all time
     * @param bioguideId
     * @return
     */
    public List<IndustryPoliticianContributions> getIndustryToPoliticianTotals(String bioguideId) {

        //Query MongoDB for industry to politician objects
        List<IndustryPoliticianContributions> totalsList =
                politicianIndustryMongoRepo.getIndustryToPoliticianContributions(bioguideId);

        if (totalsList != null && totalsList.size() > 0)
            return totalsList;

        totalsList = politicianIndustryHadoopRepo.getIndustryToPoliticianContributions(bioguideId);

        politicianIndustryMongoRepo.saveIndustryToPoliticianContributions(totalsList);

        return totalsList;
    }

    /**
     * Get a HashMap of Cycle->Industry contribution lists for all congressional cycles a politician has been apart of
     * @param bioguideId
     * @return
     */
    public HashMap<Integer, List<IndustryPoliticianContributions>> getIndustryTotalsAllTime(String bioguideId) {

        //Query MongoDB for industry to politician objects
        Iterator<IndustryPoliticianContributions> totalsIterator = politicianIndustryMongoRepo.getIndustryToPoliticianContributionsIterator(bioguideId);

        HashMap<Integer, List<IndustryPoliticianContributions>> totalsHashMap = new HashMap<>(30);

        //Add industry totals to the HashMap. Check the size, if it's zero, fall back to Impala.
        while (totalsIterator.hasNext()) {
            IndustryPoliticianContributions industryTotals = totalsIterator.next();

            //If the hashmap already has a list of industry totals for the object's cycle
            if (totalsHashMap.containsKey(industryTotals.getCongress()))
                totalsHashMap.get(industryTotals.getCongress()).add(industryTotals);
                //If the hashmap doesn't contain a list of industry totals, make it
            else {
                List<IndustryPoliticianContributions> totalsList = new LinkedList<>();
                totalsList.add(industryTotals);
                totalsHashMap.put(industryTotals.getCongress(), totalsList);
            }
        }

        //A size greater than 0 means that MongoDB had the sums cached
        if (totalsHashMap.size() > 0)
            return totalsHashMap;

        //Fall back to Impala if MongoDB did not have the sums cached
        totalsHashMap = politicianIndustryHadoopRepo.getAllIndustryContributionsPerCongress(bioguideId);

        //Cache sums to MongoDB

        //Get an iterator for the values in the hash map
        Iterator it = totalsHashMap.entrySet().iterator();
        Map.Entry pairs;

        //Iterate through all entry pairs in the map and update the TermTotalsMap with the values.
        while (it.hasNext()) {
            pairs = (Map.Entry) it.next();

            if (politicianIndustryMongoRepo.countIndustryToPoliticianContributions(bioguideId, (Integer) pairs.getKey()) <= 0)
                politicianIndustryMongoRepo.saveIndustryToPoliticianContributions((List<IndustryPoliticianContributions>)pairs.getValue());
        }

        return totalsHashMap;
    }

    @Autowired
    public void setPoliticianIndustryMongoRepo(PoliticianIndustryMongoRepo politicianIndustryMongoRepo) {
        this.politicianIndustryMongoRepo = politicianIndustryMongoRepo;
    }

    @Autowired
    public void setPoliticianIndustryHadoopRepo(PoliticianIndustryHadoopRepo politicianIndustryHadoopRepo) {
        this.politicianIndustryHadoopRepo = politicianIndustryHadoopRepo;
    }
}
