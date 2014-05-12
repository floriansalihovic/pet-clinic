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
      sling.include(resource, 'petclinic/components/visits')
    }
  }
}

sling.include(resource, 'petclinic/components/footer')