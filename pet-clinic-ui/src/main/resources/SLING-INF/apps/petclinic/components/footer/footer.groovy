markupBuilder.footer {
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