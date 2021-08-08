package readit.core.servlets;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component(service=Servlet.class,
property= {
		"sling.servlet.resourceTypes=cq:Page",
		"sling.servlet.methods=GET",
		"sling.servlet.methods=POST",
		"sling.servlet.methods=DELETE",
		"sling.servlet.selectors=getlightboxes",
		"sling.servlet.selectors=createlightbox",
		"sling.servlet.selectors=deletelightbox"

}
		)
public class LightBox extends SlingAllMethodsServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(LightBox.class);
	private static final String LIGHTBOX = "lightboxes";
	@Override
	public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		log.info("light");
		ResourceResolver resourceResolver = request.getResourceResolver();
		Session session =resourceResolver.adaptTo(Session.class);
		String userId = session.getUserID();
		log.debug("userid{}", userId);
		if(userId!="anonymous") {
			Resource resource = request.getResourceResolver().getResource("/var/"+userId);
			log.debug("resource userid{}", resource.getParent());
			Node node = null;
			if(resource!=null) {
				node = resource.adaptTo(Node.class);
				try {
					log.debug("node {}", node.getName());
				} catch (RepositoryException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					if(node.hasNode(LIGHTBOX)) {
						Resource resource1 = request.getResourceResolver().getResource(resource.getPath()+"/"+LIGHTBOX);
						Iterator<Resource> children = resource1.listChildren();
						JSONObject json = new JSONObject();
						while (children.hasNext()) {
							Resource child = children.next();
							String childNodeName = child.getName();
							String[] assets = child.getValueMap().get("assets", String[].class);	
							log.debug("asset {}", assets);
							json.put(childNodeName, assets);
						}
						response.getWriter().print(json.toString());
					}
				} catch (RepositoryException | JSONException e){
					log.error(e.getMessage());
				}
			}
		}
	}
	@Override
	public void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws PersistenceException {
		log.info("inside post call");
		ResourceResolver resourceResolver=request.getResourceResolver();
		String userId=resourceResolver.getUserID();
		if(userId!="anonymous") {
			Resource resource	= request.getResourceResolver().getResource("/var");
			log.debug("resource name {}",resource.getPath());
			Map<String, Object> lightMap = new HashMap<>();
			Resource userNode = createResource(resourceResolver, resource, userId, lightMap, true);
			log.debug("create user node {}",userNode);
			Resource nodeResource = (Resource) createResource(resourceResolver, userNode, LIGHTBOX, lightMap, true);
			log.debug("create lightbox node {}",nodeResource);
			String lightboxName = request.getParameter("lightboxname");
			if(lightboxName!=null) {
				lightMap.put("assets", request.getParameterValues("assets"));
				createResource(resourceResolver, nodeResource, lightboxName, lightMap, true);
				log.debug("lightboxName {}",lightboxName);
			}	
		}
	}
	@Override
	protected void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		log.info("inside post call");
		ResourceResolver resourceResolver=request.getResourceResolver();
		String userId=resourceResolver.getUserID();
		if(userId!="anonymous") {
			Resource resource= request.getResourceResolver().getResource("/var/"+userId);
			log.debug("resource path {}",resource.getPath());
			deleteResource(resourceResolver, resource);
			resourceResolver.commit();
		}
	}
	private Resource createResource(ResourceResolver resolver, Resource parent, String name, Map<String,Object> props, boolean create) {
		Resource lightboxNode = null;
		log.info("lightboxNode");
		if(parent.getChild(name) == null && create) {
			try {
				lightboxNode =	resolver.create(parent, name, props);
				log.debug("lightbox{}",lightboxNode.toString());
				lightboxNode.getResourceResolver().commit();
			} catch (PersistenceException e) {
				e.printStackTrace();
			}			
		} else {
			lightboxNode = resolver.getResource(name);
			log.debug("node return{}",lightboxNode.toString());
		}
		return lightboxNode;
	}
	private void deleteResource(ResourceResolver resolver, Resource Node) {
		try {
			resolver.delete(Node);
			log.info("node delete{}");

		} catch (PersistenceException e) {
			log.error(e.getMessage());
		}	
	}
}