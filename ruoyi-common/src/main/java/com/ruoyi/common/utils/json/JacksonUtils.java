package com.ruoyi.common.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Jackson JSON 工具类
 * 封装常用的 JSON 序列化和反序列化操作
 */
@Component
public class JacksonUtils {

    /**
     * 全局共享的 ObjectMapper 实例
     * ObjectMapper 是线程安全的，推荐全局单例使用
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // 日期时间格式化常量
    private static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";

    static {
        // 初始化 ObjectMapper 配置
        configureObjectMapper(OBJECT_MAPPER);
    }

    /**
     * 配置 ObjectMapper
     */
    private static void configureObjectMapper(ObjectMapper mapper) {
        // 1. 忽略未知属性，避免 JSON 中有多余字段时反序列化失败
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 2. 空对象序列化时不抛异常
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 3. 配置日期时间处理
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // LocalDateTime 序列化器和反序列化器
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        // LocalDate 序列化器和反序列化器
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT);
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        mapper.registerModule(javaTimeModule);

        // 4. 美化输出时的日期格式
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * 将对象序列化为 JSON 字符串
     * @param obj 要序列化的对象
     * @return JSON 字符串
     * @throws JsonProcessingException 序列化失败时抛出
     */
    public static String toJson(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return "null";
        }
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    /**
     * 将对象序列化为格式化的 JSON 字符串（带缩进）
     * @param obj 要序列化的对象
     * @return 格式化的 JSON 字符串
     * @throws JsonProcessingException 序列化失败时抛出
     */
    public static String toPrettyJson(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return "null";
        }
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象
     * @param json JSON 字符串
     * @param clazz 目标类型的 Class 对象
     * @param <T> 目标类型
     * @return 反序列化后的对象
     * @throws JsonProcessingException 反序列化失败时抛出
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        if (isEmpty(json)) {
            return null;
        }
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    /**
     * 将 JSON 字符串反序列化为复杂类型的对象（如泛型）
     * @param json JSON 字符串
     * @param typeReference 类型引用，例如 new TypeReference<List<User>>() {}
     * @param <T> 目标类型
     * @return 反序列化后的对象
     * @throws JsonProcessingException 反序列化失败时抛出
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) throws JsonProcessingException {
        if (isEmpty(json)) {
            return null;
        }
        return OBJECT_MAPPER.readValue(json, typeReference);
    }

    /**
     * 从输入流中读取 JSON 并反序列化为指定类型的对象
     * @param inputStream 包含 JSON 数据的输入流
     * @param clazz 目标类型的 Class 对象
     * @param <T> 目标类型
     * @return 反序列化后的对象
     * @throws IOException 读取流或反序列化失败时抛出
     */
    public static <T> T fromJson(InputStream inputStream, Class<T> clazz) throws IOException {
        Objects.requireNonNull(inputStream, "输入流不能为 null");
        return OBJECT_MAPPER.readValue(inputStream, clazz);
    }

    /**
     * 将 JSON 字符串反序列化为 List 集合
     * @param json JSON 字符串
     * @param elementType 集合元素类型的 Class 对象
     * @param <T> 集合元素类型
     * @return List 集合
     * @throws JsonProcessingException 反序列化失败时抛出
     */
    public static <T> List<T> toList(String json, Class<T> elementType) throws JsonProcessingException {
        if (isEmpty(json)) {
            return List.of();
        }
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, elementType);
        return OBJECT_MAPPER.readValue(json, javaType);
    }

    /**
     * 将 JSON 字符串反序列化为 Map 集合
     * @param json JSON 字符串
     * @return Map<String, Object> 集合
     * @throws JsonProcessingException 反序列化失败时抛出
     */
    public static Map<String, Object> toMap(String json) throws JsonProcessingException {
        return fromJson(json, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * 将 JSON 字符串反序列化为指定值类型的 Map 集合
     * @param json JSON 字符串
     * @param valueType Map 值的类型
     * @param <V> Map 值的类型
     * @return Map<String, V> 集合
     * @throws JsonProcessingException 反序列化失败时抛出
     */
    public static <V> Map<String, V> toMap(String json, Class<V> valueType) throws JsonProcessingException {
        if (isEmpty(json)) {
            return Map.of();
        }
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, String.class, valueType);
        return OBJECT_MAPPER.readValue(json, javaType);
    }

    /**
     * 将对象转换为 JsonNode，用于灵活操作 JSON 节点
     * @param obj 要转换的对象
     * @return JsonNode 对象
     * @throws JsonProcessingException 转换失败时抛出
     */
    public static JsonNode toJsonNode(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return OBJECT_MAPPER.createObjectNode();
        }
        return OBJECT_MAPPER.valueToTree(obj);
    }

    /**
     * 将 JSON 字符串解析为 JsonNode
     * @param json JSON 字符串
     * @return JsonNode 对象
     * @throws JsonProcessingException 解析失败时抛出
     */
    public static JsonNode parseJsonNode(String json) throws JsonProcessingException {
        if (isEmpty(json)) {
            return OBJECT_MAPPER.createObjectNode();
        }
        return OBJECT_MAPPER.readTree(json);
    }

    /**
     * 将对象序列化并写入输出流
     * @param obj 要序列化的对象
     * @param outputStream 目标输出流
     * @throws IOException 写入流失败时抛出
     */
    public static void writeJson(Object obj, OutputStream outputStream) throws IOException {
        Objects.requireNonNull(outputStream, "输出流不能为 null");
        OBJECT_MAPPER.writeValue(outputStream, obj);
    }

    /**
     * 检查字符串是否为空
     * @param str 字符串
     * @return 为空返回 true，否则返回 false
     */
    private static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 获取 ObjectMapper 实例，用于特殊配置
     * @return ObjectMapper 实例
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}
