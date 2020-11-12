package com.marvel.project.service;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface TranslationService {

  String detectLanguage(String characterFromId) throws IOException;

  String translateCharacter(String characterFromId, String lang, String detectedLanguage) throws IOException, ParseException;
}
