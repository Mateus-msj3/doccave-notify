package io.github.msj.doccave_notify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingGoalResponseDTO {

    private UUID id;

    private String name;

    private String description;

    private String goalType;

    private Integer goalPages;

    private Integer pagesRead;

    private Integer goalBooks;

    private LocalDate startDate;

    private LocalDate endDate;

    private String status;

    private int pagesPerDay;

    private boolean goalAchieved;

    private UUID userReadingId;

    private UUID userId;

    private UserResponseDTO user;

    private Set<UUID> selectedBooks;

    private Set<String> booksNames;

    private Integer booksCompleted;

    private LocalDateTime createdDate;

    private String createdBy;

    private LocalDateTime lastModifiedDate;

    private String lastModifiedBy;

}
