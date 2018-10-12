package movies.spring.data.neo4j.digital.domain.relationship;

import movies.spring.data.neo4j.digital.domain.DomainEvent;
import movies.spring.data.neo4j.digital.domain.Tag;

import org.neo4j.ogm.annotation.*;


@RelationshipEntity(type = "Has_Domian_Tag")
public class DaminHasTag {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private DomainEvent domainEvent;

    public DomainEvent getDomainEvent() {
		return domainEvent;
	}

	public void setDomainEvent(DomainEvent domainEvent) {
		this.domainEvent = domainEvent;
	}

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


}
