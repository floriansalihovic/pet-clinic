import org.apache.sling.api.resource.ValueMap
import groovy.xml.MarkupBuilder

// getting the user session based resource resolver
def resourceResolver = resource.getResourceResolver()
// accessing the node containing the owners
ownersResource = resourceResolver.getResource('/sling/content/owners')

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
          h1(class: 'ui header', 'Owners')
        }
        div(class: 'nine wide column') {
          div(class: 'ui icon input', style: 'float: right; margin-left: 1em;') {
            form(method: 'GET') {
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
          ownersResource.listChildren().each { resource ->
            properties = resource.adaptTo(ValueMap.class)
            tr {
              td {
                a(href: '#', "${properties.get('firstName')} ${properties.get('lastName')}")
              }
              td(properties.get('city'))
              td(properties.get('address'))
              td(properties.get('telephone'))
              td {
                div {
                  span(class: 'ui small label teal', 'Janny')
                  span(class: 'ui small label teal', 'Leo')
                  span(class: 'ui small label teal', 'Shaka')
                }
              }
            }
          }
        }
      }
      div(class: 'ui divider')
      // todo: create component. -->
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
}
