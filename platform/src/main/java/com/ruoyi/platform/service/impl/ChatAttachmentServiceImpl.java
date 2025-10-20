package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.ChatAttachmentMapper;
import com.ruoyi.platform.domain.ChatAttachment;
import com.ruoyi.platform.service.IChatAttachmentService;

/**
 * 消息附件（存储图片/语音等附件的元信息）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class ChatAttachmentServiceImpl implements IChatAttachmentService 
{
    @Autowired
    private ChatAttachmentMapper chatAttachmentMapper;

    /**
     * 查询消息附件（存储图片/语音等附件的元信息）
     * 
     * @param attachmentId 消息附件（存储图片/语音等附件的元信息）主键
     * @return 消息附件（存储图片/语音等附件的元信息）
     */
    @Override
    public ChatAttachment selectChatAttachmentByAttachmentId(Long attachmentId)
    {
        return chatAttachmentMapper.selectChatAttachmentByAttachmentId(attachmentId);
    }

    /**
     * 查询消息附件（存储图片/语音等附件的元信息）列表
     * 
     * @param chatAttachment 消息附件（存储图片/语音等附件的元信息）
     * @return 消息附件（存储图片/语音等附件的元信息）
     */
    @Override
    public List<ChatAttachment> selectChatAttachmentList(ChatAttachment chatAttachment)
    {
        return chatAttachmentMapper.selectChatAttachmentList(chatAttachment);
    }

    /**
     * 新增消息附件（存储图片/语音等附件的元信息）
     * 
     * @param chatAttachment 消息附件（存储图片/语音等附件的元信息）
     * @return 结果
     */
    @Override
    public int insertChatAttachment(ChatAttachment chatAttachment)
    {
        chatAttachment.setCreateTime(DateUtils.getNowDate());
        return chatAttachmentMapper.insertChatAttachment(chatAttachment);
    }

    /**
     * 修改消息附件（存储图片/语音等附件的元信息）
     * 
     * @param chatAttachment 消息附件（存储图片/语音等附件的元信息）
     * @return 结果
     */
    @Override
    public int updateChatAttachment(ChatAttachment chatAttachment)
    {
        chatAttachment.setUpdateTime(DateUtils.getNowDate());
        return chatAttachmentMapper.updateChatAttachment(chatAttachment);
    }

    /**
     * 批量删除消息附件（存储图片/语音等附件的元信息）
     * 
     * @param attachmentIds 需要删除的消息附件（存储图片/语音等附件的元信息）主键
     * @return 结果
     */
    @Override
    public int deleteChatAttachmentByAttachmentIds(Long[] attachmentIds)
    {
        return chatAttachmentMapper.deleteChatAttachmentByAttachmentIds(attachmentIds);
    }

    /**
     * 删除消息附件（存储图片/语音等附件的元信息）信息
     * 
     * @param attachmentId 消息附件（存储图片/语音等附件的元信息）主键
     * @return 结果
     */
    @Override
    public int deleteChatAttachmentByAttachmentId(Long attachmentId)
    {
        return chatAttachmentMapper.deleteChatAttachmentByAttachmentId(attachmentId);
    }
}
