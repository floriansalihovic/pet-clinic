package io.github.floriansalihovic.petclinic.adapters;

import io.github.floriansalihovic.petclinic.owners.*;
import io.github.floriansalihovic.petclinic.owners.impl.*;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.adapter.*;
import org.apache.sling.api.resource.*;
import org.slf4j.*;

@Component
@Service
@Properties({
        @Property(name = AdapterFactory.ADAPTER_CLASSES, value = {"io.github.floriansalihovic.petclinic.owners.PetOwner"}),
        @Property(name = AdapterFactory.ADAPTABLE_CLASSES, value = {"org.apache.sling.api.resource.Resource"})})
public class PetOwnerAdapterFactory implements AdapterFactory {

    /**
     * The default logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(PetOwnerAdapterFactory.class);

    /**
     * The adapter class.
     */
    private final static Class<PetOwner> OWNER = PetOwner.class;

    @SuppressWarnings("unchecked")
    @Override
    public <AdapterType> AdapterType getAdapter(Object adaptable,
                                                Class<AdapterType> type) {
        LOGGER.info("getAdapter {} for {}", adaptable, type);
        if (type != OWNER) {
            return null;
        } else if (adaptable instanceof Resource) {
            final Resource resource = (Resource) adaptable;
            if (resource.isResourceType("petclinic/owner")) {
                return (AdapterType) this.adaptTo(resource);
            }
        } else {
            LOGGER.warn("getAdapter: Unexpected call with unsupported type {}; cannot adapt {}", type, adaptable);
        }

        return null;
    }

    /**
     * Adapts a resource to a PetOwner instance.
     *
     * @param resource
     *         The resource to be adapted.
     *
     * @return The PetOwner instance created.
     */
    private PetOwner adaptTo(final Resource resource) {
        return new PetOwnerResourceDecoratorImpl(resource);
    }
}
