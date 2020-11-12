package com.marvel.project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marvel.project.config.AppConfig;
import com.marvel.project.models.CharactersResponse;
import com.marvel.project.models.Result;
import com.marvel.project.service.CharactersService;
import com.marvel.project.service.TranslationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class CharactersServiceImpl implements CharactersService {

  @Autowired
  private AppConfig appConfig;

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  TranslationService translationService;

  private static final String API_KEY = "apikey";

  private static final String HASH = "hash";

  private static final String TIMESTAMP = "ts";

  private static final String LIMIT = "limit";

  private static final String OFFSET = "offset";

  @Override
  @Cacheable(value = "charactersIdsList")
  public String createCharactersResponseList() throws JsonProcessingException {
    log.info("Building characters ids request");
    String url = appConfig.getMarvelCharacters();
    UriComponentsBuilder builder = getUriComponentsBuilder(url);

    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
    HttpEntity httpEntity = new HttpEntity(requestHeaders);

    List<String> charactersIdsList = new ArrayList<>();
    HttpEntity<String> response;
    CharactersResponse responseCharacters;

    int total;
    int limit = 100;
    int offset = 0;
      do {
        response = restTemplate.exchange(builder.replaceQueryParam(LIMIT, limit)
                        .replaceQueryParam(OFFSET, offset).toUriString(),
                HttpMethod.GET, httpEntity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        responseCharacters = mapper.readValue(response.getBody(), CharactersResponse.class);
        total = responseCharacters.getData().getTotal();

        for (int i = 0; i < responseCharacters.getData().getCount(); i++) {
          if (nonNull(responseCharacters.getData().getResults().get(i).getId()) &&
                  !charactersIdsList.contains(responseCharacters.getData().getResults().get(i).getId())) {
            charactersIdsList.add(i, String.valueOf(responseCharacters.getData().getResults().get(i).getId()));
          }
        }
        log.debug("The current offset is " + offset);
        offset = offset + 100;
      } while (offset <= total);

      log.info("Characters Ids fetching completed");
      log.debug("Characters Ids Api request returned the following response: " + charactersIdsList);

    return String.valueOf(charactersIdsList);
  }

  @Override
  public String getCharacterFromId(String id, String lang) throws IOException {
    log.info("Building character info request");
    if (nonNull(lang)) {
      log.info("Building character info request with translation");
    }
    String url = appConfig.getMarvelCharacter().replace("{id}", id);
    UriComponentsBuilder builder = getUriComponentsBuilder(url);

    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);

    HttpEntity httpEntity = new HttpEntity(requestHeaders);

    HttpEntity<String> response;

    response = restTemplate.exchange(builder.toUriString(),
            HttpMethod.GET, httpEntity, String.class);

    ObjectMapper mapper = new ObjectMapper();
    CharactersResponse responseCharacters = mapper.readValue(response.getBody(), CharactersResponse.class);

    Result characterFromId = responseCharacters.getData().getResults().get(0);

    if (characterFromId == null) {
      log.error("The Marvel Api returned zero results for character id: " + id);
    }
    String formattedCharacterFromId = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(characterFromId);

    return formattedCharacterFromId;
  }

  /**
   *
   * @param url
   * @return
   */
  private UriComponentsBuilder getUriComponentsBuilder(String url) {
    long timeStamp = System.currentTimeMillis();
    String stringToHash = timeStamp + appConfig.getPrivateApiKey() + appConfig.getPublicApiKey();
    String hash = DigestUtils.md5Hex(stringToHash);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam(TIMESTAMP, valueOf(timeStamp))
            .queryParam(API_KEY, appConfig.getPublicApiKey())
            .queryParam(HASH, hash);
    return builder;
  }
}
