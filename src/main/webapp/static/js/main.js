$('document').ready(function () {
    console.log("it works")
    $('.table .editButton').on('click', function (event) {
        console.log("it works")
        event.preventDefault();


        var href = $(this).attr('href');

        $.get(href, function (student, status) {
            $('#groupIdEdit').val(student.groupId)
            $('#firstNameEdit').val(student.firstName)
            $('#lastNameEdit').val(student.lastName)
            $('#birthDateEdit').val(student.birthDate)
            $('#addressEdit').val(student.address)
            $('#facultyEdit').val(student.faculty)
            $('#courseEdit').val(student.course)
            console.log(student.groupId)
            console.log(student.firstName)
        })

        $('#editModal').modal();
    });
});