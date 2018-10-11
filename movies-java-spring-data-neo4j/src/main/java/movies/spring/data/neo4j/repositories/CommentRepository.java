package movies.spring.data.neo4j.repositories;

import movies.spring.data.neo4j.digital.domain.ClockIn;
import movies.spring.data.neo4j.digital.domain.Comment;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "CommentRepository", path = "CommentRepository")
public interface CommentRepository extends Neo4jRepository<Comment, Long> {
    Comment findByObjId(String objId);
}