package ir.maktabsharif.hospitalstaffmanagementsystem.dto;

import ir.maktabsharif.hospitalstaffmanagementsystem.model.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestDto {

    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
