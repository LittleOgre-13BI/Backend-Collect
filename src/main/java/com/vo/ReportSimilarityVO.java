package com.vo;

import lombok.Data;

@Data
public class ReportSimilarityVO {

    private Long fid;

    private Long uid;

    private Long mid;

    private String title;

    private Double similarity;
}
