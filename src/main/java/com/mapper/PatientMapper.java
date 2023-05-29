package com.mapper;

import com.dto.PatientDTO;
import com.entity.PatientEntity;
import com.generated.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(target = "surname", source = "patient_surname")
    @Mapping(target = "name", source = "patient_name")
    @Mapping(target = "age", source = "patient_age")
    PatientDTO mapWithoutId(PatientEntity patient);

    @Mapping(target = "patient_surname", source = "surname")
    @Mapping(target = "patient_name", source = "name")
    @Mapping(target = "patient_age", source = "age")
    PatientEntity mapGenerated(Patient generated);

}
