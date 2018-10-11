package movies.spring.data.neo4j.repositories;

import movies.spring.data.neo4j.digital.domain.ClockIn;
import movies.spring.data.neo4j.digital.domain.Image;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "Image", path = "Image")
public interface ImageRepository extends Neo4jRepository<Image, Long> {
    Image findByObjId(String objId);
}