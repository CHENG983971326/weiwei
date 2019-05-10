package com.neuedu.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

public class ResultModel implements Serializable {
        private Logger logger= LoggerFactory.getLogger(ResultModel.class);
        // 定义jackson对象
        private static final ObjectMapper MAPPER = new ObjectMapper();

        // 响应业务状态
        private Integer code;

        // 响应消息
        private String msg;

        // 响应中的数据
        private Object data;

        public static ResultModel build(Integer code, String msg, Object data) {
            return new ResultModel(code, msg, data);
        }

        public static ResultModel ok(Object data) {
            return new ResultModel(data);
        }

        public static ResultModel ok() {
            return new ResultModel(null);
        }

        public ResultModel() {

        }

        public static ResultModel build(Integer code, String msg) {
            return new ResultModel(code, msg, null);
        }

        public ResultModel(Integer code, String msg, Object data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }

        public ResultModel(Object data) {
            this.code = 200;
            this.msg = "OK";
            this.data = data;
        }

//    public Boolean isOK() {
//        return this.code == 200;
//    }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        /**
         * 将json结果集转化为Result对象
         *
         * @param jsonData json数据
         * @param clazz Result中的object类型
         * @return
         */
        public static ResultModel formatToPojo(String jsonData, Class<?> clazz) {
            try {
                if (clazz == null) {
                    return MAPPER.readValue(jsonData, ResultModel.class);
                }
                JsonNode jsonNode = MAPPER.readTree(jsonData);
                JsonNode data = jsonNode.get("data");
                Object obj = null;
                if (clazz != null) {
                    if (data.isObject()) {
                        obj = MAPPER.readValue(data.traverse(), clazz);
                    } else if (data.isTextual()) {
                        obj = MAPPER.readValue(data.asText(), clazz);
                    }
                }
                return build(jsonNode.get("code").intValue(), jsonNode.get("msg").asText(), obj);
            } catch (Exception e) {
                return null;
            }
        }

    /**
     * 没有object对象的转化
     *
     * @param json
     * @return
     */
    public static ResultModel format(String json) {
        try {
            return MAPPER.readValue(json, ResultModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Object是集合转化
     *
     * @param jsonData json数据
     * @param clazz 集合中的类型
     * @return
     */
    public static ResultModel formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("code").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }
}
