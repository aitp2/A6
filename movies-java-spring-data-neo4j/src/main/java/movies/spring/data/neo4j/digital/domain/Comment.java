package movies.spring.data.neo4j.digital.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import movies.spring.data.neo4j.digital.domain.relationship.CommentHasTag;
import movies.spring.data.neo4j.digital.domain.relationship.DaminHasTag;

import java.time.Instant;
import java.util.List;

/**
 * @author jianfei.yin
 * @create 2018-08-27 9:24 PM
 **/
@NodeEntity
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    private String content;

    private String objId;

    @Relationship(type = "Has_Comment_Tag",direction = "INCOMING")
    private List<CommentHasTag> commentHasTag;
    
    public List<CommentHasTag> getCommentHasTag() {
		return commentHasTag;
	}

	public void setCommentHasTag(List<CommentHasTag> commentHasTag) {
		this.commentHasTag = commentHasTag;
	}
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", objId='" + objId + '\'' +
                '}';
    }
}
