## Groovy scripts

Groovy is a dynamic language running on the JVM. It provides a lot of nice features and syntactic sugar to make it a perfect match for a counterpart of Java, eliminating the need for JSPs. In this guide the ```groovy.xml.MarkupBuilder``` is essential and its api is almost a small DSL for creating markup. The big advantage is that the programmer operates from within the language. Whereas in JSPs markup, scriptlets and the JSTL are a minimum set of technologies involved, in Groovy its just groovy integrating seamlessly with the Java API. The following code illustrates the implementation of a basic page, being also the main template used for the pages.

    import groovy.xml.MarkupBuilder;
    
    def builder = new MarkupBuilder(out)
    builder.html {
      head {
        meta(charset: 'UTF-8')
        meta(content: 'IE=edge,chrome=1', 'http-equiv': 'X-UA-Compatible');
        meta(name: 'viewport', content: 'width=device-width, initial-scale=1.0')
        link(href: '/etc/clientlibs/petclinic/css/default.css', rel: 'stylesheet')
        link(href: '/etc/clientlibs/petclinic/css/semantic.min.css', rel: 'stylesheet')
        script('', type: 'text/javascript', src: '/etc/clientlibs/petclinic/js/jquery-1.11.0.min.js')
        script('', type: 'text/javascript', src: '/etc/clientlibs/petclinic/js/semantic.min.js')
        script('', type: 'text/javascript', src: '/etc/clientlibs/petclinic/js/form.js')
      }
      body {
        div(class: 'ui menu teal inverted') {
          a(class: 'title item', href: '/content/petclinic/en/owners.html') {
            strong('Pet Clinic')
          }
          a(class: 'item', href: '/content/petclinic/en/owners.html', 'Find Owners')
          a(class: 'item', href: '/content/petclinic/en/vets.html', 'Veterinarians')
          a(class: 'item', href: '/content/petclinic/en/specialities.html', 'Specialities')
          a(class: 'item', href: '/content/petclinic/en/petTypes.html', 'Pet Types')
        }
        div(class: 'container') {
          div(class: 'ui grid') {
            div(class: 'seven wide column') {
              h1(class: 'ui header', 'Add Owner')
            }
          }

          // content goes here

        div(class: 'ui divider')
        div(class: 'ui divided horizontal footer link list') {
          div(class: 'item') {
            mkp.yieldUnescaped '&copy; 2014 Florian Salihovic'
          }
          a(class: 'item', href: 'https://github.com/floriansalihovic/pet-clinic', 'at github');
          div(class: 'item') {
            a(href: 'https://sling.apache.org', target: '_blank', 'Powered by Apache Sling')
          }
          div(class: 'item') {
            a(href: 'http://floriansalihovic.github.io', target: '_blank', 'by me')
          }
        }
      }
    }

Although it is no markup, it looks and feels like writing markup. Adding dynamic rendering comes with the language itself, there is not additional library required. Taking for instance the form to create an owner.

    form(class: 'ui form', role: 'form', action: '/sling/content/owners/*', method: 'POST') {
      div(class: 'field') {
        label(for: 'firstName', 'First Name:')
        input(id: 'firstName', name: 'firstName', type: 'text', placeholder: 'First Name')
      }
      div(class: 'field') {
        label(for: 'lastName', 'Last Name:')
        input(id: 'lastName', name: 'lastName', type: 'text', placeholder: 'Last Name')
      }
      div(class: 'field') {
        label(for: 'address', 'Address:')
        input(id: 'address', name: 'address', type: 'text', placeholder: 'Address')
      }
      div(class: 'field') {
        label(for: 'city', 'City:')
        input(id: 'city', name: 'city', type: 'text', placeholder: 'City')
      }
      div(class: 'field') {
        label(for: 'telephone', 'Telephone:')
        input(id: 'telephone', name: 'telephone', type: 'text', placeholder: 'Telephone')
      }
      input(type: 'hidden', name: 'sling:resourceType', value: 'petclinic/components/pages/owners')
      input(type: 'hidden', name: ':redirect', value: "${resource.getPath()}.html")
      input(type: 'hidden', name: '_charset_', value: 'UTF-8')
      button(type: 'submit', class: 'ui blue submit button', 'Save')
    }

This form creates a new owner resource. To edit an owner resource, the form looks alomost identical, the problem which has to be solved is acccessing the resources properties.

Accessing the properties of a resource can be done by adapting it to a ValueMap and reading its keys and values. In the following code, ```request``` and ```resource``` are passed to the scripting engine, all other values can be derived from then as the example illustrates.

    import org.apache.sling.api.resource.ValueMap

    def suffix = request.getRequestPathInfo().getSuffix()
    def resourceResolver = resource.getResourceResolver()
    def ownerResource = resourceResolver.getResource(suffix)
    def ownerProps = ownerResource.adaptTo(ValueMap.class)

    form(class: 'ui form', role: 'form', action: suffix, method: 'POST') {
      div(class: 'field') {
        label(for: 'firstName', 'First Name:')
        input(id: 'firstName', name: 'firstName', type: 'text', placeholder: 'First Name',
              value: "${ownerProps.get('firstName')}")
      }
      div(class: 'field') {
        label(for: 'lastName', 'Last Name:')
        input(id: 'lastName', name: 'lastName', type: 'text', placeholder: 'Last Name',
              value: "${ownerProps.get('lastName')}")
      }
      div(class: 'field') {
        label(for: 'address', 'Address:')
        input(id: 'address', name: 'address', type: 'text', placeholder: 'Address',
      value: "${ownerProps.get('address')}")
      }
      div(class: 'field') {
        label(for: 'city', 'City:')
        input(id: 'city', name: 'city', type: 'text', placeholder: 'City',
              value: "${ownerProps.get('city')}")
      }
      div(class: 'field') {
        label(for: 'telephone', 'Telephone:')
        input(id: 'telephone', name: 'telephone', type: 'text', placeholder: 'Telephone',
              value: "${ownerProps.get('telephone')}")
      }
      input(type: 'hidden', name: 'sling:resourceType', value: 'petclinic/components/pages/owners')
      input(type: 'hidden', name: ':redirect', value: "${resource.getPath()}.html")
      input(type: 'hidden', name: '_charset_', value: 'UTF-8')
      button(type: 'submit', class: 'ui blue submit button', 'Save')
    }

A more complex example would be the adding and modification of pets. Pets are bound to an owner and are always of a certain type.

    import org.apache.sling.api.resource.ValueMap
    
    def suffix = request.getRequestPathInfo().getSuffix()
    def resourceResolver = resource.getResourceResolver()
    def ownerResource = resourceResolver.getResource(suffix)
    def ownerProps = ownerResource.adaptTo(ValueMap.class)
    def petTypesResource = resourceResolver.getResource('/sling/content/petTypes')

    form(class: 'ui form', role: 'form', action: "${suffix}/pets/*", method: 'POST') {
      div(class: 'field') {
        label(for: 'firstName', 'First Name:')
        input(id: 'firstName', name: 'firstName', type: 'text',
              placeholder: "${ownerProps.get('firstName')} ${ownerProps.get('lastName')}", 
              readonly: 'true')
      }
      div(class: 'field') {
        label(for: 'name', 'Name:')
        input(id: 'name', name: 'name', type: 'text', placeholder: 'Name')
      }
      div(class: 'field') {
        label(for: 'birthDate', 'Birth Date:')
        input(id: 'birthDate', name: 'birthDate', type: 'text', placeholder: 'dd/MM/yy')
      }
      div(class: 'grouped inline fields ui segment') {
        petTypesResource.listChildren().each { petTypeResource ->
          def petTypeProperties = petTypeResource.adaptTo(ValueMap.class)
          div(class: 'field') {
            div(class: 'ui radio checkbox') {
              input(type: 'radio', name: 'typeId', value: "${petTypeResource.getPath()}")
              label("${petTypeProperties.get('name')}")
            }
          }
        }
      }
      input(type: 'hidden', name: 'sling:resourceType', value: 'petclinic/pet')
      input(type: 'hidden', name: ':redirect',
      value: "/content/petclinic/en/owners.detail.html${ownerResource.getPath()}")
      input(type: 'hidden', name: '_charset_', value: 'UTF-8')
      button(type: 'submit', class: 'ui blue submit button', 'Save')
    }

Here, even the add form requires some dynamically generated form elements. The edit form on the other hand requires a bit more finesse.

    import org.apache.sling.api.resource.ValueMap

    def suffix = request.getRequestPathInfo().getSuffix()
    def resourceResolver = resource.getResourceResolver()
    def petResource = resourceResolver.getResource(suffix);
    def petProps = petResource.adaptTo(ValueMap.class);
    def ownerResource = petResource.getParent().getParent()
    def ownerProps = ownerResource.adaptTo(ValueMap.class)
    def petTypesResource = resourceResolver.getResource('/sling/content/petTypes')

    form(class: 'ui form', role: 'form', action: "${petResource.getPath()}", method: 'POST') {
      div(class: 'field') {
        label(for: 'firstName', 'First Name:')
        input(id: 'firstName', name: 'firstName', type: 'text',
              placeholder: "${ownerProps.get('firstName')} ${ownerProps.get('lastName')}", readonly: 'true')
      }
      div(class: 'field') {
        label(for: 'name', 'Name:')
        input(id: 'name', name: 'name', type: 'text', placeholder: 'Name',
              value: "${petResource.getName()}")
      }
      div(class: 'field') {
        label(for: 'birthDate', 'Birth Date:')
        input(id: 'birthDate', name: 'birthDate', type: 'text', placeholder: 'dd/MM/yy',
              value: "${petProps.get('birthDate')}")
      }
      div(class: 'grouped inline fields ui segment') {
        petTypesResource.listChildren().each { petTypeResource ->
          def petTypeProperties = petTypeResource.adaptTo(ValueMap.class)
          div(class: 'field') {
            div(class: 'ui radio checkbox') {
              if (petTypeResource.getPath() == petProps.get('typeId'))
                input(type: 'radio', name: 'typeId', value: "${petTypeResource.getPath()}", checked: 'checked')
              else
                input(type: 'radio', name: 'typeId', value: "${petTypeResource.getPath()}")
              label("${petTypeProperties.get('name')}")
            }
          }
        }
      }
      input(type: 'hidden', name: 'sling:resourceType', value: 'petclinic/pet')
      input(type: 'hidden', name: ':redirect', value: "/content/petclinic/en/owners.detail.html${ownerResource.getPath()}")
      input(type: 'hidden', name: '_charset_', value: 'UTF-8')
      button(type: 'submit', class: 'ui blue submit button', 'Save')
    }

The scripts implemented all share the same pattern, list views tend to differ slightly by creating tables instead of forms, but accessing the data and working with resources and value maps looks fairly the same. Decompiling static ```HTML``` into ```Groovy``` should just be a task requiring great diligence.