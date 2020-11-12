package com.marvel.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.marvel.project.config.AppConfig;
import com.marvel.project.service.CharactersService;
import com.marvel.project.service.TranslationService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static java.util.Objects.nonNull;

@Slf4j
@RestController
@EnableCaching
@RequestMapping(value = AppConfig.ROOT)
public class Controller {

  @Autowired
  private CharactersService charactersService;

  @Autowired
  private TranslationService translationService;

  @GetMapping(value = AppConfig.CHARACTERS_ENDPOINT, produces = {MediaType.APPLICATION_JSON_VALUE})
  public String charactersResponseList() throws JsonProcessingException {
    log.info("Characters Ids list requested");
      String result = charactersService.createCharactersResponseList();
      if (nonNull(result)) {
        return result;
      } else {
        log.error("The Characters Ids Api request returned no results");
      }
      return null;
  }

  @GetMapping(value = {AppConfig.CHARACTER_ENDPOINT, AppConfig.CHARACTER_TRANSLATED_ENDPOINT}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public String characterFromId(@PathVariable() String id, @PathVariable(required = false) String targetLanguage) throws IOException, ParseException {
    log.info("Character info request initiated");
    if (nonNull(targetLanguage)) {
      log.info("Character info request initiated with translation");
    }
      String result = charactersService.getCharacterFromId(id, targetLanguage);

      if (nonNull(targetLanguage)) {
        String sourceLanguage = translationService.detectLanguage(result);
        String translatedCharacter = translationService.translateCharacter(result, targetLanguage, sourceLanguage);
        if (nonNull(translatedCharacter)) {
          return translatedCharacter;
        } else {
          log.error("The character info request with translation returned no response");
        }
      }
      else if (nonNull(result)){
        return result;
      } else {
        log.error("The character info request returned no response");
      }
      return null;
  }

}
