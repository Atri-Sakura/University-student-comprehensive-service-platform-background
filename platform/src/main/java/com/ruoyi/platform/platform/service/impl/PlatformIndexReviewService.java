package com.ruoyi.platform.platform.service.impl;

import com.ruoyi.common.utils.file.MinioFileUtils;
import com.ruoyi.platform.domain.IndexImgUrl;
import com.ruoyi.platform.platform.mapper.PlatformIndexReviewMapper;
import com.ruoyi.platform.platform.service.IPlatformIndexReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PlatformIndexReviewService implements IPlatformIndexReviewService {
    @Autowired
    private PlatformIndexReviewMapper platformIndexReviewMapper;
    @Autowired
    private MinioFileUtils minioFileUtils;
    @Override
    public List<IndexImgUrl> getUserIndexImgs() {
        return platformIndexReviewMapper.getUserIndexImgs();
    }

    @Override
    @Transactional
    public int deleteUserIndexImgs(IndexImgUrl indexImgUrl) {
        String url = indexImgUrl.getIndexImageUrl();
        try {
            int result = platformIndexReviewMapper.deleteUserIndexImgs(indexImgUrl.getIndexImageUrlId());
            if (result > 0) {
                minioFileUtils.deleteByUrl(url);
                return result;
            }
        }catch (Exception e){
            throw new RuntimeException("删除失败");
        }
        return 0;
    }

    @Override
    @Transactional
    public int addIndexImgUrl(MultipartFile file) {
        String url;
        try {
            Long random = new Random().nextLong(9000000L);
            List<IndexImgUrl> indexImgUrls = platformIndexReviewMapper.getUserIndexImgs();
            List<Integer> indexImgUrlIds = new ArrayList<>();
            for (IndexImgUrl indexImgUrl : indexImgUrls) {
                    indexImgUrlIds.add(indexImgUrl.getIndexImageUrlId());
            }
            while (indexImgUrlIds.contains(random)) {
                random = new Random().nextLong(9000000L);
            }
            url = minioFileUtils.upload(file,"indeximage",random );
            int result = platformIndexReviewMapper.addIndexImgUrl(Math.toIntExact(random),url);
            if (result > 0) {
                return result;
            }else{
                minioFileUtils.deleteByUrl(url);
                return 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
