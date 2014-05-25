package io.github.floriansalihovic.petclinic.owners.impl;

import io.github.floriansalihovic.petclinic.owners.*;
import org.apache.sling.api.resource.*;

import java.util.*;

public class PetOwnerResourceDecoratorImpl implements PetOwner {

    // The decorated resource.
    private final Resource petOwnerResource;

    // Storage variable for the address property.
    public final String address;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAddress() {
        return this.address;
    }

    // Storage variable for the city property.
    private String city;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCity() {
        return this.city;
    }

    // Storage variable for the first name property.
    public final String firstName;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFirstName() {
        return this.firstName;
    }

    // Storage variable for the first last name property.
    public final String lastName;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLastName() {
        return this.lastName;
    }

    // Storage variable for the telephone property.
    private String telephone;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return this.petOwnerResource.getPath();
    }

    // Storage variable for the pet names property.
    private Collection<String> petNames;

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getPetNames() {
        return this.petNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTelephone() {
        return this.telephone;
    }

    /**
     * Creates a new Owner instance.
     *
     * @param resource
     *         The resource to be decorated.
     */
    public PetOwnerResourceDecoratorImpl(final Resource resource) {
        this.petOwnerResource = resource;

        final ValueMap petOwnerProps = this.petOwnerResource.adaptTo(ValueMap.class);

        this.address = petOwnerProps.get("address", String.class);
        this.city = petOwnerProps.get("city", String.class);
        this.firstName = petOwnerProps.get("firstName", String.class);
        this.lastName = petOwnerProps.get("lastName", String.class);
        this.telephone = petOwnerProps.get("telephone", String.class);
        this.petNames = new ArrayList<String>();

        final Resource petsResource = this.petOwnerResource.getChild("pets");
        if (null != petsResource) {
            final Iterator<ValueMap> children =
                    ResourceUtil.adaptTo(petsResource.listChildren(), ValueMap.class);
            while (children.hasNext()) {
                ValueMap petProps = children.next();
                this.petNames.add(petProps.get("name", String.class));
            }
        }
    }
}
