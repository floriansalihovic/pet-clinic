package io.github.floriansalihovic.petclinic.owners.impl;

import io.github.floriansalihovic.petclinic.owners.*;

public class OwnerImpl implements Owner {

    /**
     * Storage variable for the first name property.
     */
    public final String firstName;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Storage variable for the first last name property.
     */
    public final String lastName;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Creates a new Owner instance.
     *
     * @param firstName
     *         The first name of the owner.
     * @param lastName
     *         The last name of the owner.
     */
    public OwnerImpl(String firstName,
                     String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
