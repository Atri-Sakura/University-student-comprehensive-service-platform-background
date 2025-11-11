package com.ruoyi.platform.utils;

import com.ruoyi.platform.merchant.vo.EvaluationAnalysisVO;
import com.ruoyi.platform.merchant.vo.KeywordSummary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class AdvancedKeywordExtractionUtils {

    // 标准关键词库
    private static final List<String> STANDARD_POSITIVE_KEYWORDS = Arrays.asList(
            "味道好", "配送快", "包装精美", "服务热情", "性价比高", "新鲜", "干净", "分量足",
            "好吃", "美味", "速度快", "及时", "包装好", "服务好", "态度好", "实惠",
            "卫生", "整洁", "量大", "很饱", "推荐", "满意", "喜欢", "不错", "很棒"
    );

    private static final List<String> STANDARD_NEGATIVE_KEYWORDS = Arrays.asList(
            "等待时间长", "分量不足", "包装破损", "服务差", "难吃", "不新鲜", "不干净", "太贵",
            "等太久", "慢", "量少", "很少", "包装烂", "态度差", "不好吃", "味道差",
            "变质", "脏", "贵", "不划算", "失望", "不会再买", "咸", "淡", "太油", "糟糕"
    );

    // 同义词映射（同义词 -> 标准关键词）
    private final Map<String, String> synonymToStandardMap;

    public AdvancedKeywordExtractionUtils() {
        this.synonymToStandardMap = buildSynonymMapping();
    }

    /**
     * 高级关键词提取 - 支持同义词匹配和预处理，返回关键词频率
     */
    public void extractKeywordsWithFrequency(EvaluationAnalysisVO analysisVO, List<String> comments) {
        if (comments == null || comments.isEmpty()) {
            analysisVO.setPositiveKeywords(Collections.emptyMap());
            analysisVO.setNegativeKeywords(Collections.emptyMap());
            analysisVO.setKeywordSummary(new KeywordSummary());
            return;
        }

        Map<String, Integer> positiveKeywordFreq = new HashMap<>();
        Map<String, Integer> negativeKeywordFreq = new HashMap<>();

        int commentsWithKeywords = 0; // 包含关键词的评论数量

        // 处理每条评论
        for (String comment : comments) {
            if (comment == null || comment.trim().isEmpty()) {
                continue;
            }

            // 评论预处理
            String processedComment = preprocessComment(comment);

            // 提取关键词并统计频率
            boolean hasKeyword = extractAndCountKeywords(processedComment, positiveKeywordFreq, negativeKeywordFreq);
            if (hasKeyword) {
                commentsWithKeywords++;
            }
        }

        // 移除频率为0的关键词
        removeZeroFrequencyKeywords(positiveKeywordFreq);
        removeZeroFrequencyKeywords(negativeKeywordFreq);

        // 设置到VO
        analysisVO.setPositiveKeywords(positiveKeywordFreq);
        analysisVO.setNegativeKeywords(negativeKeywordFreq);

        // 计算并设置关键词统计摘要
        KeywordSummary summary = calculateKeywordSummary(positiveKeywordFreq, negativeKeywordFreq,
                comments.size(), commentsWithKeywords);
        analysisVO.setKeywordSummary(summary);
    }

    /**
     * 评论预处理
     */
    private String preprocessComment(String comment) {
        if (comment == null) {
            return "";
        }

        // 1. 转换为小写
        String processed = comment.toLowerCase();

        // 2. 去除标点符号和特殊字符
        processed = processed.replaceAll("[\\pP\\p{Punct}]", " ");

        // 3. 去除多余空格
        processed = processed.replaceAll("\\s+", " ").trim();

        return processed;
    }

    /**
     * 提取关键词并统计频率
     */
    private boolean extractAndCountKeywords(String comment,
                                            Map<String, Integer> positiveFreq,
                                            Map<String, Integer> negativeFreq) {
        boolean hasKeyword = false;

        // 检查标准正面关键词
        for (String keyword : STANDARD_POSITIVE_KEYWORDS) {
            if (containsKeyword(comment, keyword)) {
                positiveFreq.put(keyword, positiveFreq.getOrDefault(keyword, 0) + 1);
                hasKeyword = true;
            }
        }

        // 检查标准负面关键词
        for (String keyword : STANDARD_NEGATIVE_KEYWORDS) {
            if (containsKeyword(comment, keyword)) {
                negativeFreq.put(keyword, negativeFreq.getOrDefault(keyword, 0) + 1);
                hasKeyword = true;
            }
        }

        // 检查同义词
        for (Map.Entry<String, String> entry : synonymToStandardMap.entrySet()) {
            String synonym = entry.getKey();
            String standardKeyword = entry.getValue();

            if (containsKeyword(comment, synonym)) {
                hasKeyword = true;
                // 判断是正面还是负面关键词
                if (STANDARD_POSITIVE_KEYWORDS.contains(standardKeyword)) {
                    positiveFreq.put(standardKeyword, positiveFreq.getOrDefault(standardKeyword, 0) + 1);
                } else if (STANDARD_NEGATIVE_KEYWORDS.contains(standardKeyword)) {
                    negativeFreq.put(standardKeyword, negativeFreq.getOrDefault(standardKeyword, 0) + 1);
                }
            }
        }

        return hasKeyword;
    }

    /**
     * 检查评论是否包含关键词
     */
    private boolean containsKeyword(String comment, String keyword) {
        String processedKeyword = keyword.toLowerCase();
        return comment.contains(processedKeyword);
    }

    /**
     * 构建同义词映射
     */
    private Map<String, String> buildSynonymMapping() {
        Map<String, String> synonymMap = new HashMap<>();

        // 正面词同义词映射
        synonymMap.put("好吃", "味道好");
        synonymMap.put("美味", "味道好");
        synonymMap.put("口味好", "味道好");
        synonymMap.put("很香", "味道好");
        synonymMap.put("可口", "味道好");

        synonymMap.put("送货快", "配送快");
        synonymMap.put("送得快", "配送快");
        synonymMap.put("及时", "配送快");
        synonymMap.put("准时", "配送快");
        synonymMap.put("迅速", "配送快");

        synonymMap.put("包装漂亮", "包装精美");
        synonymMap.put("包装精致", "包装精美");
        synonymMap.put("包装完好", "包装精美");
        synonymMap.put("包装完整", "包装精美");

        synonymMap.put("服务好", "服务热情");
        synonymMap.put("态度好", "服务热情");
        synonymMap.put("耐心", "服务热情");
        synonymMap.put("周到", "服务热情");
        synonymMap.put("贴心", "服务热情");

        synonymMap.put("实惠", "性价比高");
        synonymMap.put("便宜", "性价比高");
        synonymMap.put("划算", "性价比高");
        synonymMap.put("物美价廉", "性价比高");
        synonymMap.put("超值", "性价比高");

        synonymMap.put("鲜美", "新鲜");
        synonymMap.put("现做", "新鲜");
        synonymMap.put("食材好", "新鲜");

        synonymMap.put("卫生", "干净");
        synonymMap.put("整洁", "干净");
        synonymMap.put("清爽", "干净");

        synonymMap.put("量大", "分量足");
        synonymMap.put("很饱", "分量足");
        synonymMap.put("吃不完", "分量足");
        synonymMap.put("充足", "分量足");

        // 负面词同义词映射
        synonymMap.put("等太久", "等待时间长");
        synonymMap.put("等得久", "等待时间长");
        synonymMap.put("慢", "等待时间长");
        synonymMap.put("迟迟", "等待时间长");

        synonymMap.put("量少", "分量不足");
        synonymMap.put("很少", "分量不足");
        synonymMap.put("不够吃", "分量不足");
        synonymMap.put("一点点", "分量不足");

        synonymMap.put("包装烂", "包装破损");
        synonymMap.put("包装破", "包装破损");
        synonymMap.put("漏了", "包装破损");
        synonymMap.put("洒了", "包装破损");

        synonymMap.put("态度差", "服务差");
        synonymMap.put("不耐烦", "服务差");
        synonymMap.put("冷漠", "服务差");
        synonymMap.put("恶劣", "服务差");

        synonymMap.put("不好吃", "难吃");
        synonymMap.put("味道差", "难吃");
        synonymMap.put("难以下咽", "难吃");
        synonymMap.put("难入口", "难吃");

        synonymMap.put("变质", "不新鲜");
        synonymMap.put("异味", "不新鲜");
        synonymMap.put("变味", "不新鲜");

        synonymMap.put("脏", "不干净");
        synonymMap.put("杂乱", "不干净");
        synonymMap.put("污渍", "不干净");

        synonymMap.put("贵", "太贵");
        synonymMap.put("不划算", "太贵");
        synonymMap.put("价格高", "太贵");
        synonymMap.put("昂贵", "太贵");

        return synonymMap;
    }

    /**
     * 移除频率为0的关键词
     */
    private void removeZeroFrequencyKeywords(Map<String, Integer> keywordFreq) {
        keywordFreq.entrySet().removeIf(entry -> entry.getValue() == 0);
    }

    /**
     * 计算关键词统计摘要
     */
    private KeywordSummary calculateKeywordSummary(Map<String, Integer> positiveKeywords,
                                                   Map<String, Integer> negativeKeywords,
                                                   int totalComments, int commentsWithKeywords) {
        KeywordSummary summary = new KeywordSummary();

        // 关键词数量
        summary.setTotalPositiveKeywords(positiveKeywords.size());
        summary.setTotalNegativeKeywords(negativeKeywords.size());

        // 最常出现的正面关键词
        String topPositive = positiveKeywords.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("无");
        summary.setTopPositiveKeyword(topPositive);

        // 最常出现的负面关键词
        String topNegative = negativeKeywords.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("无");
        summary.setTopNegativeKeyword(topNegative);

        // 关键词总出现次数
        int positiveTotal = positiveKeywords.values().stream().mapToInt(Integer::intValue).sum();
        int negativeTotal = negativeKeywords.values().stream().mapToInt(Integer::intValue).sum();
        summary.setPositiveKeywordTotalCount(positiveTotal);
        summary.setNegativeKeywordTotalCount(negativeTotal);

        // 关键词覆盖率
        if (totalComments > 0) {
            BigDecimal coverage = BigDecimal.valueOf((double) commentsWithKeywords / totalComments * 100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            summary.setKeywordCoverage(coverage);
        }

        return summary;
    }

    /**
     * 获取排序后的关键词列表（按频率降序）
     */
    public List<Map.Entry<String, Integer>> getSortedPositiveKeywords(EvaluationAnalysisVO analysisVO) {
        return analysisVO.getPositiveKeywords().entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
    }

    /**
     * 获取排序后的负面关键词列表（按频率降序）
     */
    public List<Map.Entry<String, Integer>> getSortedNegativeKeywords(EvaluationAnalysisVO analysisVO) {
        return analysisVO.getNegativeKeywords().entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
    }
}