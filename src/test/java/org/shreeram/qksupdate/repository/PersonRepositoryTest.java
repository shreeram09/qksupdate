package org.shreeram.qksupdate.repository;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.shreeram.qksupdate.entity.Person;
import org.shreeram.qksupdate.helper.PersonHelper;
import org.shreeram.qksupdate.mapper.PersonMapper;
import io.quarkus.logging.Log;

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

    /**
     * given domain with id , should add unmanaged entity (merge)
     * earlier merge used to insert record if id/primary-key is not found even with GeneratedValue(Auto) strategy for Id
     * with recent Hibernate 6.6.x version merge is now more restrictive on primary-key
     * for optimistic transaction locking
     * */
    @Test
    @TestTransaction
    void givenDomainId_whenSave_addUnmanagedRow(){
        var domain = PersonHelper.getPersonDomain();
        var unmanaged = mapper.mapToEntity(domain);
        //commented as with generatedValue strategy for id, merge expecting row with given id/primary-key in database
        //else throws OptimisticLockException : Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect)
        //unmanaged.setId(2L);
        unmanaged.setCity("Uttarakhand");
        var managed = repository.save(unmanaged);
        assertNotNull(managed);
        assertEquals("Uttarakhand",managed.getCity());
    }

    /**
     * given domain without id , should add entity (persist)
     */
    @Test
    @TestTransaction
    void givenDomain_whenAdd_insertNewRow(){
        var domain = PersonHelper.getPersonDomain();
        var unmanaged = mapper.mapToEntity(domain);
        repository.add(unmanaged);
        assertNotNull(unmanaged);
        assertEquals(domain.name(),unmanaged.getName());
        assertEquals(domain.city(),unmanaged.getCity());
    }

    /**
     * given domain with id , should add entity (persist)
     */
    @Test
    @TestTransaction
    void givenDomainId_whenAdd_addRow(){
        var domain = PersonHelper.getPersonDomain();
        var entity = mapper.mapToEntity(domain);
        entity.setId(2L);
        Exception e = assertThrows(PersistenceException.class,
                ()->  repository.add(entity),
                ()->"unmanaged/detached/non-persistent entity cannot be persisted/inserted/updated") ;
        Log.errorv("type: {0}, message: {1}",e.getClass().getName(),e.getMessage());
    }

    @Test/*given domain with id , should not update unmanaged entity (persist)*/
    @TestTransaction
    void givenId_whenAdd_updateUnmanagedRow(){
        var domain = PersonHelper.getPersonDomain();
        var entity = mapper.mapToEntity(domain);
        repository.add(entity);
        var managed = repository.getEntityManager().find(Person.class,entity.getId());
        assertNotNull(managed);
        var unmanaged= new Person();
        Log.infov("is persistent: {0}",repository.isPersistent(unmanaged));
        unmanaged.setId(managed.getId());
        Log.infov("is persistent: {0}",repository.isPersistent(unmanaged));
        unmanaged.setCity("Haryana");
        Exception e = assertThrows(PersistenceException.class,
                ()->  repository.add(unmanaged),
                ()->"unmanaged/detached/non-persistent entity cannot be persisted/inserted/updated") ;
        Log.errorv("type: {0}, message: {1}",e.getClass().getName(),e.getMessage());
    }

    /**
     * strict type checking introduced since Quarkus 3.7 with hibernate 6.4.x,
     * so this test case while using function('db_function') in HQL
     * since it returns java.lang.Object while query parsing,
     * here db_function could be any inbuilt or userdefined function
     * eg. function('replace',col_name,'charToRemove','charToReplace')<br>
     * issue:<a href="https://github.com/hibernate/hibernate-orm/pull/7275">HHH-1678</a>
     * */
    @Test
    void givenString_whenFunctionInHQL_returnsFormattedString(){
        var hql = "from Person p where replace(lower(city),' ','') like '%n'";
        var result = repository.getEntityManager()
                .createQuery(hql,Person.class)
                .setMaxResults(1)
                .getSingleResult();
        assertNotNull(result);
        Log.infov("found {0}",result);
    }
}