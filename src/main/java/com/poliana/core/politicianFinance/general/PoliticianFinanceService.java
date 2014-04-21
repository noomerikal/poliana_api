package com.poliana.core.politicianFinance.general;

import com.rollup.olap.CubeDataRepo;
import com.rollup.olap.HolapClient;
import com.rollup.olap.models.DataNode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author David Gilmore
 * @date 4/15/14
 */
@Service
public class PoliticianFinanceService {

    private static final Logger logger = Logger.getLogger(PoliticianFinanceService.class);

    private HolapClient holapClient;

    private CubeDataRepo cubeDataRepo;

    private PoliticianRedisRepo politicianRedisRepo;
    private PoliticianFinanceRepository politicianFinanceRepository;


    /**
     * Get a list of pac and industry totals per congress for all time wrapper in a DataNode.
     * The return will be a list of Map<String, Object>
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public DataNode getPacAndIndustryTotals() {

        DataNode ret;

        List<Map<String, Object>> contributions = politicianFinanceRepository.getPacAndIndustryTotals();

        ret = new DataNode(contributions);


        return ret;
    }

    /**
     * Get a list of pac and industry totals per congress for all time wrapper in a DataNode.
     * The return will be a list of Map<String, Object>
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public DataNode getPacAndIndustryTotals(String unit) {

        DataNode ret;

        unit = (unit == null) ? "": unit;

        List<Map<String, Object>> contributions;

        if (politicianRedisRepo.getIndustryAndPacContributionsExistInCache()) {
            contributions = null; //TODO
        }
        else {
            contributions = politicianFinanceRepository.getPacAndIndustryTotalsPerCongress();
            cubeDataRepo.save("industry_and_pac_totals", contributions);
            politicianRedisRepo.setIndustryAndPacContributionsExistInCache();
        }

        Map<Object, List<Map<String, Object>>> rolled = holapClient.rollup(unit, contributions);

        ret = (rolled.size() > 0) ? new DataNode(rolled) : new DataNode(contributions);

        return ret;
    }

    @Autowired
    public void setHolapClient(HolapClient holapClient) {
        this.holapClient = holapClient;
    }

    @Autowired
    public void setCubeDataRepo(CubeDataRepo cubeDataRepo) {
        this.cubeDataRepo = cubeDataRepo;
    }

    @Autowired
    public void setPoliticianRedisRepo(PoliticianRedisRepo politicianRedisRepo) {
        this.politicianRedisRepo = politicianRedisRepo;
    }

    @Autowired
    public void setPoliticianFinanceRepository(PoliticianFinanceRepository politicianFinanceRepository) {
        this.politicianFinanceRepository = politicianFinanceRepository;
    }
}
