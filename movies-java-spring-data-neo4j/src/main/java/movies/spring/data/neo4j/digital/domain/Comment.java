package movies.spring.data.neo4j.digital.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import java.time.Instant;

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
