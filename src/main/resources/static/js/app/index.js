function fetchAndDisplayPosts(page) {
    const pageSize = 6; // 페이지 당 상품 수 설정
    fetch(`/api/v1/product/list?page=${page}&pageSize=${pageSize}`)
        .then(response => response.json())
        .then(data => {
            updateTableContent(data.products); // 상품 내용 업데이트
            updatePagination(data.totalPages, page); // 페이지네이션 업데이트
            history.pushState(null, '', `?page=${page}&pageSize=${pageSize}`)
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
                <h5 class="card-title">${product.prodName}</h5>
                <p class="card-text">${product.nickname}</p>
                <p class="card-text">${product.prodPrice}</p>
            </div>
        </div>`;

    productElement.appendChild(anchor);

    return productElement;
}
function updatePagination(totalPages, currentPage) {
    const paginationParent = document.querySelector('#pagination-parent');
    paginationParent.innerHTML = ''; // 이전 페이지네이션 요소 삭제

    const paginationContainer = document.createElement('ul');
    paginationContainer.className = 'pagination justify-content-center';

    // '이전' 버튼 생성
    const prevPage = currentPage - 1;
    createPaginationButton(prevPage >= 1, prevPage, '이전', paginationContainer);

    // 페이지 번호 버튼 생성
    for (let i = 1; i <= totalPages; i++) {
        createPaginationButton(true, i, i.toString(), paginationContainer, currentPage === i);
    }

    // '다음' 버튼 생성
    const nextPage = currentPage + 1;
    createPaginationButton(nextPage <= totalPages, nextPage, '다음', paginationContainer);

    paginationParent.appendChild(paginationContainer);
}

function createPaginationButton(isActive, pageNumber, text, container, isCurrentPage = false) {
    const li = document.createElement("li");
    li.className = `page-item ${!isActive ? 'disabled' : ''} ${isCurrentPage ? 'active' : ''}`;

    const link = document.createElement('a');
    link.className = 'page-link';
    link.href = '#';
    link.innerText = text;
    if (isActive) {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            if (!isCurrentPage) {
                fetchAndDisplayPosts(pageNumber);
            }
        });
    }

    li.appendChild(link);
    container.appendChild(li);
}

// updateTableContent 함수 및 createProductCard 함수는 변경 없이 사용

document.addEventListener('DOMContentLoaded', () => fetchAndDisplayPosts(1));
