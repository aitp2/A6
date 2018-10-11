package movies.spring.data.neo4j.digital.domain.relationship;

import movies.spring.data.neo4j.digital.domain.Employee;
import movies.spring.data.neo4j.digital.domain.SmallObjective;
import org.neo4j.ogm.annotation.*;

/**
 * @author jianfei.yin
 * @create 2018-08-27 11:17 PM
 **/
@RelationshipEntity(type = "ERECTED_BY")
public class Flag {
    @Id
    @GeneratedValue
    private Long id;
    @EndNode
    private Employee employee;

    @StartNode
    private SmallObjective smallObjective;



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

    public SmallObjective getSmallObjective() {
        return smallObjective;
    }

    public void setSmallObjective(SmallObjective smallObjective) {
        this.smallObjective = smallObjective;
    }
    @Override
    public String toString() {
        return "Flag{" +
                "id=" + id +
                ", employee=" + employee +
                ", smallObjective=" + smallObjective +
                '}';
    }

}
