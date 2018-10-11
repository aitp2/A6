package movies.spring.data.neo4j.digital.controller;

import movies.spring.data.neo4j.digital.domain.*;
import movies.spring.data.neo4j.digital.domain.relationship.*;
import movies.spring.data.neo4j.repositories.*;
import movies.spring.data.neo4j.services.MovieService;
import movies.spring.data.neo4j.services.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;

/**
 * @author jianfei.yin
 * @create 2018-08-27 9:38 PM
 **/
@RestController
@RequestMapping("/api")
public class DigitalController {

    @Autowired
    private ClockInRepository clockInRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private SmallInvitationRepository smallInvitationRepository;
    @Autowired
    private SmallObjectiveRepository smallObjectiveRepository;
    @Autowired
    private TagsRepository tagsRepository;
    
	private final TagService tagService;
	@Autowired
    MovieRepository repository;
	public DigitalController(TagService tagService) {
		this.tagService = tagService;
	}
    @GetMapping("/clear")
    public void clear(){
        clockInRepository.deleteAll();
        commentRepository.deleteAll();
        employeeRepository.deleteAll();
        imageRepository.deleteAll();
        smallObjectiveRepository.deleteAll();
        smallInvitationRepository.deleteAll();
        tagsRepository.deleteAll();
    }
    @PostMapping("/tag")
    public void createClockIn(@RequestBody  Tag tag){
        tagsRepository.save(tag);
    }
    @PostMapping("/clock")
    public void createClockIn(@RequestBody  ClockIn clockIn){
        clockInRepository.save(clockIn);
    }
    @PostMapping("/comment")
    public void createComment(@RequestBody  Comment comment){
        commentRepository.save(comment);
    }

    @PostMapping("/employee")
    public void createEmployee(@RequestBody  Employee employee){
        employeeRepository.save(employee);
    }
    @PostMapping("/image")
    public void createImage(@RequestBody  Image image){
        imageRepository.save(image);
    }
    @PostMapping("/smallInvitation")
    public void createSmallInvitation(@RequestBody  SmallInvitation smallInvitation){
        smallInvitationRepository.save(smallInvitation);
    }
    @PostMapping("/smallObjective")
    public void createSmallObjective(@RequestBody  SmallObjective smallObjective){
        smallObjectiveRepository.save(smallObjective);
    }
    @GetMapping("/tag/{title}")
    public String getTagIdByTitle(@PathVariable String title){
    	Tag tag = tagsRepository.findByTitle(title);
    	if(null != tag)
    	{
    		return tag.getObjId();
    	}
    	return null;
    }
    @GetMapping("/sampleTag")
    public void createSampleTag(){
    	Map<String,List<String>> superTags = new HashMap<String,List<String>>();
    	superTags.put("饮料", Arrays.asList(new String[]{"椰肉","椰汁","鲜奶","奶茶","椰子","夏日清","红茶","茶","喝酒","眷村","咖啡","星冰乐","果蔬汁"}));
    	superTags.put("食物", Arrays.asList(new String[]{"饮料","糖","芋头","冰无糖","芋","紫米","早饭","海盐","火锅","焦糖","主食","面","雪糕","炒米粉","果冻","凉粉","啤酒鸭","火龙果","豆腐","香草","小食","鱼蛋","龟苓膏","螃蟹","奶昔","海鲜"}));
    	superTags.put("宠物", Arrays.asList(new String[]{"狗","边牧","猫","鸡","海鸥","云吸宠","天鹅"}));
    	superTags.put("建筑", Arrays.asList(new String[]{"寺庙","庙"}));
    	superTags.put("体育", Arrays.asList(new String[]{"看球","跳伞","篮球","平板","球衣","台球","球衣","健身房","蹦床"}));
    	superTags.put("娱乐", Arrays.asList(new String[]{"跳伞","电视","音乐","逛街","电影","书","露营","旅途","杀人","蹦床"}));
    	superTags.put("音乐", Arrays.asList(new String[]{"二胡","歌曲","歌","乐器","歌单","单曲","首歌"}));
    	 for (Map.Entry<String,List<String>> entry : superTags.entrySet()) {
			for(String subTagTitle: entry.getValue())
			{
				tagService.updateBelonToTag(subTagTitle,entry.getKey());
			}
    	}
    }
    
