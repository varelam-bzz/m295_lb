package ch.matias.m295_lb.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String name;
    @Column(name = "Release_Date")
    @NotNull
    private LocalDate releaseDate;
    @Column(precision = 10, scale = 2)
    @Max(value = 300, message = "Game price should not exceed CHF 300.")
    @NotNull
    private BigDecimal price;
    @PositiveOrZero(message = "There cannot be negative purchases for a game.")
    @NotNull
    private Integer purchases;
    @NotNull
    private Boolean released;
    @ManyToOne
    @JoinColumn(name = "Publisher_ID", nullable = false)
    @NotNull
    private Publisher publisher;
}
