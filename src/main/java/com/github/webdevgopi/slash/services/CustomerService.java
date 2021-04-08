package com.github.webdevgopi.slash.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.webdevgopi.slash.config.SlashGraphQlProperties;
import com.github.webdevgopi.slash.models.Customer;
import com.github.webdevgopi.slash.models.graphQL.SlashGraphQlResultCustomer;
import com.github.webdevgopi.slash.utils.RestTemplateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {
    private final SlashGraphQlProperties slashGraphQlProperties;

    private static final String CUSTOMER_QUERY = "query { queryCustomer { username } }";
//    private static final String HOST_NAME = "https://quiet-hill.ap-south-1.aws.cloud.dgraph.io";
    public List<Customer> getCustomers() throws Exception {
        ResponseEntity<String> responseEntity = RestTemplateUtils.query(slashGraphQlProperties.getHostname(), CUSTOMER_QUERY);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SlashGraphQlResultCustomer slashGraphQlResult = objectMapper.readValue(responseEntity.getBody(), SlashGraphQlResultCustomer.class);
            log.debug("slashGraphQlResult={}", slashGraphQlResult);
            return slashGraphQlResult.getData().getQueryCustomer();
        } catch (JsonProcessingException e) {
            throw new Exception("An error was encountered processing responseEntity=" + responseEntity.getBody(), e);
        }
    }
}
