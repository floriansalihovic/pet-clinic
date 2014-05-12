markupBuilder.form(class: 'ui form', role: 'form', action: '/sling/content/owners/*', method: 'POST') {
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
