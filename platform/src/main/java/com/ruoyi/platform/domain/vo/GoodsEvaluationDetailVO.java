package com.ruoyi.platform.domain.vo;

import com.ruoyi.platform.domain.GoodsEvaluation;
import com.ruoyi.platform.domain.GoodsEvaluationImage;

import java.util.List;

/**
 * 商品评价详情VO：包括评价和图片列表
 */
public class GoodsEvaluationDetailVO {
    private GoodsEvaluation evaluation;
    private List<GoodsEvaluationImage> images;

    public GoodsEvaluationDetailVO(GoodsEvaluation evaluation, List<GoodsEvaluationImage> images) {
        this.evaluation = evaluation;
        this.images = images;
    }

    public GoodsEvaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(GoodsEvaluation evaluation) {
        this.evaluation = evaluation;
    }

    public List<GoodsEvaluationImage> getImages() {
        return images;
    }

    public void setImages(List<GoodsEvaluationImage> images) {
        this.images = images;
    }
}