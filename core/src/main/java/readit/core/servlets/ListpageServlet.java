package readit.core.servlets;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.LoggerFactory;
import readit.core.services.ListService;
@Component(
		service=Servlet.class,
		property = {
				"sling.servlet.resourceTypes=cq:Page",
				"sling.servlet.selectors=childpages",
				"sling.servlet.methods=GET",
				"sling.servlet.extension=json"
		}
		)
public class ListpageServlet extends SlingAllMethodsServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(ListpageServlet.class);
	@Reference
	private ListService ls;
	String res=null;
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		res = ls.getlist();
		response.getWriter().print(res);


	}
}

