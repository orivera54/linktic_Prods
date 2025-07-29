package com.linktic.productoservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonApiResponse<T> {

    @JsonProperty("data")
    private T data;

    @JsonProperty("errors")
    private List<JsonApiError> errors;

    @JsonProperty("meta")
    private Meta meta;

    @JsonProperty("links")
    private Links links;

    public JsonApiResponse() {}

    public JsonApiResponse(T data) {
        this.data = data;
    }

    public JsonApiResponse(List<JsonApiError> errors) {
        this.errors = errors;
    }

    public static <T> JsonApiResponse<T> success(T data) {
        return new JsonApiResponse<>(data);
    }

    public static <T> JsonApiResponse<T> success(T data, Meta meta) {
        JsonApiResponse<T> response = new JsonApiResponse<>(data);
        response.meta = meta;
        return response;
    }

    public static <T> JsonApiResponse<T> error(List<JsonApiError> errors) {
        return new JsonApiResponse<>(errors);
    }

    public static <T> JsonApiResponse<T> error(String title, String detail, String status) {
        JsonApiError error = new JsonApiError(title, detail, status);
        return new JsonApiResponse<>(List.of(error));
    }

    // Getters and Setters
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<JsonApiError> getErrors() {
        return errors;
    }

    public void setErrors(List<JsonApiError> errors) {
        this.errors = errors;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JsonApiError {
        @JsonProperty("title")
        private String title;

        @JsonProperty("detail")
        private String detail;

        @JsonProperty("status")
        private String status;

        @JsonProperty("timestamp")
        private LocalDateTime timestamp;

        public JsonApiError() {
            this.timestamp = LocalDateTime.now();
        }

        public JsonApiError(String title, String detail, String status) {
            this();
            this.title = title;
            this.detail = detail;
            this.status = status;
        }

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Meta {
        @JsonProperty("timestamp")
        private LocalDateTime timestamp;

        @JsonProperty("version")
        private String version;

        public Meta() {
            this.timestamp = LocalDateTime.now();
            this.version = "1.0.0";
        }

        // Getters and Setters
        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Links {
        @JsonProperty("self")
        private String self;

        @JsonProperty("next")
        private String next;

        @JsonProperty("prev")
        private String prev;

        public Links() {}

        public Links(String self) {
            this.self = self;
        }

        // Getters and Setters
        public String getSelf() {
            return self;
        }

        public void setSelf(String self) {
            this.self = self;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public String getPrev() {
            return prev;
        }

        public void setPrev(String prev) {
            this.prev = prev;
        }
    }
}