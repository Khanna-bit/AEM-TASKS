package readit.core.models;
import java.util.Date;

import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.Template;

@Model(adaptables = SlingHttpServletRequest.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PropertyModel {
	@ScriptVariable
	private PageManager pageManager;	
    @ValueMapValue
	private String path;
	@PostConstruct
	public void getData() {
			 Page page =pageManager.getPage(path);
	         ValueMap prop = page.getProperties();  
	         page.getLastModifiedBy();     
	         page.getTemplate();
	}
	public PageManager getPageManager() {
		return pageManager;
	}
	public String getPath() {
		return path;
	}
	public Date getLastModified() {
		return getLastModified();
	}
	public String getLastModifiedBy() {
		return getLastModifiedBy();
	}
}
