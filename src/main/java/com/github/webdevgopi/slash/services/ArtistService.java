package com.github.webdevgopi.slash.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.webdevgopi.slash.config.SlashGraphQlProperties;
import com.github.webdevgopi.slash.models.Artist;
import com.github.webdevgopi.slash.models.graphQL.SlashGraphQlResultArtist;
import com.github.webdevgopi.slash.utils.RestTemplateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArtistService {
    private final SlashGraphQlProperties slashGraphQlProperties;

    private static final String ARTIST_QUERY = "query { queryArtist { name } }";
//    private static final String HOST_NAME = "https://quiet-hill.ap-south-1.aws.cloud.dgraph.io";
    public List<Artist> getAllArtists() throws Exception {
        ResponseEntity<String> responseEntity = RestTemplateUtils.query(slashGraphQlProperties.getHostname(), ARTIST_QUERY);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SlashGraphQlResultArtist slashGraphQlResult = objectMapper.readValue(responseEntity.getBody(), SlashGraphQlResultArtist.class);
            log.debug("slashGraphQlResult={}", slashGraphQlResult);
            return slashGraphQlResult.getData().getQueryArtist();
        } catch (JsonProcessingException e) {
            throw new Exception("An error was encountered processing responseEntity=" + responseEntity.getBody(), e);
        }
    }
}
