const wishlist = {
    init: function () {
        this.bindEvents();

        document.getElementById('show-wishlist').addEventListener("click", function () {
            wishlist.fetchWishlist();
        })
    },
    bindEvents: function() {
        const wishlistToggleBtn = document.getElementById('wishlist-toggle');
        if(wishlistToggleBtn) {
            wishlistToggleBtn.addEventListener('click', () => {
                const productId = wishlistToggleBtn.getAttribute('data-product-id');
                this.toggleWishlist(productId, wishlistToggleBtn);
            });
        }
    },

    fetchWishlist: function () {
        fetch('/api/v1/wishlist', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => response.json())
            .then(data => {
                this.displayWishlist(data);
            })
            .catch(error => console.error('Error:', error));
    },
    displayWishlist: function (wishlistItems) {
        const wishlistContainer = document.getElementById('wishlistContainer');
        if (wishlistContainer) {
            wishlistContainer.innerHTML = ''; // 기존 내용을 초기화
            const list = document.createElement('table');
            list.classList.add('table');

            // 테이블 바디 생성
            const tbody = document.createElement('tbody');
            wishlistItems.forEach(item => {
                const row = document.createElement('tr');
                row.innerHTML = `
            <td>
                <a href="/product/detail/${item.productId}" class="wishlist-item-link">
                    <img src="${item.imageDto.storedUrl}" alt="${item.prodName}" class="img-thumbnail" style="width: 100px; height: 100px;">
                </a>
            </td>
            <td>
                <a href="/product/detail/${item.productId}" class="wishlist-item-link">
                    <div>${item.productDto.prodName}</div>
                    <div>${item.productDto.prodPrice}원</div>
                </a>
            </td>
            <td>
                <button class="btn-remove" data-product-id="${item.productId}">찜 해제</button>
            </td>
            `;
                // 찜 해제 버튼에 클릭 이벤트 리스너 추가
                const btnRemove = row.querySelector('.btn-remove');
                btnRemove.addEventListener('click', (e) => {
                    e.preventDefault(); // 기본 이벤트를 취소 (링크 이동 방지)
                    e.stopPropagation(); // 이벤트 전파를 중단 (상위 링크로의 이벤트 전파 방지)
                    this.toggleWishlist(item.productId);
                });

                tbody.appendChild(row);
            });
            list.appendChild(tbody);

            wishlistContainer.appendChild(list); // 생성된 리스트를 위시리스트 컨테이너에 추가
        }
    },

    toggleWishlist: function (productId, button) {
        fetch(`/api/v1/wishlist/toggle/${productId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => response.json())
            .then(data => {
                if(button) {
                    this.updateWishlistButton(button, data);
                }
                this.fetchWishlist();
            })
            .catch(error => console.error('Error:', error));
    },
    updateWishlistButton: function (button, data) {
        button.setAttribute('data-liked', data);
        if (data) {
            button.innerHTML = `<i class="fa-solid fa-heart"></i>`
        } else {
            button.innerHTML = `<i class="fa-regular fa-heart"></i>`
        }
    }
};

document.addEventListener("DOMContentLoaded", function () {
    wishlist.init();
});
