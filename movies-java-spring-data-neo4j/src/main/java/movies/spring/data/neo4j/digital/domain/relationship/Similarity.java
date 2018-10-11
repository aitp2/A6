package movies.spring.data.neo4j.digital.domain.relationship;

import movies.spring.data.neo4j.digital.domain.Employee;
import org.neo4j.ogm.annotation.*;

import java.math.BigDecimal;

/**
 * @author jianfei.yin
 * @create 2018-08-30 10:30 PM
 **/
@RelationshipEntity(type = "SIMILAR_TO")
public class Similarity {
    @Id
    @GeneratedValue
    private Long id;

    private BigDecimal similarity;
    @StartNode
    private Employee employee1;
    @EndNode
    private Employee employee2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSimilarity() {
        return similarity;
    }

    public void setSimilarity(BigDecimal similarity) {
        this.similarity = similarity;
    }

    public Employee getEmployee1() {
        return employee1;
    }

    public void setEmployee1(Employee employee1) {
        this.employee1 = employee1;
    }

    public Employee getEmployee2() {
        return employee2;
    }

    public void setEmployee2(Employee employee2) {
        this.employee2 = employee2;
    }
}
