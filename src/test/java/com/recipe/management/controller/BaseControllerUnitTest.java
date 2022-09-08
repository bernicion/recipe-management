package com.recipe.management.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.inject.Inject;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseControllerUnitTest {

    @Inject
    protected MockMvc mockMvc;

    protected static ObjectMapper jsonMapper;

    @BeforeAll
    static void init() {
        jsonMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonMapper.registerModule(new JavaTimeModule());
        jsonMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
    }

    protected ResultActions performPOSTRequest(String endPoint, String body) throws Exception {
        return mockMvc.perform(
                post(endPoint)
                        .headers(baseHeaders())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
    }

    protected ResultActions performPUTRequest(String endPoint, Object param, String body) throws Exception {
        return mockMvc.perform(
                put(endPoint + param)
                        .headers(baseHeaders())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
    }

    protected ResultActions performGETRequestWithPathParam(String endPoint, Object param) throws Exception {
        var url = param!=null ? endPoint + param: endPoint;
        return mockMvc.perform(
                get(url)
                        .headers(baseHeaders())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
    }

    protected ResultActions performGETRequestWithPagination(String endPoint, Integer page, Integer size) throws Exception {
        return mockMvc.perform(
                get(endPoint + "/?page=" + page + "&size=" + size)
                        .headers(baseHeaders())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
    }

    protected ResultActions performDeleteRequestWithPathParam(String endPoint, Object param) throws Exception {
        return mockMvc.perform(
                delete(endPoint + param)
                        .headers(baseHeaders())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
    }

    protected HttpHeaders baseHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setHost(InetSocketAddress.createUnresolved("a.recipe.management", 8080));
        return headers;
    }
}
