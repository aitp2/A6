package movies.spring.data.neo4j.digital.domain.relationship;

import movies.spring.data.neo4j.digital.domain.DomainEvent;
import movies.spring.data.neo4j.digital.domain.Employee;
import movies.spring.data.neo4j.digital.domain.Tag;

import org.neo4j.ogm.annotation.*;


@RelationshipEntity(type = "BELON_TO")
public class HasTag {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Employee employee;

    @EndNode
    private Tag tag;

    public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

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

}
