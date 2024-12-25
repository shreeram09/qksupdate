package org.shreeram.qksupdate.repository;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.hibernate.PersistentObjectException;
import org.junit.jupiter.api.Test;
import org.shreeram.qksupdate.entity.Person;
import org.shreeram.qksupdate.helper.PersonHelper;
import org.shreeram.qksupdate.mapper.PersonMapper;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PersonRepositoryTest {

    @Inject
    PersonRepository repository;

    @Inject
    PersonMapper mapper;

    @Test/*given domain without id , should add entity (merge)*/
    @TestTransaction
    void givenDomain_whenSave_insertNewRow(){
        var domain = PersonHelper.getPersonDomain();
        var mapped = mapper.mapToEntity(domain);
        var entity = repository.save(mapped);
        assertNotNull(entity);
        assertEquals(domain.name(),entity.getName());
        assertEquals(domain.city(),entity.getCity());
    }

    @Test/*with given id , should update entity (merge)*/
    @TestTransaction
    void givenId_whenSave_updateRow(){
        var domain = PersonHelper.getPersonDomain();
        var mapped = mapper.mapToEntity(domain);
        var entity = repository.save(mapped);
        var managed = repository.getEntityManager().find(Person.class,entity.getId());
        assertNotNull(managed);
        managed.setId(managed.getId());
        managed.setCity("Uttarakhand");
        repository.save(managed);
        assertEquals(entity.getId(),managed.getId());
        assertEquals("Uttarakhand",managed.getCity());
    }

    @Test/*with given id , should update unmanaged entity (merge)*/
    @TestTransaction
    void givenId_whenSave_updateUnmanagedRow(){
        var domain = PersonHelper.getPersonDomain();
        var mapped = mapper.mapToEntity(domain);
        var entity = repository.save(mapped);
        var managed = repository.getEntityManager().find(Person.class,entity.getId());
        assertNotNull(managed);
        var detached= new Person();
        detached.setId(managed.getId());
        detached.setCity("Uttarakhand");
        repository.save(detached);
        assertEquals(managed.getId(),detached.getId());
        assertEquals("Uttarakhand",detached.getCity());
    }

    @Test/*with given domain without id , should add entity (persist)*/
    @TestTransaction
    void givenDomain_whenAdd_insertNewRow(){
        var domain = PersonHelper.getPersonDomain();
        var entity = mapper.mapToEntity(domain);
        repository.add(entity);
        assertNotNull(entity);
        assertEquals(domain.name(),entity.getName());
        assertEquals(domain.city(),entity.getCity());
    }

    @Test/*with id , should not update entity (persist)*/
    @TestTransaction
    void givenId_whenAdd_updateRow(){
        var domain = PersonHelper.getPersonDomain();
        var entity = mapper.mapToEntity(domain);
        repository.add(entity);
        var managed = repository.getEntityManager().find(Person.class,entity.getId());
        assertNotNull(managed);
        managed.setCity("Haryana");
        repository.add(entity);
        assertNotNull(entity.getId());
        assertEquals("Haryana",managed.getCity());
    }

    @Test/*with id , should not update unmanaged entity (persist)*/
    @TestTransaction
    void givenId_whenAdd_updateUnmanagedRow(){
        var domain = PersonHelper.getPersonDomain();
        var entity = mapper.mapToEntity(domain);
        repository.add(entity);
        var managed = repository.getEntityManager().find(Person.class,entity.getId());
        assertNotNull(managed);
        var detached= new Person();
        detached.setId(managed.getId());
        detached.setCity("Haryana");
        assertThrows(PersistentObjectException.class,()-> repository.add(detached),()->"unmanaged/detached/non-persistent entity cannot be persisted/inserted/updated") ;
//        assertEquals(managed.getId(),detached.getId());
//        assertEquals("Haryana",detached.getCity());
    }

    @Test
    void givenString_whenFunctionInHQL_returnsFormattedString(){
        var hql = "from Person p where function('replace',city,'#',' ') like '%n'";
        var result = repository.getEntityManager().createQuery(hql,Person.class).getResultList().stream().findFirst();
        assertNotNull(result);
        result.ifPresent(System.out::println);
    }
}