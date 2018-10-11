package movies.spring.data.neo4j.digital.domain.relationship;

import movies.spring.data.neo4j.digital.domain.Employee;
import movies.spring.data.neo4j.digital.domain.SmallInvitation;
import org.neo4j.ogm.annotation.*;

/**
 * @author jianfei.yin
 * @create 2018-08-27 11:10 PM
 **/
@RelationshipEntity(type = "LAUNCHED_BY")
public class Activity {
    @Id
    @GeneratedValue
    private Long id;
    @StartNode
    private SmallInvitation smallInvitation;
    @EndNode
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SmallInvitation getSmallInvitation() {
        return smallInvitation;
    }

    public void setSmallInvitation(SmallInvitation smallInvitation) {
        this.smallInvitation = smallInvitation;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", smallInvitation=" + smallInvitation +
                ", employee=" + employee +
                '}';
    }
}
