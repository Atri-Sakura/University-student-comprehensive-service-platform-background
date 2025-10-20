package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.ChatAttachment;

/**
 * 消息附件（存储图片/语音等附件的元信息）Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface ChatAttachmentMapper 
{
    /**
     * 查询消息附件（存储图片/语音等附件的元信息）
     * 
     * @param attachmentId 消息附件（存储图片/语音等附件的元信息）主键
     * @return 消息附件（存储图片/语音等附件的元信息）
     */
    public ChatAttachment selectChatAttachmentByAttachmentId(Long attachmentId);

    /**
     * 查询消息附件（存储图片/语音等附件的元信息）列表
     * 
     * @param chatAttachment 消息附件（存储图片/语音等附件的元信息）
     * @return 消息附件（存储图片/语音等附件的元信息）集合
     */
    public List<ChatAttachment> selectChatAttachmentList(ChatAttachment chatAttachment);

    /**
     * 新增消息附件（存储图片/语音等附件的元信息）
     * 
     * @param chatAttachment 消息附件（存储图片/语音等附件的元信息）
     * @return 结果
     */
    public int insertChatAttachment(ChatAttachment chatAttachment);

    /**
     * 修改消息附件（存储图片/语音等附件的元信息）
     * 
     * @param chatAttachment 消息附件（存储图片/语音等附件的元信息）
     * @return 结果
     */
    public int updateChatAttachment(ChatAttachment chatAttachment);

    /**
     * 删除消息附件（存储图片/语音等附件的元信息）
     * 
     * @param attachmentId 消息附件（存储图片/语音等附件的元信息）主键
     * @return 结果
     */
    public int deleteChatAttachmentByAttachmentId(Long attachmentId);

    /**
     * 批量删除消息附件（存储图片/语音等附件的元信息）
     * 
     * @param attachmentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChatAttachmentByAttachmentIds(Long[] attachmentIds);
}
