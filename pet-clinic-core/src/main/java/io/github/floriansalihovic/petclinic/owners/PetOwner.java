package io.github.floriansalihovic.petclinic.owners;

import java.util.*;

public interface PetOwner {

    /**
     * The accessor to the owner's current address.
     *
     * @return the name of the address the pet owner lives at.
     */
    String getAddress();

    /**
     * The accessor to the owner's current location.
     *
     * @return the name of the city the pet owner lives in.
     */
    String getCity();

    /**
     * The accessor to the owner's first name.
     *
     * @return the first name of the owner.
     */
    String getFirstName();

    /**
     * The accessor to the owner's last name.
     *
     * @return the first last of the owner.
     */
    String getLastName();

    /**
     * Provide access to the path of the source resource.
     *
     * @return The path of the source resource.
     */
    String getPath();

    /**
     * The accessor for a pet owner's pets.
     *
     * @return The collection containing the names of all pets.
     */
    Collection<String> getPetNames();

    /**
     * The accessor to the owner's telephone number.
     *
     * @return the telephone number of the owner.
     */
    String getTelephone();
}
