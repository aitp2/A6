package movies.spring.data.neo4j.digital.domain.relationship;

import movies.spring.data.neo4j.digital.domain.DomainEvent;
import movies.spring.data.neo4j.digital.domain.Employee;
import org.neo4j.ogm.annotation.*;

/**
 * @author jianfei.yin
 * @create 2018-08-27 11:23 PM
 **/
@RelationshipEntity(type = "ATTENDED")
public class Attended {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Employee employee;

    @EndNode
    private DomainEvent domainEvent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public DomainEvent getDomainEvent() {
        return domainEvent;
    }

    public void setDomainEvent(DomainEvent domainEvent) {
        this.domainEvent = domainEvent;
    }
}
