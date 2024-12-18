package org.shreeram.qksupdate.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.shreeram.qksupdate.entity.Person;


@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {

    public Person save(Person person){
        return getEntityManager().merge(person);
    }

    public void add(Person person){
        getEntityManager().persist(person);
    }
}
