package readit.core.services;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.LoggerFactory;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
@Component(service=ListService.class,immediate = true)
@Designate(ocd=List.class)
public class ListService{
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(ListService.class);
	private String path;
	public List configuration;
	@Activate
	public void activate(List configuration) {	
		log.info("hiiii");
		path = configuration.getWay();
		this.configuration = configuration;
	}
	public String getPath() {
		return path;
	}
	@Reference
	private ResourceResolverFactory factory;
	public List li;
	public String getlist() {
		log.info("hiiii");
		Map<String, Object> props = new HashMap<>();
		props.put(ResourceResolverFactory.SUBSERVICE,"writeservice");
		ResourceResolver rr = null;
		JSONArray lp = new JSONArray();
		log.debug("lp {}",lp.toString());
		try {
			rr = factory.getResourceResolver(props);
			PageManager pagemngr =rr.adaptTo(PageManager.class);
			log.debug("page manager {}",pagemngr.toString());
			Page page = pagemngr.getPage(configuration.getWay());
			log.debug("page path {}",page.getPath());
			Iterator<Page> listpage=page.listChildren();
			while(listpage.hasNext()) {
				Page page1 = (Page) listpage.next();
				JSONObject obj=new JSONObject();
				obj.put("title",page1.getTitle());
				obj.put("description",page1.getDescription());
				lp.put(obj);
			}
		}catch(LoginException e) {
			e.printStackTrace();
		} catch(JSONException e) {
			e.printStackTrace();
			log.debug("errorlog {}",e.getMessage());
		}
		return lp.toString();
	}
}

