const cart = {
    init: function () {
        const _this = this;

        document.getElementById("btn-cart")?.addEventListener("click", function () {
            _this.addItems();
        });
    },

    updateStockQuantity: function () {
        // 모든 제품 ID를 포함하는 요소를 선택
        const productIds = document.querySelectorAll('.productId');
        console.log(productIds);
        // 각 제품에 대해 반복
        productIds.forEach((productIdElement) => {
            const productId = productIdElement.textContent;
            fetch(`/api/v1/product/${productId}/stock`)
                .then(response => response.json())
                .then(availableStock => {
                    const stockElement = productIdElement.closest('.productContainer').querySelector('.stock');
                    stockElement.textContent = `재고: ${availableStock}`;
                })
                .catch(error => console.error('Error fetching available stock:', error));
        });

    },


    addItems: function () {
        const data = {
            productId: document.getElementById("productId").value,
            quantity: document.getElementById("quantity").value,
            price: document.getElementById("formattedPrice").innerText.replace(/\D/g, ''),
        }
        console.log(data)

        fetch('/api/v1/cart/add', {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data)
        }).then(response => {
            if (response.ok) {
                alert("장바구니에 추가되었습니다.")
                this.updateStockQuantity();
            } else {
                return response.json()
            }
        }).catch(error => {
            console.error('Error:', error);
        })
    }
}

document.addEventListener("DOMContentLoaded", function () {
    cart.init();
    cart.updateStockQuantity();
})