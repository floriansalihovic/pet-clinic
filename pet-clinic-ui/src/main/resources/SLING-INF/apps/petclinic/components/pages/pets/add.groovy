import groovy.xml.MarkupBuilder
import org.apache.sling.api.resource.ValueMap

// the suffix pointing to an owner resource
def suffix = request.getRequestPathInfo().getSuffix()
// the user session based resource resolver
def resourceResolver = resource.getResourceResolver()
// the owner resource
def ownerResource = resourceResolver.getResource(suffix)
// accessing the owner's properties by adapting the resource to a ValueMap.
def ownerProps = ownerResource.adaptTo(ValueMap.class)

def petTypesResource = resourceResolver.getResource('/sling/content/petTypes')

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
          h1(class: 'ui header', 'Add Pet')
        }
      }
      form(class: 'ui form', role: 'form', action: "${suffix}/pets/*", method: 'POST') {
        div(class: 'field') {
          label(for: 'firstName', 'First Name:')
          input(id: 'firstName', name: 'firstName', type: 'text',
              placeholder: "${ownerProps.get('firstName')} ${ownerProps.get('lastName')}", readonly: 'true')
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
        input(type: 'hidden', name: 'sling:resourceType', value: 'petclinic/components/pages/owners/pets')
        input(type: 'hidden', name: ':redirect',
            value: "/content/petclinic/en/owners.detail.html${ownerResource.getPath()}")
        input(type: 'hidden', name: '_charset_', value: 'UTF-8')
        button(type: 'submit', class: 'ui blue submit button', 'Save')
      }
    };
    div(class: 'ui divider') { mkp.yield('') };
    div(class: 'ui divided horizontal footer link list') {
      div(class: 'item') { mkp.yieldUnescaped '&copy; 2014 Florian Salihovic' }
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
