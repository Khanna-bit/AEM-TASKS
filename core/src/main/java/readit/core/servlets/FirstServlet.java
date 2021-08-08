package readit.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

@SuppressWarnings("deprecation")
@Component(
		service=Servlet.class,
		property = {
				"sling.servlet.resourceTypes=readit/components/structure/page",
				"sling.servlet.methods=POST",
				"sling.servlet.selectors=articles",
			    "sling.servlet.extension=json",
			    "sling.servlet.methods=GET"
		}
)
public class FirstServlet extends SlingAllMethodsServlet{	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		JSONObject obj = new JSONObject();
		try {
			obj.put("title","data from resource based servlet");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response.getWriter().print(obj.toString());
	}
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		JSONObject obj = new JSONObject();
		try {
			obj.put("title","data from resource based servlet");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response.getWriter().print(obj.toString());
	}
}
