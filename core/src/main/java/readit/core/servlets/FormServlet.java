package readit.core.servlets;

import java.io.IOException;

import javax.jcr.Node;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
		service = Servlet.class,
		property= {
				"Sling.servlet.paths=/bin/var/userdata",
				"Sling.servlet.methods=POST"
		}
		)
public class FormServlet extends SlingAllMethodsServlet{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
	}
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {	
		try {
			String emailId = request.getParameter("firstname");
			String lastname = request.getParameter("lastname");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			JSONObject obj=new JSONObject();
			obj.put("firstname",emailId);
			obj.put("lastname",lastname);
			obj.put("email",email);
			obj.put("password", password);
			obj.put("gender", gender);
			String jsonText= obj.toString();
			response.getWriter().write(jsonText);   
			Resource resource = request.getResourceResolver().getResource("/var/userdata");
			Node node = resource.adaptTo(Node.class);
			Node newNode =	node.addNode(emailId);
			logger.error(emailId, newNode.setProperty("firstname", emailId));
			newNode.setProperty("lastname", lastname);
			newNode.setProperty("email", email);
			newNode.setProperty("password", password);
			newNode.setProperty("gender", gender);
			newNode.getSession().save();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.debug("errorlog {}",e.getMessage());
		}
	}
}

