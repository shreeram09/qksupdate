package org.shreeram.qksupdate.helper;
import org.shreeram.qksupdate.domain.Person;
public interface PersonHelper {

    static Person getPersonDomain(){
        return new Person("raghav","jharkhand");
    }
}
