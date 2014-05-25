## Sling Adapters

The are many topics already covered to have a Sling implementation of the pet clinic. One feature, which is still missing is querying for pet owners. There are many ways to implement it.

1. Providing a servlet which can be invoked to load the data asynchronously.
2. Invoke a search directly in the script.
3. Providing a custom tag library descriptor (TLD) to be included.
4. Providing a declarative OSGi search service.
5. Provding an adapter for a search service.

The direct benefits in Sling adapters are the following:

- Sling adapters allow implementing an object mapper without reflection. Since the repositry is schemaless, the programmer has full control over the constraints for the creation policy.
- New adapters can be added and exchanged when needed. Since adapters can be added via OSGi services, enhancing/reducing a systems capabilties can be simply done by adding/removing services.
- Adapters reduce dependency management in classes, since object creation is completely handled by the adapter - similar to factories.
- [/system/console/adapters](http://localhost:8080/system/console/adapters) provides a transparent view on all adapters available.

### Implementing a pet owner search service

The requirements for the tasks are fairly straight forward. Based on a term, find all pet owners with properties which provide a hit. A resource resolver needs to be passed to access the resources and providing search results.

    package io.github.floriansalihovic.petclinic.search;
    
    import org.apache.sling.api.resource.*;
    
    import java.util.*;
    
    public interface PetOwnerSearchService {
    
        void setQuery(String query);
    
        void setResourceResolver(ResourceResolver resourceResolver);
    
        Iterator<Resource> findOwnerResources();
    }

The interface provides setters for all properties necessary and a `findOwnerResources` method to search for resources. An implementation could be implemented as follows.

    package io.github.floriansalihovic.petclinic.search.impl;
    
    import io.github.floriansalihovic.petclinic.search.*;
    import org.apache.felix.scr.annotations.*;
    import org.apache.sling.api.resource.*;
    import org.slf4j.*;
    
    import javax.jcr.query.*;
    import java.util.*;
    
    public class PetOwnerSearchServiceImpl implements PetOwnerSearchService {
    
        private static final Logger LOGGER = LoggerFactory.getLogger(PetOwnerSearchServiceImpl.class);
    
        private String query;
    
        @Override
        public void setQuery(final String query) {
            this.query = query;
        }
    
        private ResourceResolver resourceResolver;
    
        @Override
        public void setResourceResolver(final ResourceResolver resourceResolver) {
            this.resourceResolver = resourceResolver;
        }
    
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

Except for the query, which is written in `JCR-SQL2`, the implementation is stright forward. The `ResourceResource` passed is used to find all resources based on the query and if a term is passed (the naming is not so clever in this case) it will be applied as well.

### Implementing the search service adapter (factory)

Having the search service in place, one can quite easily implement an adapter. An adapter can be provided for any classes being a `org.apache.sling.api.adapter.Adabtable`. The first design decision made is transforming a `SlingHttpServletRequest` (being an `Adaptable` instance) into an `PetOwnerSearchService` instance. The request contains all information needed so it is a good match. The rest of the implementation is straight forward. 

    package io.github.floriansalihovic.petclinic.adapters;
    
    import io.github.floriansalihovic.petclinic.search.*;
    import io.github.floriansalihovic.petclinic.search.impl.*;
    import org.apache.felix.scr.annotations.*;
    import org.apache.sling.api.*;
    import org.apache.sling.api.adapter.*;
    import org.apache.sling.api.request.*;
    import org.slf4j.*;
    
    @Component
    @Service
    @Properties({
            @Property(name = AdapterFactory.ADAPTER_CLASSES,
                    value = {"io.github.floriansalihovic.petclinic.search.PetOwnerSearchService"}),
            @Property(name = AdapterFactory.ADAPTABLE_CLASSES,
                    value = {"org.apache.sling.api.SlingHttpServletRequest"})})
    public class PetOwnerSearchServiceAdapterFactory implements AdapterFactory {
    
        private static final Logger LOGGER = LoggerFactory.getLogger(PetOwnerSearchServiceAdapterFactory.class);
    
        @SuppressWarnings("unchecked")
        @Override
        public <AdapterType> AdapterType getAdapter(Object adaptable,
                                                    Class<AdapterType> type) {
            LOGGER.info("getAdapter {} for {}", adaptable, type);
            if (type != PetOwnerSearchService.class) {
                return null;
            } else if (adaptable instanceof SlingHttpServletRequest) {
                return (AdapterType) this.adaptTo((SlingHttpServletRequest) adaptable);
            } else {
                LOGGER.warn("getAdapter: Unexpected call with unsupported type {}; cannot adapt {}", type, adaptable);
            }
    
            return null;
        }
    
        private PetOwnerSearchService adaptTo(SlingHttpServletRequest request) {
            final RequestParameter p = request.getRequestParameter("q");
            final PetOwnerSearchService service = new PetOwnerSearchServiceImpl();
    
            if (null != p) {
                service.setQuery(p.getString());
            }
    
            service.setResourceResolver(request.getResourceResolver());
    
            return service;
        }
    }

The implications are, that the code in `/apps/petclinic/pages/owners/html.groovy` can be modified to the following.

    import io.github.floriansalihovic.petclinic.search.PetOwnerSearchService
    import org.apache.sling.api.resource.ValueMap
    
    def searchService = request.adaptTo(PetOwnerSearchService.class)
    def ownerResources = searchService ? searchService.findOwnerResources() : null
    
    sling.include(resource, 'petclinic/components/header')
    sling.include(resource, 'petclinic/components/navigation')
    
    markupBuilder.html {
      body {
        div(class: 'container') {
          div(class: 'ui grid') {
            div(class: 'seven wide column') {
              h1(class: 'ui header', 'Owners')
            }
            div(class: 'nine wide column') {
              div(class: 'ui icon input', style: 'float: right; margin-left: 1em;') {
                form(method: 'GET', action:"${resource.getPath()}.html") {
                  input(name: 'q', type: 'text', placeholder: 'Find owners...')
                }
                i(class: 'circular search icon', '')
              }
              a(href: "${resource.getPath()}.add.html", class: 'ui button green',
                  style: 'float: right; margin-left: 1em;', 'Add Owner')
            }
          }
          table(class: 'ui table segment') {
            thead {
              tr {
                th('Name'); th('City'); th('Address'); th('Telephone'); th('Pets')
              }
            }
            tbody {
              if (ownerResources) {
                ownerResources.each { ownerResource ->
                  def ownerProps = ownerResource.adaptTo(ValueMap.class)
                  tr {
                    td {
                      a(href: "${resource.getPath()}.detail.html${ownerResource.getPath()}",
                          "${ownerProps.get('firstName')} ${ownerProps.get('lastName')}")
                    }
                    td(ownerProps.get('city'))
                    td(ownerProps.get('address'))
                    td(ownerProps.get('telephone'))
                    td {
                      def petsResource = ownerResource.getChild('pets')
                      if (petsResource) {
                        div {
                          petsResource.listChildren().each { petResource ->
                            def petProps = petResource.adaptTo(ValueMap.class)
                            span(class: 'ui small label teal', "${petProps.get('name')}")
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    
    sling.include(resource, 'petclinic/components/footer')

The differences are subtle, but already an enhancement. The direct access of a node in the repository was removed by a service call. The service is taking care of retrieving the appropriate data.

### Adapting pet owner resources

`org.apache.sling.api.resource.Resource` is also a a `org.apache.sling.api.adapter.Adabtable` implementation. Implementing an adapter for pet owner resources is a good decision to hide information on how data is actually stored. Starting with a `PetOwner` class.

    package io.github.floriansalihovic.petclinic.owners;
    
    import java.util.*;
    
    public interface PetOwner {
    
        String getAddress();
    
        String getCity();
    
        String getFirstName();
    
        String getLastName();
    
        String getPath();
    
        Collection<String> getPetNames();
    
        String getTelephone();
    }

This interface is expressive enough to refactor the `/apps/petclinic/pages/owners/html.groovy` a bit further. An implementation may be like

    package io.github.floriansalihovic.petclinic.owners.impl;
    
    import io.github.floriansalihovic.petclinic.owners.*;
    import org.apache.sling.api.resource.*;
    
    import java.util.*;
    
    public class PetOwnerResourceDecoratorImpl implements PetOwner {
    
        private final Resource petOwnerResource;
    
        private String address;
    
        @Override
        public String getAddress() { return this.address; }
    
        private String city;
    
        @Override
        public String getCity() { return this.city; }
        
        public String firstName;

        @Override
        public String getFirstName() { return this.firstName; }
    
        public String lastName;
    
        @Override
        public String getLastName() { return this.lastName; }
    
        private String telephone;
    
        @Override
        public String getPath() { return this.resource.getPath(); }
    
        private Collection<String> petNames;
    
        @Override
        public Collection<String> getPetNames() { return this.petNames; }
    
        @Override
        public String getTelephone() { return this.telephone; }
    
        public PetOwnerResourceDecoratorImpl(final Resource resource) {
            this.petOwnerResource = resource;
    
            final ValueMap petOwnerProps = resource.adaptTo(ValueMap.class);
    
            this.firstName = petOwnerProps.get("firstName", String.class);
            this.lastName = petOwnerProps.get("lastName", String.class);
            this.city = petOwnerProps.get("city", String.class);
            this.address = petOwnerProps.get("address", String.class);
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

An AdapterFactory - similar to the `PetOwnerSearchServiceAdapterFactory` - must be provided to be able to use the model.

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

        private final static Logger LOGGER = LoggerFactory.getLogger(PetOwnerAdapterFactory.class);

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

        private PetOwner adaptTo(final Resource resource) {
            return new PetOwnerResourceDecoratorImpl(resource);
        }
    }


To provide a comfortable access, the `PetOwnerSearchService` will be modified by providing a method `findPetOwners` which will return a typed collection with the newly added domain class.

    package io.github.floriansalihovic.petclinic.search;
    
    import io.github.floriansalihovic.petclinic.owners.*;
    import org.apache.sling.api.resource.*;
    
    import java.util.*;
    
    public interface PetOwnerSearchService {
    
        void setQuery(String query);
    
        void setResourceResolver(ResourceResolver resourceResolver);
    
        Iterator<PetOwner> findPetOwners();
    }

The implementation will be slightly modified, encapsulating the previous method and returning an `Iteraction<PetOwner>`.

    package io.github.floriansalihovic.petclinic.search.impl;

    import io.github.floriansalihovic.petclinic.owners.*;
    import io.github.floriansalihovic.petclinic.search.*;
    import org.apache.sling.api.resource.*;
    import org.slf4j.*;

    import javax.jcr.query.*;
    import java.util.*;

    public class PetOwnerSearchServiceImpl implements PetOwnerSearchService {

        private static final Logger LOGGER = LoggerFactory.getLogger(PetOwnerSearchServiceImpl.class);

        private String query;

        @Override
        public void setQuery(final String query) {
            this.query = query;
        }

        private ResourceResolver resourceResolver;

        @Override
        public void setResourceResolver(final ResourceResolver resourceResolver) {
            this.resourceResolver = resourceResolver;
        }

        @Override
        public Iterator<PetOwner> findPetOwners() {
            return ResourceUtil.adaptTo(this.findPetOwnerResources(), PetOwner.class);
        }

        private Iterator<Resource> findPetOwnerResources() {
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

This will lead to an even cleaner design in `/apps/petclinic/components/pages/html.groovy` as the following groovy script shows.

    import io.github.floriansalihovic.petclinic.search.PetOwnerSearchService
    
    def searchService = request.adaptTo(PetOwnerSearchService.class)
    def petOwnersIterator = searchService ? searchService.findPetOwners() : null
    
    sling.include(resource, 'petclinic/components/header')
    sling.include(resource, 'petclinic/components/navigation')
    
    markupBuilder.html {
      body {
        div(class: 'container') {
          div(class: 'ui grid') {
            div(class: 'seven wide column') {
              h1(class: 'ui header', 'Owners')
            }
            div(class: 'nine wide column') {
              div(class: 'ui icon input', style: 'float: right; margin-left: 1em;') {
                form(method: 'GET', action: "${resource.getPath()}.html") {
                  input(name: 'q', type: 'text', placeholder: 'Find owners...')
                }
                i(class: 'circular search icon', '')
              }
              a(href: "${resource.getPath()}.add.html", class: 'ui button green',
                  style: 'float: right; margin-left: 1em;', 'Add Owner')
            }
          }
          table(class: 'ui table segment') {
            thead {
              tr {
                th('Name'); th('City'); th('Address'); th('Telephone'); th('Pets')
              }
            }
            tbody {
              if (petOwnersIterator) {
                petOwnersIterator.each() { petOwner ->
                  tr {
                    td {
                      a(href: "${petOwner.getPath()}.detail.html${petOwner.getPath()}",
                          "${petOwner.getFirstName()} ${petOwner.getLastName()}")
                    }
                    td(petOwner.getCity())
                    td(petOwner.getAddress())
                    td(petOwner.getTelephone())
                    td {
                      div {
                        petOwner.getPetNames().each() { name ->
                          span(class: 'ui small label teal', "${name}")
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    
    sling.include(resource, 'petclinic/components/footer')
