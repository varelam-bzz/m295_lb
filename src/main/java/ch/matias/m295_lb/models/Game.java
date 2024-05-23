package ch.matias.m295_lb.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Game")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Size(max = 45, message = "Game name cannot exceed 45 characters.")
    private String name;
    @Column(name = "Release_Date")
    private LocalDate releaseDate;
    @Column(precision = 10, scale = 2)
    @Max(value = 300, message = "Game price should not exceed CHF 300.")
    private BigDecimal price;
    @PositiveOrZero(message = "There cannot be negative purchases for a game.")
    private Integer purchases;
    private Boolean released;
    @ManyToOne
    @JoinColumn(name = "Publisher_ID", nullable = false)
    private Publisher publisher;
}
