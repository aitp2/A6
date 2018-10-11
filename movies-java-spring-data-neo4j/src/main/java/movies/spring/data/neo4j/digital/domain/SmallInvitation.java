package movies.spring.data.neo4j.digital.domain;

import movies.spring.data.neo4j.digital.domain.relationship.Commented;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author jianfei.yin
 * @create 2018-08-27 5:50 PM
 **/
@NodeEntity
public class SmallInvitation extends DomainEvent{
    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "COMMENTED",direction = "INCOMING")
    private List<Commented> commented;

    private String title;

    private BigDecimal budget;

    private String address;

    private String description;

    private String comment;

    private Integer readingCount;
    private String objId;

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

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getReadingCount() {
        return readingCount;
    }

    public void setReadingCount(Integer readingCount) {
        this.readingCount = readingCount;
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
