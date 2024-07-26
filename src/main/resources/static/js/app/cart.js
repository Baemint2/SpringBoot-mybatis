import { cartToOrder } from "./cartToOrder.js";

const cart = {
    init: function () {
        const _this = this;

        document.getElementById("btn-cart")?.addEventListener("click", function () {
            _this.addItems();
        });

        document.getElementById("btn-delete-selected")?.addEventListener('click', function (evt) {
            _this.deleteSelectedItems(evt);
        })

    },
    updateProductQuantityOnPage: function(prodId, updatedQuantity) {
        // 모든 productContainer 요소를 가져옴
        const productContainers = document.querySelectorAll(`.productContainer[data-product-id="${prodId}"]`);
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
        const data = {
            prodId: document.getElementById("prodId").value,
            cartQuantity: document.getElementById("quantity").value,
            cartPrice: document.querySelector(".formattedPrice").innerText.replace(/\D/g, ''),
        }
        console.log(data.price)
        fetch('/api/v1/cart', {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(data => {
                if(data.cartId) {
                    console.log(data);
                    alert("장바구니에 추가되었습니다.");
                    this.updateSingleStockQuantity(data.prodId); // 여기서 prodId를 직접 사용
                } else if(data.error) {
                    alert(data.error);
                }
            })
            .catch(error => {
                console.error("Error: ", error);
            });
    },
    // 개별 제품의 재고 정보를 업데이트하는 함수
    updateSingleStockQuantity: function (prodId) {
        console.log(`Updating stock for product ID: ${prodId}`); // 로그 추가
        fetch(`/api/v1/product/${prodId}/stock`)
            .then(response => response.json())
            .then(stock => {
                const selector = `.container .stock`;
                const stockElement = document.querySelector(selector);
                if (stockElement) {
                    stockElement.innerHTML = `<strong class="mr-3" ">Quantity: ${stock}</strong>`;
                } else {
                    console.error(`Stock element not found for product ID: ${prodId}`);
                }
            })
            .catch(error => console.error('Error fetching available stock:', error));
    },

    getTotalPrice: function () {
        fetch('/api/v1/cart')
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
            button.addEventListener("click", function (evt) {
                evt.preventDefault();
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
            button.addEventListener("click", function (evt) {
                evt.preventDefault();
                const quantityElement = this.parentElement.querySelector('.quantity');
                let quantity = parseInt(quantityElement.textContent);
                quantity++;
                quantityElement.textContent = quantity;
            })
        })
    },
    changeStock: function () {
        document.querySelectorAll('.change-stock').forEach(button => {
            button.addEventListener('click', function (evt) {
                evt.preventDefault();
                const stockContainer = this.closest('.stockContainer');
                const prodId = stockContainer.getAttribute('data-product-id');
                const newQuantityElement = stockContainer.querySelector('.quantity');
                const newQuantity = parseInt(newQuantityElement.textContent);
                console.log(newQuantity);
                // 수정해야하는 위치
                const currentQuantity = parseInt(stockContainer.getAttribute('data-quantity'))
                console.log("현재 수량 : " + currentQuantity)
                const adjustment = newQuantity - currentQuantity; // 실제 변경해야 할 수량 계산
                console.log("변경될 수량 : " + adjustment)

                console.log("newQuantity:", newQuantity, "currentQuantity:", currentQuantity, "adjustment:", adjustment);

                const stockUpdateData = {
                    prodId: prodId,
                    adjustment: adjustment,
                    isIncrease: adjustment > 0
                };

                fetch("/api/v1/product/stock/update", {
                    method: "PUT",
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
          button.addEventListener("click", function (evt) {
              evt.preventDefault();
              const cartId = this.getAttribute('data-cart-item-id');
              console.log(this.getAttribute('data-cart-item-id'));
              cart.deleteCartItem(cartId);
          })
      })
    },

    deleteCartItem: function (cartId) {
        console.log(`삭제 될 상품 ID: ${cartId}`);
        fetch(`/api/v1/cart/${cartId}`, {
            method: "DELETE",
            headers: {"Content-Type" : "application/json"}
        }).then(response => {
            if(!response.ok) {
                throw new Error("네트워크 에러가 발생했습니다.")
            }
            alert("상품이 삭제되었습니다.")
            const buttonElement = document.querySelector(`[data-cart-item-id="${cartId}"]`);
            console.log(buttonElement)
            if(buttonElement) {
                const rowElement = buttonElement.closest('.stockContainer');
                console.log(rowElement)
                rowElement.remove();
            }
            this.getTotalPrice();
        }).catch( error =>
            alert(`오류가 발생했습니다. ${error.message}`));
    },
    deleteSelectedItems: function (evt) {
        evt.preventDefault();
        const selectedItems = document.querySelectorAll('input[type="checkbox"]:checked');
        let cartItems = Array.from(selectedItems).map(item => item.getAttribute('data-cart-item-id'));

        if(cartItems.length === 0) {
            alert("삭제할 상품을 선택해주세요.");
            return;
        }

        if(!confirm("선택한 상품을 삭제하시겠습니까?")) {
            return;
        }

        fetch(`/api/v1/cart/item/${cartItems.join(',')}`, {
            method: "DELETE",
            headers: {"Content-Type": "application/json" },
        }).then(response => {
            if(response.ok) {
                alert("선택한 상품이 삭제되었습니다.")
                cartItems.forEach(cartId => {
                    const buttonElement = document.querySelector(`input[data-cart-item-id="${cartId}"]`)
                    if(buttonElement) {
                        const rowElement = buttonElement.closest('.stockContainer');
                        if(rowElement) {
                            rowElement.remove();
                        }
                    }
                })
                this.getTotalPrice()
            } else {
                throw new Error("네트워크 에러 발생");
            }
        }).catch(error => {
            console.log(error);
            alert("삭제 중 오류가 발생했습니다.")
        })

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
    cartToOrder.init();
})


