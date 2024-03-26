const categories = [
    {
        id: 1, name: '의류', subCategories: [
            {id: 13, name: '남성복'},
            {id: 14, name: '여성복'}
        ]
    },
    {
        id: 2, name: '전자제품', subCategories: [
            {id: 10, name: '노트북'},
            {id: 11, name: '태블릿'},
            {id: 12, name: '스마트워치'}
        ]
    },
    {
        id: 3, name: '잡화', subCategories: [
            {id: 15, name: '다이어리'},
            {id: 16, name: '휴대폰케이스'}
        ]
    },
    {
        id: 4, name: '악세서리', subCategories: [
            {id: 6, name: '귀걸이'},
            {id: 7, name: '목걸이'},
            {id: 8, name: '팔찌'},
            {id: 9, name: '반지'}
        ]
    }
];

document.addEventListener("DOMContentLoaded", function () {
    const majorCategorySelect = document.getElementById("majorCategory");
    const mediumCategorySelect = document.getElementById("mediumCategory");

    categories.forEach(category => {
        const option = new Option(category.name, category.id);
        majorCategorySelect.add(option);
    })

    majorCategorySelect.onchange = function() {
    mediumCategorySelect.innerHTML = '<option value="">중분류 선택</option>';

        const selectedCategory = categories.find(category => category.id === parseInt(this.value, 10));
        if (selectedCategory) {
            selectedCategory.subCategories.forEach(sub => {
                console.log(sub)
                const option = new Option(sub.name, sub.id);
                mediumCategorySelect.add(option);
            });
        }
    }
})