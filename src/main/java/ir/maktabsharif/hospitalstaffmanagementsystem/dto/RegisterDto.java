package ir.maktabsharif.hospitalstaffmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    private String username;
    private String password;
    private String confirmPassword;
    private String name;
    private String contactNumber;
    private String jobTitle;
    private String department;

}