package movies.spring.data.neo4j.digital.domain;

import movies.spring.data.neo4j.digital.domain.relationship.Commented;
import movies.spring.data.neo4j.digital.domain.relationship.DaminHasTag;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.time.Instant;
import java.util.List;

/**
 * @author jianfei.yin
 * @create 2018-08-27 9:26 PM
 **/
@NodeEntity
public class SmallObjective extends DomainEvent{
    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "COMMENTED",direction = "INCOMING")
    private List<Commented> commented;

    private String title;

    private String description;

    private Integer readingCount;

    private Instant start;

    private Instant end;
    private String objId;
    
    @Relationship(type = "Has_Domian_Tag",direction = "INCOMING")
    private List<DaminHasTag> daminHasTag;
    
    public List<DaminHasTag> getDaminHasTag() {
		return daminHasTag;
	}

	public void setDaminHasTag(List<DaminHasTag> daminHasTag) {
		this.daminHasTag = daminHasTag;
	}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReadingCount() {
        return readingCount;
    }

    public void setReadingCount(Integer readingCount) {
        this.readingCount = readingCount;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public List<Commented> getCommented() {
        return commented;
    }

    public void setCommented(List<Commented> commented) {
        this.commented = commented;
    }
}
