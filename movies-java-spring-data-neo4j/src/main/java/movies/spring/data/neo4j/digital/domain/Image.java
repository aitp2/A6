package movies.spring.data.neo4j.digital.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * @author jianfei.yin
 * @create 2018-08-27 9:27 PM
 **/
@NodeEntity
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    private String path;
    private String objId;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }
}
