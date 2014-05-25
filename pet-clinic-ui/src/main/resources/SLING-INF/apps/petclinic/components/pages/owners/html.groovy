import io.github.floriansalihovic.petclinic.search.PetOwnerSearchService

def searchService = request.adaptTo(PetOwnerSearchService.class)
def petOwnersIterator = searchService ? searchService.findPetOwners() : null

sling.include(resource, 'petclinic/components/header')
sling.include(resource, 'petclinic/components/navigation')

markupBuilder.html {
  body {
    div(class: 'container') {
      div(class: 'ui grid') {
        div(class: 'seven wide column') {
          h1(class: 'ui header', 'Owners')
        }
        div(class: 'nine wide column') {
          div(class: 'ui icon input', style: 'float: right; margin-left: 1em;') {
            form(method: 'GET', action: "${resource.getPath()}.html") {
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
          if (petOwnersIterator) {
            petOwnersIterator.each() { petOwner ->
              tr {
                td {
                  a(href: "${resource.getPath()}.detail.html${petOwner.getPath()}",
                      "${petOwner.getFirstName()} ${petOwner.getLastName()}")
                }
                td(petOwner.getCity())
                td(petOwner.getAddress())
                td(petOwner.getTelephone())
                td {
                  div {
                    petOwner.getPetNames().each() { name ->
                      span(class: 'ui small label teal', "${name}")
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

sling.include(resource, 'petclinic/components/footer')
