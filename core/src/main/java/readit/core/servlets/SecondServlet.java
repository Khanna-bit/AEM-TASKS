package readit.core.servlets;
import java.io.IOException;
import java.util.Iterator;
import javax.annotation.Resource;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.http.impl.client.cache.FileResourceFactory;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import com.adobe.xfa.ut.Resolver;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
@Component(service=Servlet.class,
          property = {
        		  "sling.servlet.resourceTypes=/readit/components/structure/page",
        		  "sling.servlet.methods=POST",
        		  "sling.servlet.selectors=articles",
        		  "sling.servlet.extension=json",
        		  "sling.servlet.methods=GET"}
		)
public class SecondServlet extends SlingAllMethodsServlet{	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String articlePath ="/content/readit/en/articles";
		ResourceResolver resolver =request.getResourceResolver();
		PageManager pagemanager=resolver.adaptTo(PageManager.class);
		Page pagepath = pagemanager.getPage(articlePath);
		if(pagepath!=null) {
			Iterator<Page> childpages =pagepath.listChildren();
			while (childpages.hasNext()) {
				Page page = (Page) childpages.next();
				JSONObject pageobj = new JSONObject();
				try {
					pageobj.put("title",page.getPageTitle());
					pageobj.put("description",page.getDescription());
					pageobj.put("path",page.getPath()+".html");
                   org.apache.sling.api.resource.Resource imageResource = resolver.getResource(page.getPath()+"/jcr:content/image");
                   pageobj.put("thumbnail",imageResource.getValueMap().get("fileReference",String.class));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}		
		}
		response.setContentType("application/json");
		JSONObject obj= new JSONObject();
		try {
			obj.put("title","data from the servlet");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().println(obj.toString());	
	}
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
	    JSONObject obj = new JSONObject();
	    try {
			obj.put("title","this is from resource Servlet");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    response.getWriter().print(obj.toString());	    
	}	
}