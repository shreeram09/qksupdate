package org.shreeram.qksupdate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "person",schema = "upgrade")
@NoArgsConstructor
@Data
public class Person {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String city;

}
