# The Sling PetClinic Application

This application is intended to provide a fast and easy access to Apache Sling using Groovy as a scripting language.

For an extensive guide about the implementation details, please have a look at the [wiki pages at github](https://github.com/floriansalihovic/pet-clinic/wiki).

## Run the Sling Pet Clinic locally

To run the Sling Pet Clinic, the following steps are required.

1. Maven 3 must be installed.
2. GIT must be installed.
3. A Sling instance must be installed and running.
4. `git clone git@github.com:floriansalihovic/pet-clinic.git` will checkout the project.
5. `cd pet-clinic`
6. `mvn clean install -PautoInstallBundle`
7. Open the browser at [`http://localhost:8080/content/petclinic/en/owners.html`](http://localhost:8080/content/petclinic/en/owners.html)