const wishlist = {
    init: function () {
        this.bindEvents();
        this.fetchWishlist();
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
        if(wishlistContainer) {
            wishlistContainer.innerHTML = ''; // 기존 목록을 초기화
            wishlistItems.forEach(item => {
                const element = document.createElement('div');
                element.classList.add('wishlist-item');
                element.innerHTML = `
                    <img src="${item.storedUrl}" alt="${item.prodName}" class="img-thumbnail" style="width: 100px; height: 100px;">
                    <div>
                        <h5>${item.prodName}</h5>
                        <p>${item.prodPrice}원</p>
                        <button class="btn-remove" data-product-id="${item.productId}">찜 해제</button>
                    </div>
                `;
                element.querySelector('.btn-remove').addEventListener('click', () => {
                    this.toggleWishlist(item.productId);
                });

                wishlistContainer.appendChild(element);
            });
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
            button.textContent = 'Remove from Wishlist';
        } else {
            button.textContent = 'Add to Wishlist';
        }
    }
};

document.addEventListener("DOMContentLoaded", function () {
    wishlist.init();
});
