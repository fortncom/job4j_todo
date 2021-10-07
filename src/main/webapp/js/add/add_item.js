function addNewItem() {
    let description = $('#formForDefinition1').val();
    let categories = [];
    let id = $('#categorySelection').val();
    for (const idEl of id) {
        let name = $('#categoryName' + idEl).val();
        categories.push({id: idEl, name: name});
    }
    let arr = {description: description, categories: categories};

    if (validate(description, categories)) {
        $.ajax({
            cache: false,
            type: 'POST',
            url: '/todo/index.do',
            data: JSON.stringify(arr),
            contentType: 'application/json'
        }).done(function(){
            window.location.href='http://localhost:8080/todo/welcome/index.html';
        }).fail(function(err){
            alert("Error:" + err);
        })
    }
};
