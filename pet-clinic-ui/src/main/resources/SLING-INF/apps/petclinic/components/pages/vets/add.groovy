import groovy.xml.MarkupBuilder
import org.apache.sling.api.resource.ValueMap

// getting the user session based resource resolver
def resourceResolver = resource.getResourceResolver()
// accessing the resource containing the veterinarians
def veterinariansResource = resourceResolver.getResource('/sling/content/veterinarians')
// accessing the specialities available.
def specialitiesResource = resourceResolver.getResource('/sling/content/specialities')

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
          h1(class: 'ui header', 'Add Veterinarian')
        }
      }
      form(class: 'ui form', role: 'form', action: "${veterinariansResource.getPath()}/*", method: 'POST') {
        div(class: 'field') {
          label(for: 'name', 'First Name:')
          input(id: 'firstName', name: 'firstName', type: 'text', placeholder: 'First Name')
        }
        div(class: 'field') {
          label(for: 'name', 'Last Name:')
          input(id: 'lastName', name: 'lastName', type: 'text', placeholder: 'Last Name')
        }
        div(class: 'grouped inline fields ui segment') {
          specialitiesResource.listChildren().each { specialityResource ->
            def specialityProps = specialityResource.adaptTo(ValueMap.class)
            div(class: 'field') {
              div(class: 'ui checkbox') {
                input(type: 'checkbox', name: 'specialities', value: "${specialityResource.getPath()}")
                label("${specialityProps.get('name')}")
              }
            }
          }
        }
        input(type: 'hidden', name: 'specialities@TypeHint', value: 'String[]')
        input(type: 'hidden', name: "specialities@Delete", value: 'delete specialities')
        input(type: 'hidden', name: 'sling:resourceType',
            value: 'petclinic/components/pages/veterinarians/veterinarian')
        input(type: 'hidden', name: ':redirect', value: "${resource.getPath()}.html")
        input(type: 'hidden', name: '_charset_', value: 'UTF-8')
        button(type: 'submit', class: 'ui blue submit button', 'Save')
      }
    }
    div(class: 'ui divider') {
      mkp.yield('')
    };
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