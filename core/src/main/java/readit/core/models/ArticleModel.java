package readit.core.models;
import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;

import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class)

public class ArticleModel {
    
	@ValueMapValue
	private String articlelink; 
	
	@ValueMapValue
	private String image;
	
	@ValueMapValue
	private String pubdate;
	
	@ValueMapValue
	private String readmins;
	
	@ValueMapValue
	private String subheading;
	
	@ValueMapValue
	private String title;

	
	@PostConstruct
	public void init() {
		if(articlelink!=null && articlelink.startsWith("/content/readit")) {
		}
	}
	
	public String getArticlelink() {
		return articlelink;
	}

	public String getImage() {
		return image;
	}

	public String getPubdate() {
		return pubdate;
	}

	public String getReadmins() {
		return readmins;
	}

	public String getSubheading() {
		return subheading;
	}

	public String getTitle() {
		return title;
	}	
}
