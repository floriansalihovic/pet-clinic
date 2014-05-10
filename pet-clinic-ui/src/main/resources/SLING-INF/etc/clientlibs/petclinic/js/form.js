;

(function () {
  $(document).ready(function () {
    $('.ui.checkbox').checkbox();
    $('.ui.radio.checkbox').checkbox();
    $(".ui.form").form({
      name: {
        identifier: 'name',
        rules: [
          {
            type: 'empty',
            prompt: 'Please enter pet name'
          }
        ]
      },
      birthDate: {
        identifier: 'birthDate',
        rules: [
          {
            type: 'empty',
            prompt: 'Please enter birth date'
          }
        ]
      },
      date: {
        identifier: 'date',
        rules: [
          {
            type: 'empty',
            prompt: 'Please date'
          }
        ]
      },
      description: {
        identifier: 'description',
        rules: [
          {
            type: 'empty',
            prompt: 'Please description'
          }
        ]
      }
    });
  });
}());