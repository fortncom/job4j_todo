function validate(description) {
    let answer = "Описание задачи должно быть заполнено";
    if (description === '') {
    alert(answer);
    return false;
    }
    return true;
}

$(document).ready(function () {
showAll();
});