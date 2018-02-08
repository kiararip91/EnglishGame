package com.chiararipanti.itranslate.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author chiararipanti
 *         date 04/05/2013
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "english",
        "italian",
        "it",
        "es",
        "fr",
        "de",
        "sentence",
        "image",
        "level"
})
public class Word {

    @JsonProperty("id")
    private String id;
    @JsonProperty("english")
    private String english;
    @JsonProperty("italian")
    private String italian;
    @JsonProperty("it")
    private String it;
    @JsonProperty("es")
    private String es;
    @JsonProperty("fr")
    private String fr;
    @JsonProperty("de")
    private String de;
    @JsonProperty("sentence")
    private String sentence;
    @JsonProperty("image")
    private String image;
    @JsonProperty("level")
    private String level;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("english")
    public String getEnglish() {
        return english;
    }

    @JsonProperty("english")
    public void setEnglish(String english) {
        this.english = english;
    }

    @JsonProperty("italian")
    public String getItalian() {
        return italian;
    }

    @JsonProperty("italian")
    public void setItalian(String italian) {
        this.italian = italian;
    }

    @JsonProperty("it")
    public String getIt() {
        return it;
    }

    @JsonProperty("it")
    public void setIt(String it) {
        this.it = it;
    }

    @JsonProperty("es")
    public String getEs() {
        return es;
    }

    @JsonProperty("es")
    public void setEs(String es) {
        this.es = es;
    }

    @JsonProperty("fr")
    public String getFr() {
        return fr;
    }

    @JsonProperty("fr")
    public void setFr(String fr) {
        this.fr = fr;
    }

    @JsonProperty("de")
    public String getDe() {
        return de;
    }

    @JsonProperty("de")
    public void setDe(String de) {
        this.de = de;
    }

    @JsonProperty("sentence")
    public String getSentence() {
        return sentence;
    }

    @JsonProperty("sentence")
    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    @JsonProperty("level")
    public String getLevel() {
        return level;
    }

    @JsonProperty("level")
    public void setLevel(String level) {
        this.level = level;
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