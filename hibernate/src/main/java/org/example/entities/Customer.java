package org.example.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
@Builder
@Setter
@Getter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "sector", length = 100)
    private String sector;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Order> orders;
}
