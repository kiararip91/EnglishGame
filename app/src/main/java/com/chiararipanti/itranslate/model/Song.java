

package com.chiararipanti.itranslate.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "author",
        "solution",
        "alternatives"
})
public class Song {

    @JsonProperty("id")
    private int id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("author")
    private String author;
    @JsonProperty("solution")
    private String solution;
    @JsonProperty("alternatives")
    private ArrayList<String> alternatives;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("id")
    public int getid() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    @JsonProperty("solution")
    public void setSolution(String solution) {
        this.solution = solution;
    }

    @JsonProperty("solution")
    public String getSolution(){
        return solution;
    }

    @JsonProperty("alternatives")
    public ArrayList<String> getAlternatives() {
        return alternatives;
    }

    @JsonProperty("alternatives")
    public void setAlternatives(ArrayList<String> alternatives) {
        this.alternatives = alternatives;
    }

}