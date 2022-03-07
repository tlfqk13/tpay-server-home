package com.tpay.domains.employee;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.domains.employee.application.dto.EmployeeRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class EmployeeRegistrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void 멤버등록_성공() throws Exception {
    String json = "{\n" +
        " \"name\": \"하직원\",\n" +
        " \"userId\": \"nsg2311\",\n" +
        " \"password\": \"qq123456!!\",\n" +
        " \"passwordCheck\": \"qq123456!!\"\n" +
        "}";
    EmployeeRegistrationRequest employeeRegistrationRequest = objectMapper.readValue(json, EmployeeRegistrationRequest.class);

    mockMvc.perform(post("/employee/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andReturn();
  }

}
