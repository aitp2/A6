package movies.spring.data.neo4j.repositories;

import movies.spring.data.neo4j.digital.domain.Tag;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "Tag", path = "Tag")
public interface TagsRepository extends Neo4jRepository<Tag, Long> {
	Tag findByObjId(String objId);
	Tag findByTitle(String title);
}