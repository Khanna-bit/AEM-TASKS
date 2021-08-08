package readit.core.models;
import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
@Model(adaptables = SlingHttpServletRequest.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DataModel{
	@ScriptVariable
	private PageManager pageManager;
	
	private String title;
	
	private String nav;
	
    @ValueMapValue
	private String path;
	
	@PostConstruct
	public void getData() {
			 Page page =pageManager.getPage(path);
	         ValueMap PathPageProperties = page.getProperties();
	         title=PathPageProperties.get("jcr:title",String.class);
	         nav=PathPageProperties.get("navTitle",String.class);	 
	}
	public String getTitle() {
		return title;
	}
	public String getNav() {
		return nav;
	}
	public String getPath() {
		return path;
	}
}