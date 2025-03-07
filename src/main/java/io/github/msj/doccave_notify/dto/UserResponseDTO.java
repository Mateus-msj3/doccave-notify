package io.github.msj.doccave_notify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private UUID id;

    private String firstname;

    private String lastname;

    private LocalDate dateOfBirth;

    private String email;

    private String phoneNumber;

    private String urlImageProfile;

}
