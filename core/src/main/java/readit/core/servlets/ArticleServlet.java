package readit.core.servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import com.adobe.xfa.ut.Resolver;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
@Component(
		service=Servlet.class,
		property ={
				"sling.servlet.resourceTypes=/readit/components/structure/page",
				"sling.servlet.methods=GET",
				"sling.servlet.methods=POST",
				"sling.servlet.extension=json",		   
                  }
)
public class ArticleServlet extends SlingAllMethodsServlet{
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String path="/content/readit/en/articles";
		ResourceResolver resolver = request.getResourceResolver();
		PageManager pagemanager=resolver.adaptTo(PageManager.class);
		Page pathpage= pagemanager.getPage(path);
		if(pathpage!=null) {
			Iterator<Page> page= pathpage.listChildren();
			while (page.hasNext()) {
				Page page2 = (Page) page.next();
			}
		}
		response.setContentType("application/json");
		JSONObject obj= new JSONObject();
		try {
			obj.put("title", "this is third servlet");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response.getWriter().print(obj.toString());
	}
}
