package bci.api.model;

import bci.api.entity.User;
import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long number;
    private Integer citycode;
    private String contrycode;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;
}