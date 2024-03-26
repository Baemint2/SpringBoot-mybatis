let currentSearchConditions = {
    prodName: null,
    nickname: null,
    startPrice: null,
    endPrice: null,
    pageSize: 6,
    category: null
}

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


function fetchAndDisplayPosts(page) {
    const {prodName, nickname, startPrice, endPrice, pageSize, category} = currentSearchConditions;
    let queryString = `page=${page}&pageSize=${pageSize}`;
    queryString += prodName ? `&prodName=${encodeURIComponent(prodName)}` : '';
    queryString += nickname ? `&nickname=${encodeURIComponent(nickname)}` : '';
    queryString += startPrice ? `&startPrice=${encodeURIComponent(startPrice)}` : '';
    queryString += endPrice ? `&endPrice=${encodeURIComponent(endPrice)}` : '';
    queryString += category ? `&categoryId=${encodeURIComponent(category)}` : '';

    fetch(`/api/v1/product/search?${queryString}`)
        .then(response => response.json())
        .then(data => {
            updateTableContent(data.products); // 상품 내용 업데이트
            console.log(data)
            updatePagination(data.totalPages, page, currentSearchConditions, pageSize); // 페이지네이션 업데이트
            history.pushState(null, '', `?${queryString}`);
        })
        .catch(error => console.error('Error fetching products', error));
}


function updateTableContent(products) {
    const productsListElement = document.getElementById('productList');
    productsListElement.innerHTML = '';

    const rowElement = document.createElement('div');
    rowElement.classList.add('row');
    products.forEach(product => {
        const productCard = createProductCard(product);
        rowElement.appendChild(productCard);
    });

    productsListElement.appendChild(rowElement);
}

function createProductCard(product) {
    const productElement = document.createElement('div');
    productElement.classList.add('col-md-4');

    const anchor = document.createElement('a');
    anchor.href = `/product/detail/${product.productId}`; // 여기에 클라이언트 측 URL을 사용하세요.
    anchor.classList.add('card-link'); // 필요에 따라 스타일링을 위한 클래스를 추가할 수 있습니다.

    console.log(product)
    const formattedNumber = new Intl.NumberFormat('ko-KR').format(product.prodPrice);
    anchor.innerHTML = `
        <div class="card mb-3">
            <img src="${product.storedUrl}" class="card-img-top" alt="${product.prodName}">
            <div class="card-body">
                <small>[${product.categoryName}]</small>
                <h5 class="card-title">${product.nickname}</h5>
                <p class="card-text">${product.prodName}</p>
                <p class="card-text">${formattedNumber}원</p>
                
            </div>
        </div>`;

    productElement.appendChild(anchor);

    return productElement;
}

function updatePagination(totalPages, currentPage, searchConditions, pageSize) {
    const paginationParent = document.querySelector('#pagination-parent');
    paginationParent.innerHTML = ''; // 이전 페이지네이션 요소 삭제

    const paginationContainer = document.createElement('ul');
    paginationContainer.className = 'pagination justify-content-center';

    // '이전' 버튼 생성
    const prevPage = currentPage - 1;
    createPaginationButton(prevPage >= 1, prevPage, '이전', paginationContainer, currentPage === prevPage, searchConditions, pageSize);

    // 페이지 번호 버튼 생성
    for (let i = 1; i <= totalPages; i++) {
        createPaginationButton(true, i, i.toString(), paginationContainer, currentPage === i, searchConditions, pageSize);
    }

    // '다음' 버튼 생성
    const nextPage = currentPage + 1;
    createPaginationButton(nextPage <= totalPages, nextPage, '다음', paginationContainer, currentPage === nextPage, searchConditions, pageSize);

    paginationParent.appendChild(paginationContainer);
}

function createPaginationButton(isActive, pageNumber, text, container, isCurrentPage, searchConditions, pageSize) {
    const li = document.createElement("li");
    li.className = `page-item ${!isActive ? 'disabled' : ''} ${isCurrentPage ? 'active' : ''}`;

    console.log(searchConditions);

    const link = document.createElement('a');
    link.className = 'page-link';
    link.href = '#';
    link.innerText = text;
    if (isActive) {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            if (!isCurrentPage) {
                fetchAndDisplayPosts(pageNumber, currentSearchConditions.prodName, currentSearchConditions.nickname, currentSearchConditions.startPrice, currentSearchConditions.endPrice, pageSize);
            }
        });
    }

    li.appendChild(link);
    container.appendChild(li);
}

function changeSearchType() {
    const searchType = document.getElementById("searchType").value;
    const searchField = document.getElementById("searchField");
    const priceRangeInputs = document.getElementById("priceRangeInputs");

    // 기존 검색 필드 초기화
    searchField.innerHTML = '';
    searchField.placeholder = '검색어 입력';

    if (searchType === "productName") {
        searchField.innerHTML = `상품명: <input type="text" id="productName">`; // ID 값을 변경
        priceRangeInputs.style.display = 'none';
    } else if (searchType === "seller") {
        searchField.innerHTML = `판매자: <input type="text" id="seller">`; // ID 값을 변경
        priceRangeInputs.style.display = 'none';
    } else if (searchType === "priceRange") {
        priceRangeInputs.style.display = 'block';
    }
}

document.getElementById("searchButton").addEventListener("click", function () {
    updateSearchConditions();
    fetchAndDisplayPosts(1);
    console.log(currentSearchConditions.productName);
});

function updateSearchConditions() {
    const searchType = document.getElementById("searchType").value;

    currentSearchConditions = {
        prodName: null,
        nickname: null,
        startPrice: null,
        endPrice: null,
        pageSize: 6,
        category: null
    };

    if (searchType === "productName") {
        currentSearchConditions.prodName = document.getElementById('productName').value;
    } else if (searchType === "seller") {
        currentSearchConditions.nickname = document.getElementById("seller").value;
    } else if (searchType === "priceRange") {
        currentSearchConditions.startPrice = document.getElementById("startPrice").value;
        currentSearchConditions.endPrice = document.getElementById("endPrice").value;
    }
}



const categoriesListElement = document.getElementById("categoryList");
categories.forEach(category => {
    const categoryItem = document.createElement("li");
    const categoryLink = document.createElement("p");
    categoryLink.textContent = category.name;

    categoryItem.appendChild(categoryLink);

    const subCategoriesList = document.createElement("ul");
    category.subCategories.forEach(subCategory => {
        const subCategoryItem = document.createElement("li");
        const subCategoryLink = document.createElement("a");
        subCategoryLink.href = "javascript:void(0);";
        subCategoryLink.textContent = subCategory.name;

        subCategoryLink.addEventListener('click', () => {
            currentSearchConditions.category = subCategory.id;
            fetchAndDisplayPosts(1);
        })

        categoryLink.addEventListener('click', () => {
            updateSearchConditions("category", category.id);
            fetchAndDisplayPosts(1);
        });

        subCategoryItem.appendChild(subCategoryLink);
        subCategoriesList.appendChild(subCategoryItem);
    })

    categoryItem.appendChild(subCategoriesList);
    categoriesListElement.appendChild(categoryItem);
})


document.addEventListener('DOMContentLoaded', () => fetchAndDisplayPosts(1, null, null, null, null, 6, null));

