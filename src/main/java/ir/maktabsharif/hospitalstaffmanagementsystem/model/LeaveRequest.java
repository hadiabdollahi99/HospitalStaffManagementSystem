package ir.maktabsharif.hospitalstaffmanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest extends BaseEntity<Long> implements Serializable {

    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String managerComment;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private User staff;

}