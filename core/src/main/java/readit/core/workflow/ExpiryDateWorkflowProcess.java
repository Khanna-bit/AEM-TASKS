package readit.core.workflow;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
@Component(service=WorkflowProcess.class,
           property= {"process.label=Update Expiry Date for Page"}
		)

public class ExpiryDateWorkflowProcess implements WorkflowProcess{
	@Reference
	private ResourceResolverFactory factory;
	@Override
	public void execute(WorkItem workItem, WorkflowSession wfsession, MetaDataMap metadata) throws WorkflowException {
           String payload = workItem.getWorkflowData().getPayloadType().toString();
           try {
        	   Map<String,Object> props = new HashMap<>();
        	   props.put(factory.SUBSERVICE, "writeservice");
        	   ResourceResolver resolver = factory.getResourceResolver(props);
        	   Resource resource = resolver.getResource(payload+"/jcr:content");
        	   if(resource!=null && payload!=null) {
        		   Node node = resource.adaptTo(Node.class);
        		   Calendar cal = Calendar.getInstance();
        		   cal.set(Calendar.DATE,20);
        		   node.setProperty("expiredate", cal);
        		   node.getSession().save();
        	   }
		} catch (org.apache.sling.api.resource.LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValueFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VersionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConstraintViolationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           
	} 

}
