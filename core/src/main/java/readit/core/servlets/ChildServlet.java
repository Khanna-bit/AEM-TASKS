package readit.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.json.JsonArray;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.swing.text.Utilities;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.adapter.Adaptable;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Component(
		service=Servlet.class,
		property = {
				"sling.servlet.resourceTypes=cq:Page",
				"sling.servlet.methods=GET",
				"sling.servlet.selectors=childpages",
				"sling.servlet.extension=json"
		}
		)
public class ChildServlet extends SlingAllMethodsServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		try {
			ResourceResolver resourceResolver = request.getResourceResolver();
			PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
			Page childpage=pageManager.getPage(request.getResource().getPath());
			JSONArray childpage1 = new JSONArray();
			if(childpage1!=null) {
				Iterator<Page> pages= ((Page) childpage1).listChildren();
				while (pages.hasNext()) {
					Page page1 = pages.next();
					JSONObject obj=new JSONObject();
					obj.put("title",page1.getTitle());
					obj.put("description",page1.getDescription());
					childpage1.put(obj);
				}
			}
		}
		catch (JSONException e) {	
			e.printStackTrace();
		}
		PrintWriter output = response.getWriter();
		Object childpage1 = null;
		output.print(childpage1.toString());
	}
}

