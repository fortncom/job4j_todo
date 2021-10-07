function loadCategory() {
    $.ajax({
        cache: false,
        type: 'GET',
        url: '/todo/category.do',
        dataType: 'json'
    }).done(function(data) {
        let categoryName = "";
        let result = "<select class=\"form-control\" id=\"categorySelection\" name=\"category\" multiple>" +
        "<option selected disabled value=\"\">Выберите категорию</option>";
        for (var category in data) {
            result += '<option value=\"' + data[category].id + '\">' + data[category].name + '</option>';
            categoryName += "<input type=\"hidden\" value=\"" + data[category].name + "\" "
                + "id=\"categoryName"+ data[category].id + "\"/>";
        }
        result += "</select>";
        document.getElementById('categorySelection').innerHTML = result;
        document.getElementById('hiddenCategoryName').innerHTML = categoryName;
    }).fail(function(err){
        alert("Error:" + err);
    });
};

function addCategory() {
    let category = $('#category1').val();
    if (validate()) {
        $.ajax({
            cache: false,
            type: 'POST',
            url: '/todo/category.do',
            data: JSON.stringify(category),
            contentType: 'application/json',
        }).done(function() {
            loadCategory();
        }).fail(function(err){
            alert("Error:" + err);
        });
    }
};

function validate(category) {
    if (category === "") {
        alert("Необходимо заполнить поле -Новая категория");
        return false;
    }
    return true;
};

$(document).ready(function () {
    loadCategory();
});