package com.odde.atddv2.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class JsonUtils {

    public static final JsonMapper OBJECT_MAPPER = JsonMapper.builder()
            .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS, true)
            .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            // 枚举写成字符串
            .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
            // 时间序列化
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            // 时区
            .defaultLocale(Locale.CHINA)
            .defaultTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
            .addModule(new JavaTimeModule())
            .addModule(new SimpleModule().addSerializer(BigDecimal.class, new ToStringSerializer()))
            .build();

   static {
        // 输入非空字段
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 只显示有的字段
        OBJECT_MAPPER.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        OBJECT_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /**
     * 将对象序列化成json字符串
     *
     * @param value javaBean
     * @param <T>   T 泛型标记
     * @return jsonString json字符串
     */
    public static <T> String toJSONString(T value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }



    public static <T> String toJSONString(ObjectMapper objectMapper, T value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将json反序列化成对象
     *
     * @param content   content
     * @param valueType class
     * @param <T>       T 泛型标记
     * @return Bean
     */
    public static <T> T json2JavaBean(String content, Class<T> valueType) {
        if (Objects.isNull(content)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(content, valueType);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    public static <T> List<T> json2Array(String json, Class<T> valueTypeRef) {

        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, valueTypeRef);
        List<T> objectList = Collections.emptyList();
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return objectList;
    }
    

    /**
     * json to Map
     * @param content
     * @return
     */
    public static Map<String, Object> json2Map(String content) {
        try {
            return OBJECT_MAPPER.readValue(content, Map.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}