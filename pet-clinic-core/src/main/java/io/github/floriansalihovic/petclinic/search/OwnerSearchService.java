package io.github.floriansalihovic.petclinic.search;

import org.apache.sling.api.resource.*;

import java.util.*;

/**
 * Service defining the interface to query for owner resources. Owner resources are identified by resource type
 * "petclinic/owner".
 */
public interface OwnerSearchService {

    /**
     * The query to be evaluated. If the query is empty, all owner resources will return.
     *
     * @param query
     *         The query string.
     */
    void setQuery(String query);

    /**
     * The resource resolver used to access
     *
     * @param resourceResolver
     */
    void setResourceResolver(ResourceResolver resourceResolver);

    /**
     * Finds all owner resources. An owner is identified bu the resource type petclinic/owner. The method will return
     * all resources, if no query was provided.
     *
     * @return All resource matching the given query or all resources of resource type petclinic/owner.
     */
    Iterator<Resource> findPetOwnerResources();
}
