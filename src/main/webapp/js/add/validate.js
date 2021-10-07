function validate(description, categories) {
    const answer = "Пожалуйста ";
    let result = answer;
    if (description === '') {
        result += "-заполните описание задачи ";
    }
    if (categories.length === 0) {
        result += "-выберите категорию ";
    }
    if (answer !== result) {
        alert(result);
        return false;
    }
    return true;
};

