package org.example.entities;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "line_items")
@Builder
@Setter
@Getter
public class LineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_item_id")
    private Long Id;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")  // Внешний ключ
    private Order order;
}
