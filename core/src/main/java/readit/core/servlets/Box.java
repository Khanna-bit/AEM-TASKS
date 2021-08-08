package readit.core.servlets;
import java.io.IOException;
import javax.jcr.AccessDeniedException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.base.util.AccessControlUtil;
import org.osgi.service.component.annotations.Component;
@Component(
		service = Servlet.class,
		property= {
				"Sling.servlet.resourceTypes=/var/userdata",
				"Sling.servlet.methods=GET",
				"Sling.servlet.methods=POST",
				"Sling.servlet.selectors=createlightbox"
		}
		)
public class Box extends SlingAllMethodsServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String userId = null;
		try {
			userId = getCurrentUserId(request);
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		response.getWriter().write(userId);
	}
	public String getCurrentUserId(SlingHttpServletRequest request) throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
		ResourceResolver resolver = request.getResourceResolver();
		Session session1 = resolver.adaptTo(Session.class);
		org.apache.jackrabbit.api.security.user.UserManager userManager;
		Session session = request.getResourceResolver().adaptTo(Session.class);
		userManager = (org.apache.jackrabbit.api.security.user.UserManager) AccessControlUtil.getUserManager(session);
		org.apache.jackrabbit.api.security.user.User currentUser = (org.apache.jackrabbit.api.security.user.User) userManager.getAuthorizable(session.getUserID());
		String currentUser1 = session.getUserID();
		Resource resource = request.getResourceResolver().getResource("/var/username");
		Node node = resource.adaptTo(Node.class);
		Node newNode = null;
		String userId = null;
		try {
			String lightbox = userId;
			newNode = node.addNode(lightbox);
		} catch (RepositoryException e1) {
			e1.printStackTrace();
		}     
		return userId;
	}
}

