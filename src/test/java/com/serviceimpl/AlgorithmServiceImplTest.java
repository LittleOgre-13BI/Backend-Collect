package com.serviceimpl;

import com.mapperservice.ReportSimilarityMapperService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AlgorithmServiceImplTest {

    @MockBean
    ReportSimilarityMapperService reportSimilarityMapperService;

    @Autowired
    AlgorithmServiceImpl algorithmServiceImpl;

    /**
     * Test for the method calculateSimilarity
     */
    @Test
    void calReportSimilarity() {

    }

    /**
     * Test for the method recommendMissions
     */
    @Test
    void recommendMissions() {

    }

    @Test
    void changeStrategy() {
    }

    @Test
    void getStrategy() {
    }
}