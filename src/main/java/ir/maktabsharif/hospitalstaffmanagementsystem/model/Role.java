package ir.maktabsharif.hospitalstaffmanagementsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity<Long> implements Serializable {

    @Column(unique = true, nullable = false)
    private String name;

    private Set<String> authorities;

}
