package readit.core.services;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name="child page")
public @interface List{

	@AttributeDefinition(name = "list page")

	public String getWay();

}
