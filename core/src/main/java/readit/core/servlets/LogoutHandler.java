package readit.core.servlets;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
@Component(
		service=Servlet.class,
		property = {
				"sling.servlet.resourceTypes=/readit/components/structure/page",
				"sling.servlet.methods=POST",
				"sling.servlet.selectors=customlogout",
				"sling.servlet.extension=html",
				"sling.servlet.methods=GET"
		}
		)
public class LogoutHandler extends SlingAllMethodsServlet{
	private static final long serialVersionUID = 1L;
	private ServletRequest request;
	String path1 =request.getParameter("path");
	@Reference
	private LogoutHandler LogoutHandler;
	public void dropCredentials(HttpServletRequest request, HttpServletResponse response) throws ServletException {
	}
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("path1");  
		response.getWriter().print(path1.toString());
	}
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

	}
}

