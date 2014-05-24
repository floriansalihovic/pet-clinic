def resourceResolver = resource.getResourceResolver()
def specialitiesResource = resourceResolver.getResource('/sling/content/specialities')

markupBuilder.form(class: 'ui form', role: 'form', action: "${specialitiesResource.getPath()}/*", method: 'POST') {
  div(class: 'field') {
    label(for: 'name', 'Name')
    input(id: 'name', name: 'name', type: 'text')
  }
  input(type: 'hidden', name: 'sling:resourceType', value: 'petclinic/speciality')
  input(type: 'hidden', name: ':redirect', value: "${resource.getPath()}.html")
  input(type: 'hidden', name: '_charset_', value: 'UTF-8')
  button(type: 'submit', class: 'ui blue submit button', 'Save')
}
