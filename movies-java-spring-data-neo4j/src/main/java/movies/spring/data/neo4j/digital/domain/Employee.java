package movies.spring.data.neo4j.digital.domain;

import movies.spring.data.neo4j.digital.domain.relationship.*;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

/**
 * @author jianfei.yin
 * @create 2018-08-27 5:48 PM
 **/
@NodeEntity
public class Employee {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String avatar;

    private String gender;

    private String objId;

    @Relationship(type = "LAUNCHED_BY",direction = "INCOMING")
    private List<Activity> launchs;
    @Relationship(type = "ERECTED_BY",direction = "INCOMING")
    private List<Flag> erected;
    @Relationship(type = "COMMENT_BY",direction = "INCOMING")
    private List<CommentedBy> commentedBys;
    @Relationship(type = "ATTENDED",direction = "INCOMING")
    private List<Attended> attendeds;
    @Relationship(type = "SIMILAR_TO", direction ="UNDIRECTED")
    private List<Similarity> similarities;
    @Relationship(type = "TAGET_TAG", direction ="UNDIRECTED")
    private List<HasTag> hasTags;
    
    public List<HasTag> getHasTags() {
		return hasTags;
	}

	public void setHasTags(List<HasTag> hasTags) {
		this.hasTags = hasTags;
	}

	public List<Activity> getLaunchs() {
        return launchs;
    }

    public void setLaunchs(List<Activity> launchs) {
        this.launchs = launchs;
    }

    public List<Flag> getErected() {
        return erected;
    }

    public void setErected(List<Flag> erected) {
        this.erected = erected;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public List<Attended> getAttendeds() {
        return attendeds;
    }

    public void setAttendeds(List<Attended> attendeds) {
        this.attendeds = attendeds;
    }

    public List<CommentedBy> getCommentedBys() {
        return commentedBys;
    }

    public void setCommentedBys(List<CommentedBy> commentedBys) {
        this.commentedBys = commentedBys;
    }



    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender='" + gender + '\'' +
                ", objId='" + objId + '\'' +
                ", launchs=" + launchs +
                ", erected=" + erected +
                ", commentedBys=" + commentedBys +
                ", attendeds=" + attendeds +
                '}';
    }

    public List<Similarity> getSimilarities() {
        return similarities;
    }

    public void setSimilarities(List<Similarity> similarities) {
        this.similarities = similarities;
    }
}
