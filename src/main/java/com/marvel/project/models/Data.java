package com.marvel.project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@lombok.Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {

  @JsonProperty("offset")
  public Integer offset;
  @JsonProperty("limit")
  public Integer limit;
  @JsonProperty("total")
  public Integer total;
  @JsonProperty("count")
  public Integer count;
  @JsonProperty("results")
  public List<Result> results;

}
