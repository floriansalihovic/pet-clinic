import org.apache.sling.api.resource.ValueMap
import groovy.xml.MarkupBuilder

// getting the user session based resource resolver
def resourceResolver = resource.getResourceResolver()
// accessing the node containing the owners
ownerResource = resourceResolver.getResource(request.getRequestPathInfo().getSuffix())
ownerProps = ownerResource.adaptTo(ValueMap.class)

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
          a(href: "${resource.getPath()}.edit.html", class: 'small ui right floated button blue',
              style: 'float: right; margin-left: 1em;', 'Add Owner')
        }
      }
      table(class: 'ui table segment') {
        tbody {
          tr {
            th 'Name'
            td {
              i(class: 'user icon', '')
              span "${ownerProps.get('firstName')} ${ownerProps.get('lastName')}"
            }
          }
          tr {
            th 'Address'
            td {
              i(class: 'map marker icon', '')
              span "${ownerProps.get('city')}, ${ownerProps.get('address')}"
            }
          }
          tr {
            th 'Telephone'
            td {
              i(class: 'phone icon', '')
              span "${ownerProps.get('telephone')}"
            }
          }
        }
      }

      div(class: 'ui grid') {
        div(class: 'twelve wide column') {
          h2(class: 'ui header', 'Pets and Visits')
        }
        div(class: 'four wide column') {
          a(class: 'small ui right floated button green', href: '#', 'Add Pet')
        }
      }

      table(class: 'ui table segment') {
        thead {
          tr {
            th(style: 'width: 25%', 'Name')
            th(style: 'width: 25%', 'Type')
            th(style: 'width: 25%', 'Visit Date')
            th(style: 'width: 25%', 'Description')
          }
        }
        tbody {
          tr {
            td(style: 'vertical-align: top') {
              a(href: '#', 'Janny')
            }
            td(style: 'vertical-align: top', 'Hamster')
            td(colspan: '2', style: 'padding: 0') {
              table(class: 'ui table small', style: 'width: 100%') {
                tbody {
                  tr {
                    td(colspan: '2') {
                      a(class: 'mini ui green button', href: '#', 'Add Visit')
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

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
