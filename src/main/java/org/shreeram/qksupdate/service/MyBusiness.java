package org.shreeram.qksupdate.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.shreeram.qksupdate.domain.Person;
import org.shreeram.qksupdate.mapper.PersonMapper;
import org.shreeram.qksupdate.repository.PersonRepository;

@ApplicationScoped
public class MyBusiness {
    @Inject
    PersonMapper mapper;

    @Inject
    PersonRepository resource;

    @Transactional
    public org.shreeram.qksupdate.entity.Person save(Person person){
        var entity = mapper.mapToEntity(person);
        return resource.save(entity);
    }
}
