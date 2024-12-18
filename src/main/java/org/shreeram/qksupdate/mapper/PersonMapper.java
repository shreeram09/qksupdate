package org.shreeram.qksupdate.mapper;
import org.mapstruct.Mapper;
import org.shreeram.qksupdate.domain.Person;

@Mapper(componentModel = "cdi")
public interface PersonMapper {
    org.shreeram.qksupdate.entity.Person mapToEntity(Person domain);
}
