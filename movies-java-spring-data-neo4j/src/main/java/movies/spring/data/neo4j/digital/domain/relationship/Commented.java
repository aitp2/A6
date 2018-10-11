package movies.spring.data.neo4j.digital.domain.relationship;

import movies.spring.data.neo4j.digital.domain.Comment;
import movies.spring.data.neo4j.digital.domain.DomainEvent;
import org.neo4j.ogm.annotation.*;

/**
 * @author jianfei.yin
 * @create 2018-08-27 11:25 PM
 **/
@RelationshipEntity(type = "COMMENTED")
public class Commented {
    @Id
    @GeneratedValue
    private Long id;

    @EndNode
    private DomainEvent domainEvent;

    @StartNode
    private Comment comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DomainEvent getDomainEvent() {
        return domainEvent;
    }

    public void setDomainEvent(DomainEvent domainEvent) {
        this.domainEvent = domainEvent;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Commented{" +
                "id=" + id +
                ", domainEvent=" + domainEvent +
                ", comment=" + comment +
                '}';
    }
}
