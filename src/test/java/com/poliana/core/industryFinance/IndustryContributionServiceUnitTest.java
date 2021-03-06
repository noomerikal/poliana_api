package com.poliana.core.industryFinance;

import com.poliana.core.industryFinance.entities.IndustryContributionTotalsMap;
import com.poliana.core.industryFinance.repositories.IndustryContributionHadoopRepo;
import com.poliana.core.industryFinance.repositories.IndustryContributionMongoRepo;
import com.poliana.core.industryFinance.services.IndustryContributionService;
import org.bson.types.ObjectId;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Key;

import static org.easymock.EasyMock.createStrictControl;
import static org.easymock.EasyMock.expect;

/**
 * @author David Gilmore
 * @date 1/12/14
 */
public class IndustryContributionServiceUnitTest {

    private IndustryContributionService industryContributionService;

    private IndustryContributionMongoRepo industryContributionMongoRepoMock;
    private IndustryContributionHadoopRepo industryContributionHadoopRepoMock;

    private IMocksControl control;


    @Before
    public void setUp() throws Exception {

        this.control = createStrictControl();

        this.industryContributionMongoRepoMock = this.control.createMock(IndustryContributionMongoRepo.class);
        this.industryContributionHadoopRepoMock = this.control.createMock(IndustryContributionHadoopRepo.class);

        this.industryContributionService = new IndustryContributionService();

        this.industryContributionService.setIndustryContributionMongoRepo(this.industryContributionMongoRepoMock);
        this.industryContributionService.setIndustryContributionHadoopRepo(this.industryContributionHadoopRepoMock);
    }

    @Test
    public void testGetIndustryTotals_ByCongress() {

        IndustryContributionTotalsMap contributionMap = new IndustryContributionTotalsMap();

        expect(this.industryContributionMongoRepoMock.getIndustryContributionTotalsMap("K01", 113)).andReturn(null);
        expect(this.industryContributionHadoopRepoMock.getIndustryContributionTotalsMap("K01", 113)).andReturn(contributionMap);
        expect(this.industryContributionMongoRepoMock.saveIndustryContributionTotalsMap(contributionMap))
                .andReturn(new Key<>(IndustryContributionTotalsMap.class, new ObjectId()));

        this.control.replay();

        this.industryContributionService.getIndustryContributionTotalsMap("K01", 113);

        this.control.verify();
    }

    @Test
    public void testGetIndustryCategoryTotals_ByCongress() {

        IndustryContributionTotalsMap contributionMap = new IndustryContributionTotalsMap();

        expect(this.industryContributionMongoRepoMock.getIndustryCategoryContributionTotalsMap("K1000", 113)).andReturn(null);
        expect(this.industryContributionHadoopRepoMock.getIndustryCategoryContributionTotalsMap("K1000", 113)).andReturn(contributionMap);
        expect(this.industryContributionMongoRepoMock.saveIndustryContributionTotalsMap(contributionMap))
                .andReturn(new Key<>(IndustryContributionTotalsMap.class, new ObjectId()));

        this.control.replay();

        this.industryContributionService.getIndustryCategoryContributionTotalsMap("K1000", 113);

        this.control.verify();
    }

    @Test
    public void testGetIndustryContributionTotalsMap_ByChamberAndCongress() {

        IndustryContributionTotalsMap contributionMap = new IndustryContributionTotalsMap();

        expect(this.industryContributionMongoRepoMock.getIndustryContributionTotalsMapByChamber("K01", "s", 113)).andReturn(null);
        expect(this.industryContributionHadoopRepoMock.getIndustryContributionTotalsMapByChamber("K01", "s", 113)).andReturn(contributionMap);
        expect(this.industryContributionMongoRepoMock.saveIndustryContributionTotalsMap(contributionMap))
                .andReturn(new Key<>(IndustryContributionTotalsMap.class, new ObjectId()));

        this.control.replay();

        this.industryContributionService.getIndustryContributionTotalsMap("K01", "s", 113);

        this.control.verify();
    }

    @Test
    public void testGetIndustryCategoryContributionTotalsMap_ByChamberAndCongress() {

        IndustryContributionTotalsMap contributionMap = new IndustryContributionTotalsMap();

        expect(this.industryContributionMongoRepoMock.getIndustryCategoryContributionTotalsMapByChamber("K1000", "s", 113)).andReturn(null);
        expect(this.industryContributionHadoopRepoMock.getIndustryCategoryContributionTotalsMap("K1000", "s", 113)).andReturn(contributionMap);
        expect(this.industryContributionMongoRepoMock.saveIndustryContributionTotalsMap(contributionMap))
                .andReturn(new Key<>(IndustryContributionTotalsMap.class, new ObjectId()));

        this.control.replay();

        this.industryContributionService.getIndustryCategoryContributionTotalsMap("K1000", "s", 113);

        this.control.verify();
    }

    @Test
    public void testGetIndustryContributionTotalsMap_ByChamberAndTimeRange() {

        IndustryContributionTotalsMap contributionMap = new IndustryContributionTotalsMap();

        expect(this.industryContributionMongoRepoMock.getIndustryContributionTotalsMapByChamber("K01", "s", 1230185819, 1290185819)).andReturn(null);
        expect(this.industryContributionHadoopRepoMock.getIndustryContributionTotalsMapByChamber("K01", "s", 1230185819, 1290185819)).andReturn(contributionMap);
        expect(this.industryContributionMongoRepoMock.saveIndustryContributionTotalsMap(contributionMap))
                .andReturn(new Key<>(IndustryContributionTotalsMap.class, new ObjectId()));

        this.control.replay();

        this.industryContributionService.getIndustryContributionTotalsMap("K01", "s", 1230185819, 1290185819);

        this.control.verify();
    }

    @Test
    public void testGetIndustryCategoryContributionTotalsMap_ByChamberAndTimeRange() {

        IndustryContributionTotalsMap contributionMap = new IndustryContributionTotalsMap();

        expect(this.industryContributionMongoRepoMock.getIndustryCategoryContributionTotalsMapByChamber("K1000", "s", 1230185819, 1290185819)).andReturn(null);
        expect(this.industryContributionHadoopRepoMock.getIndustryCategoryContributionTotalsMapByChamber("K1000", "s", 1230185819, 1290185819)).andReturn(contributionMap);
        expect(this.industryContributionMongoRepoMock.saveIndustryContributionTotalsMap(contributionMap))
                .andReturn(new Key<>(IndustryContributionTotalsMap.class, new ObjectId()));

        this.control.replay();

        this.industryContributionService.getIndustryCategoryContributionTotalsMap("K1000", "s", 1230185819, 1290185819);

        this.control.verify();
    }
}
