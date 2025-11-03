package com.ruoyi.platform.chat.codec;

import com.ruoyi.platform.chat.protobuf.ChatMessageProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ProtobufToWebSocketEncoder extends MessageToMessageEncoder<ChatMessageProto.ChatMessage> {

    // 魔数：CAFEBABE（8字节，显式声明为无符号长整型）
    private static final long MAGIC_NUMBER = 0xCAFEBABEL;

    @Override
    protected void encode(ChannelHandlerContext ctx, ChatMessageProto.ChatMessage msg, List<Object> out) throws Exception {
        try {
            byte[] protoBytes = msg.toByteArray();
            int dataLength = protoBytes.length;

            // 根据msg_type映射首部消息类型：0=注册，1=文本，2=图片（其他类型暂归为0x03）
            byte headerMsgType;
            int msgType = msg.getMsgType();
            if (msgType == 0) {
                headerMsgType = 0x00; // 注册消息
            } else if (msgType == 1) {
                headerMsgType = 0x01; // 文本消息
            } else if (msgType == 2) {
                headerMsgType = 0x02; // 图片消息
            }else if (msgType == 4) {
                headerMsgType = 0x04;
            }else if (msgType == 3) {
                headerMsgType = 0x03;
            }
            else {
                headerMsgType = 0x05; // 其他类型（如语音、系统通知等）
            }

            // 分配缓冲区并写入首部+数据
            ByteBuf buf = ctx.alloc().buffer(8 + 1 + 4 + dataLength);
            buf.writeLong(MAGIC_NUMBER);       // 魔数CAFEBABE
            buf.writeByte(headerMsgType);      // 首部类型（0x00/0x01/0x02/0x03）
            buf.writeInt(dataLength);          // 数据长度
            buf.writeBytes(protoBytes);        // Protobuf数据

            out.add(new BinaryWebSocketFrame(true, 0, buf));
            log.debug("编码完成，消息ID: {}, 类型: {}", msg.getMessageId(),
                    getTypeDesc(headerMsgType));

        } catch (OutOfMemoryError e) {
            log.error("内存溢出，关闭连接，消息ID: {}", msg.getMessageId(), e);
            ctx.close();
        } catch (Exception e) {
            log.error("编码失败，消息ID: {}", msg.getMessageId(), e);
        }
    }

    // 辅助方法：打印类型描述
    private String getTypeDesc(byte type) {
        return switch (type) {
            case 0x00 -> "注册消息";
            case 0x01 -> "文本消息";
            case 0x02 -> "图片消息";
            case 0x03 -> "离线消息";
            case 0x04 -> "系统消息";
            default -> "未知类型";
        };
    }
}