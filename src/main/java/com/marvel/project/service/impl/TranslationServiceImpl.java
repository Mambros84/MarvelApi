package com.marvel.project.service.impl;


import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.marvel.project.config.AppConfig;
import com.marvel.project.service.TranslationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class TranslationServiceImpl implements TranslationService {

  @Autowired
  private AppConfig appConfig;

  @Override
  public String detectLanguage(String characterFromId) throws IOException {
    log.debug("Detecting source language for character: " + characterFromId);
      Translate translate = TranslateOptions.newBuilder().setCredentials(ServiceAccountCredentials
              .fromStream(new FileInputStream(appConfig.getCredentialsPath()))).build().getService();

      List<String> text = new LinkedList<>();
      text.add(String.valueOf(characterFromId));
      List<Detection> detections = translate.detect(text);

      return detections.get(0).getLanguage();
  }

  @Override
  public String translateCharacter(String characterFromId, String targetLanguage, String sourceLanguage) throws IOException {
    log.debug("Initiating translation request from " + sourceLanguage + " to " + targetLanguage + " for character: " + characterFromId );
    Translate translate = TranslateOptions.newBuilder().setCredentials(ServiceAccountCredentials
            .fromStream(new FileInputStream(appConfig.getCredentialsPath()))).build().getService();

    Translation translation =
            translate.translate(
                    String.valueOf(characterFromId),
                    Translate.TranslateOption.sourceLanguage(sourceLanguage),
                    Translate.TranslateOption.targetLanguage(targetLanguage),
                    Translate.TranslateOption.format("text"));

    return String.valueOf(translation.getTranslatedText());
  }

}
