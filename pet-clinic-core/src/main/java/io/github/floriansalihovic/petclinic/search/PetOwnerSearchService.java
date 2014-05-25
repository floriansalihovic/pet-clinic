package io.github.floriansalihovic.petclinic.search;

import io.github.floriansalihovic.petclinic.owners.*;
import org.apache.sling.api.resource.*;

import java.util.*;

/**
 * Service defining the interface to query for owner resources. Owner resources are identified by resource type
 * "petclinic/owner".
 */
public interface PetOwnerSearchService {

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
     * Finds all owners. An owner is identified bu the resource type petclinic/owner. The method will return all owners,
     * if no query was provided. If a query is set, it will be return only the pet owners matching the given criteria.
     *
     * @return All pet owners found.
     */
    Iterator<PetOwner> findPetOwners();
}
