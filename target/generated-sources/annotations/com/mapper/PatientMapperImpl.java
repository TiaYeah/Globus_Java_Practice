package com.mapper;

import com.dto.PatientDTO;
import com.entity.PatientEntity;
import com.generated.Patient;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-29T12:59:46+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class PatientMapperImpl implements PatientMapper {

    @Override
    public PatientDTO mapWithoutId(PatientEntity patient) {
        if ( patient == null ) {
            return null;
        }

        PatientDTO patientDTO = new PatientDTO();

        patientDTO.setSurname( patient.getPatient_surname() );
        patientDTO.setName( patient.getPatient_name() );
        patientDTO.setAge( patient.getPatient_age() );

        return patientDTO;
    }

    @Override
    public PatientEntity mapGenerated(Patient generated) {
        if ( generated == null ) {
            return null;
        }

        PatientEntity patientEntity = new PatientEntity();

        patientEntity.setPatient_surname( generated.getSurname() );
        patientEntity.setPatient_name( generated.getName() );
        if ( generated.getAge() != null ) {
            patientEntity.setPatient_age( generated.getAge().intValue() );
        }

        return patientEntity;
    }
}
