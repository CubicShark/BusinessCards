package com.example.Business.cards.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "Worker")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Worker {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Size(min = 2, max = 40, message = "Количество символов в фио должно быть от 2 до 40")
    @Column(name = "fullName")
    private String fullName;


    @Pattern(regexp="^\\+[0-9]{12}$", message="Некорректный формат номера телефона")
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @OneToMany(mappedBy = "worker")
    private List<Request> requests;

    public Worker(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }
}
