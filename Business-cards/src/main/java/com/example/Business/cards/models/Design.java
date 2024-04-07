package com.example.Business.cards.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "Design")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Design {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @NotEmpty
    @Column(name = "font")
    private String font;


    @Min(1)
    @Max(100)
    @Column(name = "letterHeight")
    private int letterHeight;

    @OneToMany(mappedBy = "design")
    private List<Request> requests;

    public Design(String font, int letterHeight) {
        this.font = font;
        this.letterHeight = letterHeight;
    }
}
