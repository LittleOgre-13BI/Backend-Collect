package com.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("report_similarity")
public class ReportSimilarity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主报告1的编号
     */
    private long fidA;

    /**
     * 主报告2的编号
     * 保证两个编号升序排列
     */
    private long fidB;

    /**
     * 文本相似度
     */
    private double similarity;

    public ReportSimilarity(){
        setSimilarity(0);
    }

//    @Override
//    public int compareTo(ReportSimilarity o) {
//        if (o.getSimilarity() == this.getSimilarity()) return 0;
//        return o.getSimilarity() > this.getSimilarity() ? 1 : -1;
//    }
}
