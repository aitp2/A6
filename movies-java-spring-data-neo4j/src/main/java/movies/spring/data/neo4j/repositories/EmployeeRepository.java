package movies.spring.data.neo4j.repositories;

import movies.spring.data.neo4j.digital.domain.Employee;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "Employee", path = "Employee")
public interface EmployeeRepository extends Neo4jRepository<Employee, Long> {
    Employee findByObjId(String objId);
    Employee findByName(String name);
    
    @Query("MATCH (n:Employee) -[r:SIMILAR_TO]- (d:Employee) where n.objId={objId} RETURN n,r,d limit 10")
    List<Employee> findSimilar(@Param("objId")String objId);
    
    @Query("MATCH (n:Tag)-[h:Has_Domian_Tag]-(d:DomainEvent)-[l:LAUNCHED_BY]-(e:Employee) where n.title={keywords} RETURN e")
    List<Employee> findEmloyeePublishDomainByKeywrods(@Param("keywords")String keywords);
    	
    @Query("MATCH (n:Tag)-[h:Has_Domian_Tag]-(d:DomainEvent)-[l:LAUNCHED_BY]-(e:Employee) where n.title={keywords} and ID(e)={employeeID} RETURN count(e)")
    int findEmloyeePublishDomainCountByKE(@Param("keywords")String keywords,@Param("employeeID")Long employeeID);
    
    @Query("MATCH (n:Tag)-[h:Has_Domian_Tag]-(d:DomainEvent)-[l:LAUNCHED_BY]-(e:Employee) where n.title={keywords} RETURN count(e)")
    int findAllPublishDomainCountByK(@Param("keywords")String keywordsD);
    
    @Query("MATCH (n:Tag)-[h:Has_Domian_Tag]-(d:DomainEvent)-[a:ATTENDED]-(e:Employee) where n.title={keywords} RETURN e")
    List<Employee> findEmloyeeAttendDomainByKeywrods(@Param("keywords")String keywords);
    
    @Query("MATCH (n:Tag)-[h:Has_Domian_Tag]-(d:DomainEvent)-[a:ATTENDED]-(e:Employee) where n.title={keywords} and ID(e)={employeeID} RETURN count(e)")
    int findEmloyeeAttendDomainCountByKE(@Param("keywords")String keywords,@Param("employeeID")Long employeeID);
    
    @Query("MATCH (n:Tag)-[h:Has_Domian_Tag]-(d:DomainEvent)-[a:ATTENDED]-(e:Employee) where n.title={keywords} RETURN count(e)")
    int findAllAttendDomainCountByK(@Param("keywords")String keywordsD);
    
    @Query("MATCH (n:Tag)-[h:Has_Domian_Tag]-(d:DomainEvent)-[cb:COMMENTED]-(c:Comment)-[hc:COMMENT_BY]-(e:Employee) where n.title={keywords} RETURN e")
    List<Employee> findEmloyeeCommentDomainByKeywrods(@Param("keywords")String keywords);
    
    @Query("MATCH (n:Tag)-[h:Has_Domian_Tag]-(d:DomainEvent)-[cb:COMMENTED]-(c:Comment)-[hc:COMMENT_BY]-(e:Employee) where n.title={keywords} and ID(e)={employeeID} RETURN count(e)")
    int findEmloyeeCommentDomainCountByKE(@Param("keywords")String keywords,@Param("employeeID")Long employeeID);
    
    @Query("MATCH (n:Tag)-[h:Has_Domian_Tag]-(d:DomainEvent)-[cb:COMMENTED]-(c:Comment)-[hc:COMMENT_BY]-(e:Employee) where n.title={keywords} RETURN count(e)")
    int findAllCommentDomainCountByK(@Param("keywords")String keywordsD);
}