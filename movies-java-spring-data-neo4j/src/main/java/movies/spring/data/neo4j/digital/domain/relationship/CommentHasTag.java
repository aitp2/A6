package movies.spring.data.neo4j.digital.domain.relationship;

import movies.spring.data.neo4j.digital.domain.Comment;
import movies.spring.data.neo4j.digital.domain.DomainEvent;
import movies.spring.data.neo4j.digital.domain.Tag;

import org.neo4j.ogm.annotation.*;


@RelationshipEntity(type = "Has_Comment_Tag")
public class CommentHasTag {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Comment comment;

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
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
