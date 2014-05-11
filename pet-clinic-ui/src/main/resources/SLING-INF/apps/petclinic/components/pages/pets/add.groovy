import org.apache.sling.api.resource.ValueMap

def suffix = request.getRequestPathInfo().getSuffix()
def resourceResolver = resource.getResourceResolver()
def ownerResource = resourceResolver.getResource(suffix)
def ownerProps = ownerResource.adaptTo(ValueMap.class)
def petTypesResource = resourceResolver.getResource('/sling/content/petTypes')

sling.include(resource, 'petclinic/components/header')
sling.include(resource, 'petclinic/components/navigation')

markupBuilder.html {
  body {
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

sling.include(resource, 'petclinic/components/footer')