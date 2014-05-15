## Groovy scripts and components

The last part of the guide gave an overview about how Groovy can be used to implement scripts. To take the step a bit further and provide a more DRY approach building views. All script share a pattern, which should be able to produce components which are just thrown into a page and work.

The first task would be providing a binding to a groovy.xml.MarkupBuilder instance, which is shared over the components and be mutated by the components.

In the ```pet-clinic-core``` a Service is added.

    package io.github.floriansalihovic.petclinic.scripting;
    
    import groovy.xml.*;
    import org.apache.felix.scr.annotations.*;
    import org.apache.sling.scripting.api.*;
    import org.slf4j.*;
    
    import javax.script.*;
    import java.io.*;
    
    @Component
    @Service
    public class MarkupBuilderBindingsValuesProvider implements BindingsValuesProvider {
    
        private final static Logger logger = LoggerFactory.getLogger(MarkupBuilderBindingsValuesProvider.class);
    
        @Override
        public void addBindings(Bindings bindings) {
            final PrintWriter out = (PrintWriter) bindings.get("out");
            if (null == out) {
                logger.error("Expected print writer is not available.");
            } else {
                logger.info("Providing binding markupBuilder:{}.", MarkupBuilder.class.getName());
                bindings.put("markupBuilder", new MarkupBuilder(out));
            }
        }
    }

This service will provide a binding in all scripts invoked injecting ```markupBuilder``` and make it available to work with it.

    markupBuilder.html {
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

Slicing this page into several components would reduce the amount of code to be written on the pages and provide a nice modular view on things. In the ```pet-clinic-ui``` bundle, new components are added by adding scripts just like the pages to the ```/apps/petclinic/components``` folder. The component's being added are

- ```/apps/petclinic/components/head``` for the page's head element
- ```/apps/petclinic/components/navigation``` for the naviagtion in the page
- ```/apps/petclinic/components/footer``` for the footer part of the page

For ```/apps/petclinic/components/head```, ```head.groovy``` is added in the new folder. 

    markupBuilder.head {
        meta(charset: 'UTF-8')
        meta(content: 'IE=edge,chrome=1', 'http-equiv': 'X-UA-Compatible');
        meta(name: 'viewport', content: 'width=device-width, initial-scale=1.0')
        link(href: '/etc/clientlibs/petclinic/css/default.css', rel: 'stylesheet')
        link(href: '/etc/clientlibs/petclinic/css/semantic.min.css', rel: 'stylesheet')
        script('', type: 'text/javascript', src: '/etc/clientlibs/petclinic/js/jquery-1.11.0.min.js')
        script('', type: 'text/javascript', src: '/etc/clientlibs/petclinic/js/semantic.min.js')
        script('', type: 'text/javascript', src: '/etc/clientlibs/petclinic/js/form.js')
      }

The component for the navigation, placed in ```navigation.groovy``` will produce slightly different markup using the ```nav``` element to wrap the elements in the navigation.

    markupBuilder.nav {
      div(class: 'ui menu teal inverted') {
        a(class: 'title item', href: '/content/petclinic/en/owners.html') {
          strong('Pet Clinic')
        }
        a(class: 'item', href: '/content/petclinic/en/owners.html', 'Find Owners')
        a(class: 'item', href: '/content/petclinic/en/vets.html', 'Veterinarians')
        a(class: 'item', href: '/content/petclinic/en/specialities.html', 'Specialities')
        a(class: 'item', href: '/content/petclinic/en/petTypes.html', 'Pet Types')
      }
    }

Similar to the ```nav``` element used to wrap the navigation, the ```footer``` element will wrap the elements for the footer.

    markupBuilder.footer {
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

Using the ```sling.include``` to include the components in the page(s), the code will be reduced to a minimum.

    def resourceResolver = resource.getResourceResolver()
    def ownersResource = resourceResolver.getResource('/sling/content/owners')
    
    sling.include(resource, 'petclinic/components/header')
    sling.include(resource, 'petclinic/components/navigation')
    
    markupBuilder.html {
      body {
        div(class: 'container') {
          div(class: 'ui grid') {
            div(class: 'seven wide column') {
              h1(class: 'ui header', 'Owners')
            }
          }
        }
      }
    }
    
    sling.include(resource, 'petclinic/components/footer')

Now, the basic template for a page is reduced to a minimum.




