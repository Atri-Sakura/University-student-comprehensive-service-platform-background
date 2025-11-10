package com.ruoyi.platform.chat.codec;

import com.ruoyi.platform.chat.protobuf.ChatMessageProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class WebSocketToProtobufDecoder extends MessageToMessageDecoder<WebSocketFrame> {

    // 魔数：CAFEBABE（8字节，与编码器保持一致）
    private static final long MAGIC_NUMBER = 0xCAFEBABEL;

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
        // 1. 只处理二进制帧，其他类型（文本、Ping等）忽略（不关闭连接）
        if (!(frame instanceof BinaryWebSocketFrame)) {
            log.warn("忽略非二进制WebSocket帧，类型: {}", frame.getClass().getSimpleName());
            return;
        }

        BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame) frame;
        ByteBuf content = binaryFrame.content();

        try {
            // 2. 校验消息最小长度（8字节魔数 + 1字节类型 + 4字节长度 = 13字节）
            if (content.readableBytes() < 13) {
                log.warn("消息长度不足（至少13字节），实际: {}字节，忽略", content.readableBytes());
                return;
            }

            // 3. 读取并校验魔数（过滤非法消息）
            long magic = content.readLong();
            if (magic != MAGIC_NUMBER) {
                log.warn("非法魔数，预期CAFEBABE，实际: 0x{}，关闭连接",
                        Long.toHexString(magic).toUpperCase());
                ctx.close(); // 非法消息直接关闭连接，防止恶意攻击
                return;
            }

            // 4. 读取首部消息类型（0x00=注册，0x01=文本，0x02=图片，0x03=其他）
            byte headerMsgType = content.readByte();
            if (headerMsgType < 0x00 || headerMsgType > 0x05) {
                log.warn("未知首部类型: 0x{}，忽略", Integer.toHexString(headerMsgType));
                return;
            }

            // 5. 读取数据长度并校验（防止数据越界）
            int dataLength = content.readInt();
            if (dataLength <= 0 || dataLength > content.readableBytes()) {
                log.warn("数据长度异常，声明长度: {}字节，实际剩余: {}字节，忽略",
                        dataLength, content.readableBytes());
                return;
            }

            // 6. 读取Protobuf数据并解析
            byte[] protoBytes = new byte[dataLength];
            content.readBytes(protoBytes);
            ChatMessageProto.ChatMessage message = ChatMessageProto.ChatMessage.parseFrom(protoBytes);

            // 7. 校验首部类型与消息体中msg_type的一致性
            int msgType = message.getMsgType();
            boolean typeMatch = switch (headerMsgType) {
                case 0x00 -> msgType == 0; // 注册消息必须对应msg_type=0
                case 0x01 -> msgType == 1; // 文本消息必须对应msg_type=1
                case 0x02 -> msgType == 2; // 图片消息必须对应msg_type=2
                case 0x03 -> msgType == 3; // 其他类型对应msg_type≥3（离线消息）
                case 0x04 -> msgType == 4;
                case 0x05 -> msgType == 5;//撤回消息
                default -> false;
            };

            if (!typeMatch) {
                log.warn("消息类型不匹配，首部类型: 0x{}，msg_type: {}，忽略消息ID: {}",
                        Integer.toHexString(headerMsgType), msgType, message.getMessageId());
                return;
            }

            // 8. 解析成功，传递给下一个处理器
            out.add(message);
            log.debug("WebSocket帧解码完成，消息ID: {}, 类型: {}",
                    message.getMessageId(), getTypeDesc(headerMsgType));

        } catch (Exception e) {
            log.error("WebSocket帧解码失败", e);
            // 解码失败不关闭连接（除非是致命错误，如协议恶意破坏）
        }
    }

    // 辅助方法：将首部类型转换为可读描述
    private String getTypeDesc(byte type) {
        return switch (type) {
            case 0x00 -> "注册消息";
            case 0x01 -> "文本消息";
            case 0x02 -> "图片消息";
            case 0x03 -> "离线消息";
            case 0x04 -> "系统消息";
            case 0x05 -> "撤回消息";
            default -> "未知类型";
        };
    }
}