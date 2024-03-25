let currentSearchConditions = {
    prodName: null,
    nickname: null,
    startPrice: null,
    endPrice: null,
    pageSize: 6
}

function fetchAndDisplayPosts(page) {
    const {prodName, nickname, startPrice, endPrice, pageSize} = currentSearchConditions;
    let queryString = `page=${page}&pageSize=${pageSize}`;
    queryString += prodName ? `&prodName=${encodeURIComponent(prodName)}` : '';
    queryString += nickname ? `&nickname=${encodeURIComponent(nickname)}` : '';
    queryString += startPrice ? `&startPrice=${encodeURIComponent(startPrice)}` : '';
    queryString += endPrice ? `&endPrice=${encodeURIComponent(endPrice)}` : '';

    fetch(`/api/v1/product/search?${queryString}`)
        .then(response => response.json())
        .then(data => {
            console.log(data)
            updateTableContent(data.products); // 상품 내용 업데이트
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
        console.log(product)
        const productCard = createProductCard(product);
        rowElement.appendChild(productCard);
    });

    productsListElement.appendChild(rowElement);
}

function createProductCard(product) {
    console.log(product)
    const productElement = document.createElement('div');
    productElement.classList.add('col-md-4');

    const anchor = document.createElement('a');
    anchor.href = `/product/detail/${product.productId}`; // 여기에 클라이언트 측 URL을 사용하세요.
    anchor.classList.add('card-link'); // 필요에 따라 스타일링을 위한 클래스를 추가할 수 있습니다.

    anchor.innerHTML = `
        <div class="card mb-3">
            <img src="${product.storedUrl}" class="card-img-top" alt="${product.prodName}">
            <div class="card-body">
                <h5 class="card-title">${product.nickname}</h5>
                <p class="card-text">${product.prodName}</p>
                <p class="card-text">${product.prodPrice}원</p>
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
document.getElementById("searchButton").addEventListener("click", function() {
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
        pageSize: 6
    };

    if (searchType === "productName") {
        currentSearchConditions.productName =  document.getElementById('productName').value;
    } else if (searchType === "seller") {
        currentSearchConditions.sellerName = document.getElementById("seller").value;
    } else if (searchType === "priceRange") {
        currentSearchConditions.startPrice = document.getElementById("startPrice").value;
        currentSearchConditions.endPrice = document.getElementById("endPrice").value;
    }
}

document.addEventListener('DOMContentLoaded', () => fetchAndDisplayPosts(1, null, null, null, null, 6));

