package movies.spring.data.neo4j.domain;

import org.hibernate.validator.constraints.UniqueElements;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.stereotype.Indexed;

/**
 * @author jianfei.yin
 * @create 2018-08-10 1:38 PM
 **/
@NodeEntity
public class Equipment {
    @Id
    @GeneratedValue
    private Long id;
    @Index(unique = true)
    private String name;

    private Integer mentioned;

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

    public Integer getMentioned() {
        return mentioned;
    }

    public void setMentioned(Integer mentioned) {
        this.mentioned = mentioned;
    }

    public void addMentioned(){
        this.mentioned = this.mentioned+1;
    }
}
