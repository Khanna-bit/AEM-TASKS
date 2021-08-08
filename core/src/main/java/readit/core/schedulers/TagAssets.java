package readit.core.schedulers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
@Component(
		service=Runnable.class,
		property={"scheduler.expression=*/15 * * * * ?"}
		)
public class TagAssets implements Runnable{

	@Reference
	QueryBuilder queryBuilder;

	@Reference
	private ResourceResolverFactory rrf;

	@Override
	public void run() {
		Map<String,String> predicates = new HashMap<>();
		predicates.put("type","dam:asset");
		predicates.put("path","/content/dam/destaco");
		predicates.put("orderBy","@jcr:content/jcr:modified");
		predicates.put("orderby.sort","desc");
		predicates.put("group.1_group.property","jcr:content/metadata/dc:keywords");
		predicates.put("group.1_group.property.operation","exists");
		predicates.put("group.2_group.property","jcr:content/metadata/dc:acceslevels");
		predicates.put("group.2_group.property.operation","exists");
		predicates.put("group.3_group.property","jcr:content/metadata/dc:categories");
		predicates.put("group.3_group.property.operation","exists");
		predicates.put("group.p.and","true");

		Map<String,Object> rf = new HashMap<>();

		rf.put(ResourceResolverFactory.SUBSERVICE, "writeservice");
		ResourceResolver rr = null;
		try {
			rr=rrf.getServiceResourceResolver(rf);
		} catch (org.apache.sling.api.resource.LoginException e) {
			e.printStackTrace();
		}
		com.day.cq.search.Query query = (com.day.cq.search.Query) queryBuilder.createQuery(PredicateGroup.create(predicates), (Session) rr.adaptTo(Session.class));		
		SearchResult results = query.getResult();
		List<Hit> hits=results.getHits();
		Iterator<Hit> itr = hits.iterator();
		List<String> paths =new ArrayList<>();
		while(itr.hasNext()) {
			Hit hit = (Hit) itr.next();
			try {
				paths.add(hit.getPath());
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		}
		Resource resource = rr.getResource("/content/assets");
		if(resource==null) {
			Map<String,Object> nodeMap=new HashMap<>();
			nodeMap.put("paths", paths.toArray());
			Resource r1=rr.getResource("/content");
			try {
				rr.create(r1, "assets", nodeMap);
			} catch (PersistenceException e) {
				e.printStackTrace();
			}
		} else {
			ModifiableValueMap mvm = resource.adaptTo(ModifiableValueMap.class);
			mvm.put("assets", paths.toArray());
		}
		try {
			rr.commit();
			rr.close();
		} catch (PersistenceException e) {
			e.printStackTrace();
		}

	}
	
	
	
	
	
}
