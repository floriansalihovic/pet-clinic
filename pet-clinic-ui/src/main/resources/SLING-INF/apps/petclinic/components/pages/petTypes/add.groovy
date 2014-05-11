def resourceResolver = resource.getResourceResolver()
def petTypesResource = resourceResolver.getResource('/sling/content/petTypes')

sling.include(resource, 'petclinic/components/header')
sling.include(resource, 'petclinic/components/navigation')

markupBuilder.html {
  body {
    div(class: 'container') {
      div(class: 'ui grid') {
        div(class: 'seven wide column') {
          h1(class: 'ui header', 'Add Pet Type')
        }
      }
      form(class: 'ui form', role: 'form', action: "${petTypesResource.getPath()}/*", method: 'POST') {
        div(class: 'field') {
          label(for: 'name', 'Name')
          input(id: 'name', name: 'name', type: 'text')
        }
        input(type: 'hidden', name: 'sling:resourceType', value: 'petclinic/components/pages/petTypes/petType')
        input(type: 'hidden', name: ':redirect', value: "${resource.getPath()}.html")
        input(type: 'hidden', name: '_charset_', value: 'UTF-8')
        button(type: 'submit', class: 'ui blue submit button', 'Save')
      }
    }
  }
}

sling.include(resource, 'petclinic/components/footer')