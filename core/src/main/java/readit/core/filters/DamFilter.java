package readit.core.filters;
import java.io.IOException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.engine.EngineConstants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.granite.asset.api.AssetManager;
import com.day.cq.dam.api.Asset;
@Component(
		service = Filter.class,
		property= {
				EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST,
				EngineConstants.SLING_FILTER_PATTERN + "=/content/dam/acg/.*"})
public class DamFilter implements Filter{
	private static final Logger log = LoggerFactory.getLogger(DamFilter.class);
	AssetManager assetManager = null;
	@Override
	public void destroy() {
	}
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		log.info("DamFilter");
		SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
		ResourceResolver resolver1 = (ResourceResolver) slingRequest.getResourceResolver();
		String path =slingRequest.getRequestPathInfo().getResourcePath();
		AssetManager assetManager =  resolver1.adaptTo(AssetManager.class);
		Asset asset = (Asset) assetManager.getAsset(path);
		String useIn =asset.getMetadataValue("dc:useinportal");
		if(useIn.equals("true")) {
			filterChain.doFilter(slingRequest, response);
		}else {
			String[] accesslvl = (String[]) asset.getMetadata().get("dc:accesslevel");
			log.debug("got metadata values{}",((AssetManager) asset).getAsset(useIn));
			if(accesslvl!=null && accesslvl.equals("marketing-hub:access-levels/public")) {
				filterChain.doFilter(slingRequest, response);
			}else {
				try {
					Session session = resolver1.adaptTo(Session.class);
					UserManager userManager = resolver1.adaptTo(UserManager.class);
					userManager.getAuthorizable(session.getUserID());
				} catch (RepositoryException e) {					
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
