package movies.spring.data.neo4j.services;

import java.util.*;

import movies.spring.data.neo4j.data.EmployeeData;
import movies.spring.data.neo4j.data.KeywordsCoefficientdata;
import movies.spring.data.neo4j.data.KeywordsEmployeedata;
import movies.spring.data.neo4j.data.TagData;
import movies.spring.data.neo4j.digital.domain.Employee;
import movies.spring.data.neo4j.digital.domain.Tag;
import movies.spring.data.neo4j.digital.domain.relationship.BelonTo;
import movies.spring.data.neo4j.repositories.EmployeeRepository;
import movies.spring.data.neo4j.repositories.TagsRepository;
import movies.spring.data.neo4j.util.BigDecimalUtil;
import movies.spring.data.neo4j.util.readFileUtile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TagService {

    private final static Logger LOG = LoggerFactory.getLogger(TagService.class);

	private final TagsRepository tagsRepository;
	private final EmployeeRepository employeeRepository;
	public TagService(TagsRepository tagsRepository,EmployeeRepository employeeRepository) {
		this.tagsRepository = tagsRepository;
		this.employeeRepository = employeeRepository;
	}
	
	public KeywordsCoefficientdata getKeywordsCoefficientdataByKeyword(String keyWords)
	{
		KeywordsCoefficientdata keywordsCoefficientdata = new KeywordsCoefficientdata();
		TagData tagData =new TagData();
		tagData.setTitle(keyWords);
		keywordsCoefficientdata.setTagData(tagData);
		getPublishActive(keywordsCoefficientdata);
		getAttendActive(keywordsCoefficientdata);
		getCommentActive(keywordsCoefficientdata);
		createPublishActive(keywordsCoefficientdata);
		getComplexAttendScale(keywordsCoefficientdata);
		return keywordsCoefficientdata;
	}
	
	public void getComplexAttendScale(KeywordsCoefficientdata keywordsCoefficientdata)
	{
		   if(!CollectionUtils.isEmpty(keywordsCoefficientdata.getKeywordsEmployeedatas()))
		   {
			   for(KeywordsEmployeedata keywordsEmployeedata : keywordsCoefficientdata.getKeywordsEmployeedatas())
			   {
				   if(BigDecimalUtil.add(keywordsEmployeedata.getPublishActive(),keywordsEmployeedata.getAttendActive()) == 0)
				   {
					   keywordsEmployeedata.setCommentAttendScale(2);
				   }
				   else
				   {
					   double commentAttendScale =BigDecimalUtil.div(
							   keywordsEmployeedata.getCommentActive(),
							   BigDecimalUtil.add(keywordsEmployeedata.getPublishActive(),keywordsEmployeedata.getAttendActive()),
							   2);
					   keywordsEmployeedata.setCommentAttendScale(commentAttendScale);
				   }
				   
				   double totalActive = BigDecimalUtil.div(
						   BigDecimalUtil.add(keywordsEmployeedata.getCommentActive(),BigDecimalUtil.add(keywordsEmployeedata.getPublishActive(),keywordsEmployeedata.getAttendActive())),
						   3, 2);
				   keywordsEmployeedata.setTotalActive(totalActive);
			   }
		   }
		
	}
	
	public void getCommentActive(KeywordsCoefficientdata keywordsCoefficientdata)
	{
		   String title = keywordsCoefficientdata.getTagData().getTitle();
		   int allCount = employeeRepository.findAllCommentDomainCountByK(title);
		   keywordsCoefficientdata.setCommentCount(allCount);
		   List<Employee> employees = employeeRepository.findEmloyeeCommentDomainByKeywrods(title);
		   if(!CollectionUtils.isEmpty(employees))
		   {
			   int[] arr = new int[employees.size()];
			   for(int i = 0;i< employees.size();i++)
			   {
				   Employee employee = employees.get(i);
				   if(!CollectionUtils.isEmpty(keywordsCoefficientdata.getKeywordsEmployeedatas()))
				   {
					   for(KeywordsEmployeedata keywordsEmployeedata : keywordsCoefficientdata.getKeywordsEmployeedatas())
					   {
						   if(employee.getId() == keywordsEmployeedata.getEmployeeData().getId())
						   {
							   int employeeCount = employeeRepository.findEmloyeeCommentDomainCountByKE(title, employee.getId());
							   keywordsEmployeedata.setCommentCount(employeeCount);
							   arr[i]=employeeCount;
//							   double commentActive = BigDecimalUtil.deciMal(employeeCount, keywordsCoefficientdata.getCommentCount());
//							   keywordsEmployeedata.setCommentActive(commentActive);
						   }
						   else
						   {
							   createNewEmpoyee4CA(employee,keywordsCoefficientdata);
							   break;
						   }
					   }
				   }
				   else
				   {
					   createNewEmpoyee4CA(employee,keywordsCoefficientdata);
				   }
			   } 
			   Arrays.sort(arr);
			   keywordsCoefficientdata.setCommentArray(arr);
		   }
	}
	
	   private void createNewEmpoyee4CA(Employee employee,KeywordsCoefficientdata keywordsCoefficientdata)
	   {
		   EmployeeData newEmployeeData = new EmployeeData();
		   newEmployeeData.setId(employee.getId());
		   newEmployeeData.setName(employee.getName());
		   newEmployeeData.setObjId(employee.getObjId());
		   KeywordsEmployeedata newKeywordsEmployeedata = new KeywordsEmployeedata();
		   int employeeCount = employeeRepository.findEmloyeeCommentDomainCountByKE(keywordsCoefficientdata.getTagData().getTitle(), employee.getId());
		   newKeywordsEmployeedata.setEmployeeData(newEmployeeData);
		   newKeywordsEmployeedata.setCommentCount(employeeCount);
//		   double commentActive = BigDecimalUtil.deciMal(employeeCount, keywordsCoefficientdata.getCommentCount());
//		   newKeywordsEmployeedata.setCommentActive(commentActive);
		   if(CollectionUtils.isEmpty(keywordsCoefficientdata.getKeywordsEmployeedatas()))
		   {
			   ArrayList<KeywordsEmployeedata> keywordsEmployeedatas = new ArrayList<KeywordsEmployeedata>();
			   keywordsEmployeedatas.add(newKeywordsEmployeedata);
			   keywordsCoefficientdata.setKeywordsEmployeedatas(keywordsEmployeedatas);
		   }
		   else
		   {
			   keywordsCoefficientdata.getKeywordsEmployeedatas().add(newKeywordsEmployeedata);
		   }
	   } 
	   
   public void getAttendActive(KeywordsCoefficientdata keywordsCoefficientdata)
   {
	   String title = keywordsCoefficientdata.getTagData().getTitle();
	   int allCount = employeeRepository.findAllAttendDomainCountByK(title);
	   keywordsCoefficientdata.setAttendCount(allCount);
	   List<Employee> employees = employeeRepository.findEmloyeeAttendDomainByKeywrods(title);
	   if(!CollectionUtils.isEmpty(employees))
	   {
		   int[] arr = new int[employees.size()];
		   for(int i = 0;i< employees.size();i++)
		   {
			   Employee employee = employees.get(i);
			   if(!CollectionUtils.isEmpty(keywordsCoefficientdata.getKeywordsEmployeedatas()))
			   {
				   for(KeywordsEmployeedata keywordsEmployeedata : keywordsCoefficientdata.getKeywordsEmployeedatas())
				   {
					   if(employee.getId() == keywordsEmployeedata.getEmployeeData().getId())
					   {
						   int employeeCount = employeeRepository.findEmloyeeAttendDomainCountByKE(title, employee.getId());
						   keywordsEmployeedata.setAttendCount(employeeCount);
						   arr[i]=employeeCount;
//						   double attendActive = BigDecimalUtil.deciMal(employeeCount, keywordsCoefficientdata.getAttendCount());
//						   keywordsEmployeedata.setAttendActive(attendActive);
					   }
					   else
					   {
						   createNewEmpoyee4AA(employee,keywordsCoefficientdata);
						   break;
					   }
				   }
			   }
			   else
			   {
				   createNewEmpoyee4AA(employee,keywordsCoefficientdata);
			   }
		   } 
		   Arrays.sort(arr);
		   keywordsCoefficientdata.setAttendArray(arr);
	   }
   }
   
   private void createNewEmpoyee4AA(Employee employee,KeywordsCoefficientdata keywordsCoefficientdata)
   {
	   EmployeeData newEmployeeData = new EmployeeData();
	   newEmployeeData.setId(employee.getId());
	   newEmployeeData.setName(employee.getName());
	   newEmployeeData.setObjId(employee.getObjId());
	   KeywordsEmployeedata newKeywordsEmployeedata = new KeywordsEmployeedata();
	   int employeeCount = employeeRepository.findEmloyeeAttendDomainCountByKE(keywordsCoefficientdata.getTagData().getTitle(), employee.getId());
	   newKeywordsEmployeedata.setEmployeeData(newEmployeeData);
	   newKeywordsEmployeedata.setAttendCount(employeeCount);
//	   double attendActive = BigDecimalUtil.deciMal(employeeCount, keywordsCoefficientdata.getAttendCount());
//	   newKeywordsEmployeedata.setAttendActive(attendActive);
	   if(CollectionUtils.isEmpty(keywordsCoefficientdata.getKeywordsEmployeedatas()))
	   {
		   ArrayList<KeywordsEmployeedata> keywordsEmployeedatas = new ArrayList<KeywordsEmployeedata>();
		   keywordsEmployeedatas.add(newKeywordsEmployeedata);
		   keywordsCoefficientdata.setKeywordsEmployeedatas(keywordsEmployeedatas);
	   }
	   else
	   {
		   keywordsCoefficientdata.getKeywordsEmployeedatas().add(newKeywordsEmployeedata);
	   }
   } 
	   
   public void getPublishActive(KeywordsCoefficientdata keywordsCoefficientdata)
   {
	   String title = keywordsCoefficientdata.getTagData().getTitle();
	   int allCount = employeeRepository.findAllPublishDomainCountByK(title);
	   keywordsCoefficientdata.setPublishCount(allCount);
	   List<Employee> employees = employeeRepository.findEmloyeePublishDomainByKeywrods(title);
	   if(!CollectionUtils.isEmpty(employees))
	   {
		   int[] arr = new int[employees.size()];
		   for(int i = 0;i< employees.size();i++)
		   {
			   Employee employee = employees.get(i);
			   int employeeCount = employeeRepository.findEmloyeePublishDomainCountByKE(title, employee.getId());
			   arr[i]=employeeCount;
			   if(!CollectionUtils.isEmpty(keywordsCoefficientdata.getKeywordsEmployeedatas()))
			   {
				   for(KeywordsEmployeedata keywordsEmployeedata : keywordsCoefficientdata.getKeywordsEmployeedatas())
				   {
					   if(employee.getId() == keywordsEmployeedata.getEmployeeData().getId())
					   {
//						   int employeeCount = employeeRepository.findEmloyeePublishDomainCountByKE(title, employee.getId());
//						   arr[i]=employeeCount;
						   keywordsEmployeedata.setPublishCount(employeeCount);
//						   double publishActive = BigDecimalUtil.deciMal(employeeCount, keywordsCoefficientdata.getPublishCount());
//						   keywordsEmployeedata.setPublishActive(publishActive);
					   }
					   else
					   {
						   createNewEmpoyee4PA(employee,keywordsCoefficientdata);
						   break;
					   }
				   }
			   }
			   else
			   {
				   createNewEmpoyee4PA(employee,keywordsCoefficientdata);
			   }
		   }
		   Arrays.sort(arr);
		   keywordsCoefficientdata.setPublishArray(arr);
	   }
   }
   
   private void createPublishActive(KeywordsCoefficientdata keywordsCoefficientdata)
   {
	   if(!CollectionUtils.isEmpty(keywordsCoefficientdata.getKeywordsEmployeedatas()))
	   {
		   for(KeywordsEmployeedata keywordsEmployeedata : keywordsCoefficientdata.getKeywordsEmployeedatas())
		   {
			   int maxP = keywordsCoefficientdata.getPublishArray()[keywordsCoefficientdata.getPublishArray().length-1];
			   int maxA = keywordsCoefficientdata.getAttendArray()[keywordsCoefficientdata.getAttendArray().length-1];
			   int maxC = keywordsCoefficientdata.getCommentArray()[keywordsCoefficientdata.getCommentArray().length-1];
			   int minP = keywordsCoefficientdata.getPublishArray()[0];
			   int minA = keywordsCoefficientdata.getAttendArray()[0];
			   int minC = keywordsCoefficientdata.getCommentArray()[0];
			   int xP = keywordsEmployeedata.getPublishCount();
			   int xA = keywordsEmployeedata.getAttendCount();
			   int xC = keywordsEmployeedata.getCommentCount();
			   if(xP==maxP)
			   {
				   keywordsEmployeedata.setPublishActive(1D);
			   }
			   else if(maxP==minP)
			   {
				   keywordsEmployeedata.setPublishActive(0D); 
			   }
			   else
			   {
				   double active = BigDecimalUtil.deciMal(xP-minP, maxP-minP);
				   keywordsEmployeedata.setPublishActive(active);  
			   }
			   if(xA==maxA)
			   {
				   keywordsEmployeedata.setAttendActive(1D);
			   }
			   else if(maxA==minA)
			   {
				   keywordsEmployeedata.setPublishActive(0D); 
			   }
			   else
			   {
				   double active = BigDecimalUtil.deciMal(xA-minA, maxA-minA);
				   keywordsEmployeedata.setAttendActive(active);  
			   }
			   if(xC==maxC)
			   {
				   keywordsEmployeedata.setCommentActive(1D);
			   }
			   else if(maxC==minC)
			   {
				   keywordsEmployeedata.setPublishActive(0D); 
			   }
			   else
			   {
				   double active = BigDecimalUtil.deciMal(xC-minC, maxC-minC);
				   keywordsEmployeedata.setCommentActive(active);  
			   }
		   }
	   }
   }
   
   private void createNewEmpoyee4PA(Employee employee,KeywordsCoefficientdata keywordsCoefficientdata)
   {
	   EmployeeData newEmployeeData = new EmployeeData();
	   newEmployeeData.setId(employee.getId());
	   newEmployeeData.setName(employee.getName());
	   newEmployeeData.setObjId(employee.getObjId());
	   KeywordsEmployeedata newKeywordsEmployeedata = new KeywordsEmployeedata();
	   int employeeCount = employeeRepository.findEmloyeePublishDomainCountByKE(keywordsCoefficientdata.getTagData().getTitle(), employee.getId());
	   newKeywordsEmployeedata.setEmployeeData(newEmployeeData);
	   newKeywordsEmployeedata.setPublishCount(employeeCount);
//	   double publishActive = BigDecimalUtil.deciMal(employeeCount, keywordsCoefficientdata.getPublishCount());
//	   newKeywordsEmployeedata.setPublishActive(publishActive);
	   if(CollectionUtils.isEmpty(keywordsCoefficientdata.getKeywordsEmployeedatas()))
	   {
		   ArrayList<KeywordsEmployeedata> keywordsEmployeedatas = new ArrayList<KeywordsEmployeedata>();
		   keywordsEmployeedatas.add(newKeywordsEmployeedata);
		   keywordsCoefficientdata.setKeywordsEmployeedatas(keywordsEmployeedatas);
	   }
	   else
	   {
		   keywordsCoefficientdata.getKeywordsEmployeedatas().add(newKeywordsEmployeedata);
	   }
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
