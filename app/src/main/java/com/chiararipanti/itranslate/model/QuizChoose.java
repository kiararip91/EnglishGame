package com.chiararipanti.itranslate.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Created by chiararipanti on 08/02/18.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "question",
        "alternatives",
        "type",
        "solution"
})
public class QuizChoose {

    @JsonProperty("id")
    private String id;
    @JsonProperty("question")
    private String question;
    @JsonProperty("alternatives")
    private List<String> alternatives = null;
    @JsonProperty("type")
    private String type;
    @JsonProperty("solution")
    private String solution;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("question")
    public String getQuestion() {
        return question;
    }

    @JsonProperty("question")
    public void setQuestion(String question) {
        this.question = question;
    }

    @JsonProperty("alternatives")
    public List<String> getAlternatives() {
        return alternatives;
    }

    @JsonProperty("alternatives")
    public void setAlternatives(List<String> alternatives) {
        this.alternatives = alternatives;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("solution")
    public String getSolution() {
        return solution;
    }

    @JsonProperty("solution")
    public void setSolution(String solution) {
        this.solution = solution;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}