    @GetMapping("/tag/by/{userId}/{objId}")
    public void updateHasTagBy(@PathVariable String userId,@PathVariable String objId){
        Employee employee = employeeRepository.findByObjId(userId);
        if(employee==null){
            return;
        }
        List<HasTag> hasTags = employee.getHasTags()==null?new ArrayList<>():employee.getHasTags();
        Tag tag = tagsRepository.findByObjId(objId);
        HasTag hasTag = new HasTag();
        hasTag.setTag(tag);
        hasTag.setEmployee(employee);
        hasTags.add(hasTag);
        employee.setHasTags(hasTags);
        employeeRepository.save(employee);
    }
    @GetMapping("/tag/belonTo/{subTag}/{superTag}")
    public void updateBelonToTag(@PathVariable String subTag,@PathVariable String superTag){
    	Tag subTagNode = tagsRepository.findByTitle(subTag);
        if(subTagNode==null){
            return;
        }
        List<BelonTo> belonTos = subTagNode.getBelonTo()==null?new ArrayList<>():subTagNode.getBelonTo();
        Tag superTagNode = tagsRepository.findByTitle(superTag);
        BelonTo belonTo = new BelonTo();
        belonTo.setSubTag(subTagNode);
        belonTo.setSuperTag(superTagNode);
        belonTos.add(belonTo);
        subTagNode.setBelonTo(belonTos);
        tagsRepository.save(subTagNode);
    }
    @GetMapping("/comment/by/{userId}/{objId}")
    public void updateCommentBy(@PathVariable String userId,@PathVariable String objId){
        Employee employee = employeeRepository.findByObjId(userId);
        if(employee==null){
            return;
        }
        List<CommentedBy> commentedBys = employee.getCommentedBys()==null?new ArrayList<>():employee.getCommentedBys();
        Comment comment = commentRepository.findByObjId(objId);
        CommentedBy commentedBy = new CommentedBy();
        commentedBy.setComment(comment);
        commentedBy.setEmployee(employee);
        commentedBys.add(commentedBy);
        employee.setCommentedBys(commentedBys);
        employeeRepository.save(employee);
    }
    @GetMapping("/comment/invitation/{eventId}/{objId}")
    public void updateInvitationComment(@PathVariable String eventId,@PathVariable String objId){
        SmallInvitation smallInvitation = smallInvitationRepository.findByObjId(eventId);
        if(smallInvitation == null){
            return;
        }
        List<Commented> commenteds = smallInvitation.getCommented()==null?new ArrayList<>():smallInvitation.getCommented();
        Comment comment = commentRepository.findByObjId(objId);
        Commented commented = new Commented();
        commented.setComment(comment);
        commented.setDomainEvent(smallInvitation);
        commenteds.add(commented);
        smallInvitation.setCommented(commenteds);
        smallInvitationRepository.save(smallInvitation);
    }
    @GetMapping("/comment/objective/{eventId}/{objId}")
    public void updateObjectiveComment(@PathVariable String eventId,@PathVariable String objId){
        SmallObjective smallObjective = smallObjectiveRepository.findByObjId(eventId);
        if(smallObjective == null){
            return;
        }
        List<Commented> commenteds = smallObjective.getCommented()==null?new ArrayList<>():smallObjective.getCommented();
        Comment comment = commentRepository.findByObjId(objId);
        Commented commented = new Commented();
        commented.setComment(comment);
        commented.setDomainEvent(smallObjective);
        commenteds.add(commented);
        smallObjective.setCommented(commenteds);
        smallObjectiveRepository.save(smallObjective);
    }
    @GetMapping("/attended/invitation/{userId}/{objId}")
    public void updateInvitationAttended(@PathVariable String userId,@PathVariable String objId){
        Employee employee = employeeRepository.findByObjId(userId);
        if(employee==null){
            return;
        }
        List<Attended> attendeds = employee.getAttendeds()==null ? new ArrayList<>():employee.getAttendeds();
        SmallInvitation smallInvitation = smallInvitationRepository.findByObjId(objId);
        Attended attended = new Attended();
        attended.setEmployee(employee);
        attended.setDomainEvent(smallInvitation);
        attendeds.add(attended);
        employee.setAttendeds(attendeds);
        employeeRepository.save(employee);
    }
    @GetMapping("/attended/objective/{userId}/{objId}")
    public void updateObjectiveAttended(@PathVariable String userId,@PathVariable String objId){
        Employee employee = employeeRepository.findByObjId(userId);
        if(employee==null){
            return;
        }
        List<Attended> attendeds = employee.getAttendeds()==null ? new ArrayList<>():employee.getAttendeds();
        SmallObjective smallObjective = smallObjectiveRepository.findByObjId(objId);
        Attended attended = new Attended();
        attended.setEmployee(employee);
        attended.setDomainEvent(smallObjective);
        attendeds.add(attended);
        employee.setAttendeds(attendeds);
        employeeRepository.save(employee);
    }
    @GetMapping("/launch/{userId}/{objId}")
    public void updateLaunched(@PathVariable String userId,@PathVariable String objId){
        Employee employee = employeeRepository.findByObjId(userId);
        if(employee==null){
            return;
        }
        List<Activity> launched = employee.getLaunchs()==null ? new ArrayList<>() : employee.getLaunchs();
        SmallInvitation smallInvitation = smallInvitationRepository.findByObjId(objId);
        Activity activity=new Activity();
        activity.setSmallInvitation(smallInvitation);
        activity.setEmployee(employee);
        launched.add(activity);
        employee.setLaunchs(launched);
        employeeRepository.save(employee);
    }
    @GetMapping("/edrect/{userId}/{objId}")
    public void updateErect(@PathVariable String userId,@PathVariable String objId){
        Employee employee = employeeRepository.findByObjId(userId);
        if(employee==null){
            return;
        }
        List<Flag> erected = employee.getErected()==null ? new ArrayList<>() : employee.getErected();
        SmallObjective smallObjective = smallObjectiveRepository.findByObjId(objId);
        Flag flag=new Flag();
        flag.setSmallObjective(smallObjective);
        flag.setEmployee(employee);
        erected.add(flag);
        employee.setErected(erected);
        employeeRepository.save(employee);
    }
    @GetMapping("/similar/{user1}/{user2}/{sim}")
    public void similarTo(@PathVariable String user1, @PathVariable String user2, @PathVariable BigDecimal sim){
        List<Employee> similars = employeeRepository.findSimilar(user1);
        Employee similar = null;
        if(similars!=null){
            for(Employee employee:similars){
                if(employee.getObjId().equals(user1)){
                    similar= employee;
                    break;
                }
            }
        }
        if(similar == null){
            similar = employeeRepository.findByObjId(user1);
        }
        List<Similarity> similarities = similar.getSimilarities()==null?new ArrayList<>():similar.getSimilarities();
        boolean hadRelationShip = false;
        for (Similarity similarity:similarities){
            if(similarity.getEmployee1().getObjId().equals(user2)||similarity.getEmployee2().getObjId().equals(user2)){
                hadRelationShip =true;
                break;
            }
        }
        if(!hadRelationShip){
            Employee employee = employeeRepository.findByObjId(user2);
            Similarity similarity = new Similarity();
            similarity.setEmployee1(similar);
            similarity.setEmployee2(employee);
            similarity.setSimilarity(sim);
            Collections.sort(similarities, Comparator.comparing(Similarity::getSimilarity));
            Collections.reverse(similarities);
            if(similarities.size()>=5){
                if(similarity.getSimilarity().compareTo(similarities.get(4).getSimilarity())>0){
                    similarities.remove(4);
                    similarities.add(4,similarity);
                    similarities = similarities.subList(0,4);
                    similar.setSimilarities(similarities);
                    employeeRepository.save(similar);
                }
            }else {
                similarities.add(similarity);
                similar.setSimilarities(similarities);
                employeeRepository.save(similar);
            }

        }
    }
}
