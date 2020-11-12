package com.marvel.project.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Setter
@Getter
@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

  @Value("${marvel.url}")
  private String marvelUrl;

  @Value("${public.api.key}")
  private String publicApiKey;

  @Value("${private.api.key}")
  private String privateApiKey;

  @Value("src/main/resources/Credentials.json")
  private String credentialsPath;

  public static final String ROOT = "/marvelapi";
  public static final String CHARACTERS_ENDPOINT = "/characters";
  public static final String CHARACTER_ENDPOINT = "/characters/{id}";
  public static final String CHARACTER_TRANSLATED_ENDPOINT = "/characters/{id}/{targetLanguage}";

  public String getMarvelCharacters() { return marvelUrl + CHARACTERS_ENDPOINT; }

  public String getMarvelCharacter() {
    return marvelUrl + CHARACTER_ENDPOINT;
  }

}
