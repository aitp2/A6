package movies.spring.data.neo4j.digital.domain.relationship;

import movies.spring.data.neo4j.digital.domain.Tag;

import org.neo4j.ogm.annotation.*;


@RelationshipEntity(type = "BELON_TO")
public class BelonTo {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Tag subTag;

    @EndNode
    private Tag superTag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tag getSubTag() {
		return subTag;
	}

	public void setSubTag(Tag subTag) {
		this.subTag = subTag;
	}

	public Tag getSuperTag() {
		return superTag;
	}

	public void setSuperTag(Tag superTag) {
		this.superTag = superTag;
	}


}
