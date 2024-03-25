document.addEventListener('DOMContentLoaded', function() {
    fetch('/api/v1/categories')
        .then(response => response.json())
        .then(data => {
            const listElement = document.getElementById('categoryList');
            createCategoryList(listElement, data);
            console.log(data);

        })
        .catch(error => console.error('Error loading category data:', error));
});

function createCategoryList(parentElement, categories) {
    const ul = document.createElement('ul');
    ul.className = 'category-list';
    categories.forEach(category => {
        const li = document.createElement('li');
        li.textContent = category.categoryName;
        li.className = 'category-item'; // 클래스 이름 추가
        if (category.subCategories && category.subCategories.length > 0) {
            createCategoryList(li, category.subCategories);
        }
        ul.appendChild(li);
    });
    parentElement.appendChild(ul);
}
