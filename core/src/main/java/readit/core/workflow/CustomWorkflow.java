package readit.core.workflow;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowNode;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;

@Component(service=WorkflowProcess.class,
property= {"process.label= Replicating Content"}
		)
public class CustomWorkflow implements WorkflowProcess{

	Logger log  = LoggerFactory.getLogger(CustomWorkflow.class);
	@Reference
	private ResourceResolverFactory resolverFactory;

	@Reference
	private Replicator replicator;

	@Override
	public void execute(WorkItem workitem, WorkflowSession wfsession, MetaDataMap metadata) throws WorkflowException {
		log.info("Executing the workflow");
		try
		{
			log.info("Here in execute method");   
			WorkflowNode myNode = workitem.getNode(); 
			String myTitle = myNode.getTitle(); 
			log.info("The title is "+myTitle);  
			WorkflowData workflowData = workitem.getWorkflowData(); 
			String path = workflowData.getPayload().toString();
			replicationContent(path, wfsession);       
		}
		catch (Exception e)
		{
			e.printStackTrace()  ; 
		}
	}
	private String replicationContent(String path, WorkflowSession wfsession) throws LoginException
	{
		try
		{
			Session session = wfsession.adaptTo(Session.class);
			Map<String, Object> props = new HashMap<>();
			props.put(ResourceResolverFactory.SUBSERVICE,"writeservice");
			ResourceResolver rr = null;
			ResourceResolver resourceResolver = resolverFactory.getResourceResolver(props);
			replicator.replicate(session,ReplicationActionType.ACTIVATE,path); 
			log.info("Replicate");
		} catch (ReplicationException e) {
			log.info("Replication failed "+e.getMessage(), path);
			e.printStackTrace();
		}
		return path;
	}
}




