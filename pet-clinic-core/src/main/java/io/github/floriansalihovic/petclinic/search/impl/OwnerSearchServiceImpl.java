package io.github.floriansalihovic.petclinic.search.impl;

import io.github.floriansalihovic.petclinic.search.*;
import org.apache.sling.api.resource.*;
import org.slf4j.*;

import javax.jcr.query.*;
import java.util.*;

public class OwnerSearchServiceImpl implements OwnerSearchService {

    /**
     * The default logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OwnerSearchServiceImpl.class);

    private String query;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setQuery(final String query) {
        this.query = query;
    }

    private ResourceResolver resourceResolver;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResourceResolver(final ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Resource> findOwnerResources() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("select * from [nt:unstructured] as owner ")
                .append("where (owner.[sling:resourceType] = \"petclinic/owner\"");

        if (null != query) {
            buffer.append(" and contains(owner.*, \"*").append(query).append("*\")");
        }

        buffer.append(")");

        LOGGER.info("Executing query \"{}\".", query);

        return resourceResolver.findResources(buffer.toString(), Query.JCR_SQL2);
    }
}
