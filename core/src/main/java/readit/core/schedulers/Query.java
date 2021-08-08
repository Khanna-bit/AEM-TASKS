package readit.core.schedulers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.LoggerFactory;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
@Component(service=Runnable.class,
property= {"scheduler.expression=0/5 0 0 ? * * *"},
immediate = true
		)
public class Query implements Runnable{
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(Query.class);
	@Reference
	QueryBuilder queryBuilder;
	@Reference
	ResourceResolverFactory resourceResolver;
	private ResourceResolver resourceResolver2;
	@Override
	public void run() {
		log.info("schedular is executing for every 5sec");
		Map<String, Object> propsMap =new HashMap<>();
		Map<String, Object> resMap =new HashMap<String, Object>();
		propsMap.put("type", "dam:Asset");
		propsMap.put("path", "/content/dam/destaco");
		propsMap.put("group.1_group.property", "jcr:content/metadata/dc:keywords");
		propsMap.put("group.1_group.property.operation", "exists");
		propsMap.put("group.2_group.property", "jcr:content/metadata/dc:acceslevels");
		propsMap.put("group.2_group.property.operation", "exists");
		propsMap.put("group.3_group.property", "jcr:content/metadata/dc:categories");
		propsMap.put("group.3_group.property.operation", "exists");
		propsMap.put("group.p.and", "true");
		propsMap.put(ResourceResolverFactory.SUBSERVICE,"writeservice");
		resourceResolver2 = null;
		try {
			resourceResolver2=((ResourceResolverFactory) resourceResolver2).getServiceResourceResolver(resMap);
			com.day.cq.search.Query query = queryBuilder.createQuery((Session) PredicateGroup.create(propsMap));
			resourceResolver2.adaptTo(Session.class);
			SearchResult results = query.getResult();
			List<Hit> hits = results.getHits();
			Iterator<Hit> itr = hits.iterator();
			List<String>  paths = new ArrayList<>();
			while(itr.hasNext()) {
				Hit hit = (Hit) itr.next();
				paths.add(hit.getPath());
			}
			Resource resource = resourceResolver2.getResource("/content/asgg");
			if(resource==null) {
				Map<String, Object> nodeMap=new HashMap<>();
				nodeMap.put("paths", paths.toArray());
				Resource resource1= resourceResolver2.getResource("/content");
				resourceResolver2.create(resource1, "asgg", nodeMap);
			} else {
				ModifiableValueMap modify = resource.adaptTo(ModifiableValueMap.class);
				modify.put("asgg", paths.toArray());
			}
			resourceResolver2.commit();
			resourceResolver2.close();
		} catch (PersistenceException | RepositoryException e) {
			log.error(e.getMessage());
		} catch (org.apache.sling.api.resource.LoginException e) {
			e.printStackTrace();
		}
	}
}
