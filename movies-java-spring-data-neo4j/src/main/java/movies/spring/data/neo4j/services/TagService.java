package movies.spring.data.neo4j.services;

import java.util.*;

import movies.spring.data.neo4j.digital.domain.Tag;
import movies.spring.data.neo4j.digital.domain.relationship.BelonTo;
import movies.spring.data.neo4j.repositories.TagsRepository;
import movies.spring.data.neo4j.util.readFileUtile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagService {

    private final static Logger LOG = LoggerFactory.getLogger(TagService.class);

	private final TagsRepository tagsRepository;
	public TagService(TagsRepository tagsRepository) {
		this.tagsRepository = tagsRepository;
	}

   @Transactional
   public void updateBelonToTag(String subTagTitle,String superTagTitle){
    	Tag subTagNode = tagsRepository.findByTitle(subTagTitle);
        if(subTagNode==null){
        	subTagNode = new Tag();
        	subTagNode.setObjId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        	subTagNode.setTitle(subTagTitle);
        	tagsRepository.save(subTagNode);
        }
        List<BelonTo> belonTos = subTagNode.getBelonTo()==null?new ArrayList<>():subTagNode.getBelonTo();
        Tag superTagNode = tagsRepository.findByTitle(superTagTitle);
        if(superTagNode==null){
        	superTagNode = new Tag();
        	superTagNode.setObjId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        	superTagNode.setTitle(superTagTitle);
        	tagsRepository.save(superTagNode);
        }
        BelonTo belonTo = new BelonTo();
        belonTo.setSubTag(subTagNode);
        belonTo.setSuperTag(superTagNode);
        belonTos.add(belonTo);
        subTagNode.setBelonTo(belonTos);
        tagsRepository.save(subTagNode);
    }
   
   public Map<String,List<String>> getDictionaryTag(String filrPath)
   {
	   return readFileUtile.readLine(filrPath);
   }
}
