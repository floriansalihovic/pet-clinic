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
        @Property(name = AdapterFactory.ADAPTER_CLASSES, value = {"io.github.floriansalihovic.petclinic.owners.Owner"}),
        @Property(name = AdapterFactory.ADAPTABLE_CLASSES, value = {"org.apache.sling.api.resource.Resource"})})
public class OwnerAdapterFactory implements AdapterFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(OwnerAdapterFactory.class);

    private final static Class<Owner> OWNER = Owner.class;

    @SuppressWarnings("unchecked")
    @Override
    public <AdapterType> AdapterType getAdapter(Object adaptable,
                                                Class<AdapterType> type) {
        LOGGER.info("getAdapter {} for {}", adaptable, type);
        if (type != OWNER) {
            return null;
        } else if (adaptable instanceof Resource) {
            return (AdapterType) this.adaptTo((Resource) adaptable);
        } else {
            LOGGER.warn("getAdapter: Unexpected call with unsupported type {}; cannot adapt {}", type, adaptable);
        }

        return null;
    }

    /**
     *
     * @param resource
     * @return
     */
    private Owner adaptTo(final Resource resource) {
        final ValueMap map = resource.adaptTo(ValueMap.class);

        final String firstName = map.get("firstName", String.class);
        final String lastName = map.get("lastName", String.class);

        return new OwnerImpl(firstName, lastName);
    }
}
