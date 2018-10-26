package movies.spring.data.neo4j.digital.domain;

import java.util.List;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import movies.spring.data.neo4j.digital.domain.relationship.BelonTo;
import movies.spring.data.neo4j.digital.domain.relationship.HasTag;

@NodeEntity
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String objId;
    private String emotion;
    private String level;
    
    @Relationship(type = "BELON_TO", direction ="UNDIRECTED")
    private List<BelonTo> belonTo;
    
    public List<BelonTo> getBelonTo() {
		return belonTo;
	}

	public void setBelonTo(List<BelonTo> belonTo) {
		this.belonTo = belonTo;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmotion() {
		return emotion;
	}

	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
}
