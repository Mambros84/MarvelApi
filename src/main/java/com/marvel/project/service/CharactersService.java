package com.marvel.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface CharactersService {
  String createCharactersResponseList() throws JsonProcessingException;

  String getCharacterFromId(String id, String lang) throws IOException;
}


