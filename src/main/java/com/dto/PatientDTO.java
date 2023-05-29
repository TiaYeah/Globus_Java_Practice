package com.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("name")
    private String name;
    @JsonProperty("age")
    private Integer age;
}
