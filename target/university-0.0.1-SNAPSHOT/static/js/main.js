$('document').ready(function () {
    console.log("it works")
    $('.table .edit').on('click', function (event) {
        event.preventDefault();
        const href = $(this).attr('href');
        const modal = $('editModal');

        console.log(modal.find('.editForm #groupId').val(groupId))
        $.get(href, function (student, status) {
            let birthDateArr = student.birthDate
            const stringYear = birthDateArr[0].toString();
            const stringDay = birthDateArr[1].toString();
            const stringMonth = birthDateArr[2].toString();
            const date = new Date(stringYear + "-" + stringDay + "-" + stringMonth);
            const day = ("0" + date.getDate()).slice(-2);
            const month = ("0" + (date.getMonth() + 1)).slice(-2);
            const birthDate = (date.getFullYear() + '-' + month + '-' + day);

            $('.editForm #id').val(student.id)
            $('.editForm #groupId').val(student.groupId)
            $('#firstNameEdit').val(student.firstName)
            $('#lastNameEdit').val(student.lastName)
            $('#birthDate').val(birthDate)
            $('#addressEdit').val(student.address)
            $('#emailEdit').val(student.email)
            $('#phoneEdit').val(student.phone)
            $('#genderEdit').val(student.gender)
            $('#facultyEdit').val(student.faculty)
            $('#courseEdit').val(student.course)
        });

        $('#editModal').modal();
    });

    $('.table .details').on('click', function (event) {
        event.preventDefault();
        const href = $(this).attr('href');
        const modal = $('details');

        $.get(href, function (student, status) {
            let birthDateArr = student.birthDate
            const stringYear = birthDateArr[0].toString();
            const stringDay = birthDateArr[1].toString();
            const stringMonth = birthDateArr[2].toString();
            const date = new Date(stringYear + "-" + stringDay + "-" + stringMonth);
            const day = ("0" + date.getDate()).slice(-2);
            const month = ("0" + (date.getMonth() + 1)).slice(-2);
            const birthDate = (date.getFullYear() + '-' + month + '-' + day);
            $('.details #id').val(student.id)
            $('.firstName').text(student.firstName)
            $('.lastName').text(student.lastName)
            $('#birthDate').val(birthDate)
            $('#addressEdit').val(student.address)
            $('.email').text(student.email)
            $('#phoneEdit').val(student.phone)
            $('#genderEdit').val(student.gender)
            $('#facultyEdit').val(student.faculty)
            $('#courseEdit').val(student.course)
        });
         $('#details').modal();
    });
});