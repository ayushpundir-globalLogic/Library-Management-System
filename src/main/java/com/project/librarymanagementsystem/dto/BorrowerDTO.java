package com.project.librarymanagementsystem.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowerDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDate membershipDate;
    private boolean hasOverdueItems;
}
