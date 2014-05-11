import org.apache.sling.api.resource.ValueMap

def suffix = request.getRequestPathInfo().getSuffix()
def resourceResolver = resource.getResourceResolver()
def ownerResource = resourceResolver.getResource(suffix)
def ownerProps = ownerResource.adaptTo(ValueMap.class)

sling.include(resource, 'petclinic/components/header')
sling.include(resource, 'petclinic/components/navigation')

markupBuilder.html {
  body {
    div(class: 'container') {
      div(class: 'ui grid') {
        div(class: 'seven wide column') {
          h1(class: 'ui header', 'Add Owner')
        }
      }
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
    }
  }
}

sling.include(resource, 'petclinic/components/footer')