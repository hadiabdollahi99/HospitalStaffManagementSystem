package ir.maktabsharif.hospitalstaffmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private String name;
    private String contactNumber;
    private String jobTitle;
    private String department;
}