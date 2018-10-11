package movies.spring.data.neo4j.digital.domain.relationship;

import movies.spring.data.neo4j.digital.domain.Comment;
import movies.spring.data.neo4j.digital.domain.Employee;
import org.neo4j.ogm.annotation.*;

/**
 * @author jianfei.yin
 * @create 2018-08-27 11:28 PM
 **/
@RelationshipEntity(type = "COMMENT_BY")
public class CommentedBy {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Employee employee;
    @EndNode
    private Comment comment;

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

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CommentedBy{" +
                "id=" + id +
                ", employee=" + employee +
                ", comment=" + comment +
                '}';
    }
}
