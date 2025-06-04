package bci.api.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String token;

    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime lastLogin;
    private boolean active;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneEntity> phones;
}