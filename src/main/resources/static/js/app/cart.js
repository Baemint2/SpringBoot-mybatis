const cart = {
    init: function () {
        const _this = this;

        document.getElementById("btn-cart")?.addEventListener("click", function () {
            _this.addItems();
        });

    },
    updateProductQuantityOnPage: function(productId, updatedQuantity) {
        // 모든 productContainer 요소를 가져옴
        const productContainers = document.querySelectorAll(`.productContainer[data-product-id="${productId}"]`);
        productContainers.forEach(container => {
            // 각 컨테이너 내에서 재고를 표시하는 요소를 찾음
            const stockElement = container.querySelector('.stock');
            if (stockElement) {
                console.log(stockElement)
                stockElement.textContent = `재고: ${updatedQuantity}`; // 이제 여기에 적절한 값을 넣어야 함
            }
        });
    },
    addItems: function () {
        const productId = document.getElementById("productId").value;
        const quantity = document.getElementById("quantity").value;
        const price = document.querySelector(".formattedPrice").innerText.replace(/\D/g, '');

        const data = { productId, quantity, price };
        console.log(data);

        fetch('/api/v1/cart/add', {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(data => {
                if(data.cartItemId) {
                    console.log(data);
                    alert("장바구니에 추가되었습니다.");
                    this.updateSingleStockQuantity(data.productId); // 여기서 productId를 직접 사용
                } else if(data.error) {
                    alert(data.error);
                }
            })
            .catch(error => {
                console.error("Error: ", error);
            });
    },
    // 개별 제품의 재고 정보를 업데이트하는 함수
    updateSingleStockQuantity: function (productId) {
        console.log(`Updating stock for product ID: ${productId}`); // 로그 추가
        fetch(`/api/v1/product/${productId}/stock`)
            .then(response => response.json())
            .then(stock => {
                console.log(stock)
                const selector = `.container .stock`;
                console.log(`Using selector: ${selector}`); // 선택자 확인 로그
                const stockElement = document.querySelector(selector);
                console.log(stockElement)
                if (stockElement) {
                    stockElement.textContent = `재고: ${stock}`;
                } else {
                    console.error(`Stock element not found for product ID: ${productId}`);
                }
            })
            .catch(error => console.error('Error fetching available stock:', error));
    },

    getTotalPrice: function () {
        fetch('/api/v1/cart/total')
            .then(response => response.json())
            .then(price => {
                console.log(price.totalPrice)
                const totalPrice = document.getElementById('totalPrice');
                if(totalPrice) {
                    totalPrice.textContent = new Intl.NumberFormat('ko-KR', {
                        style: 'currency',
                        currency: 'KRW'
                    }).format(price.totalPrice);
                }
            }).catch(error => console.error("Error: ", error));
    },

    btnDecrease: function () {
        document.querySelectorAll('.decrease-btn').forEach(button => {
            button.addEventListener("click", function () {
                const quantityElement = this.parentElement.querySelector('.quantity');
                let quantity = parseInt(quantityElement.textContent);
                if(quantity > 1) { // 최소 수량 1
                    quantity--;
                    quantityElement.textContent = quantity;
                }
            })
        })
    },

    btnIncrease: function () {
        document.querySelectorAll('.increase-btn').forEach(button => {
            button.addEventListener("click", function () {
                const quantityElement = this.parentElement.querySelector('.quantity');
                let quantity = parseInt(quantityElement.textContent);
                quantity++;
                quantityElement.textContent = quantity;
            })
        })
    },
    changeStock: function () {
        document.querySelectorAll('.change-stock').forEach(button => {
            button.addEventListener('click', function () {
                const stockContainer = this.closest('.stockContainer');
                const productId = stockContainer.getAttribute('data-product-id');
                const newQuantityElement = stockContainer.querySelector('.quantity');
                const newQuantity = parseInt(newQuantityElement.textContent);
                const currentQuantity = parseInt(newQuantityElement.getAttribute('data-current-quantity'))
                console.log("현재 수량 : " + currentQuantity)
                const adjustment = newQuantity - currentQuantity; // 실제 변경해야 할 수량 계산
                console.log("변경될 수량 : " + adjustment)

                console.log("newQuantity:", newQuantity, "currentQuantity:", currentQuantity, "adjustment:", adjustment);

                const stockUpdateData = {
                    productId: productId,
                    adjustment: adjustment,
                    isIncrease: adjustment > 0
                };

                fetch("/api/v1/product/stock/update", {
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    body : JSON.stringify(stockUpdateData)
                })
                .then(response => {
                    if(!response.ok) {
                        throw new Error("재고 업데이트 실패")
                    }
                    alert("재고가 업데이트 되었습니다.");
                    console.log(newQuantityElement)
                    newQuantityElement.setAttribute('data-current-quantity', newQuantity)
                })
                .catch(error => {
                    console.error("Error: ", error);
                })
            })
        })
    },

    bindDeleteEvents: function () {
      document.querySelectorAll(".delete-cart-item-btn").forEach(button => {
          button.addEventListener("click", function () {
              const cartItemId = this.getAttribute('data-cart-item-id');
              console.log(this.getAttribute('data-cart-item-id'));
              cart.deleteCartItem(cartItemId);
          })
      })
    },

    deleteCartItem: function (cartItemId) {
        console.log(`삭제 될 상품 ID: ${cartItemId}`);
        fetch(`/api/v1/cart/${cartItemId}`, {
            method: "DELETE",
            headers: {"Content-Type" : "application/json"}
        }).then(response => {
            if(!response.ok) {
                throw new Error("네트워크 에러가 발생했습니다.")
            }
            alert("상품이 삭제되었습니다.")
            const buttonElement = document.querySelector(`[data-cart-item-id="${cartItemId}"]`);
            console.log(buttonElement)
            if(buttonElement) {
                const rowElement = buttonElement.closest('.stockContainer');
                console.log(rowElement)
                rowElement.remove();
            }
            this.getTotalPrice();
        }).catch( error =>
            alert(`오류가 발생했습니다. ${error.message}`));
    }



}

document.addEventListener("DOMContentLoaded", function () {
    cart.init();
    cart.updateProductQuantityOnPage();
    cart.getTotalPrice();
    cart.btnDecrease();
    cart.btnIncrease();
    cart.changeStock();
    cart.bindDeleteEvents();
})