export const cartToOrder = {
    init: function () {
        document.getElementById("btn-cart-order").addEventListener("click", (evt) => {
            evt.preventDefault();
            const form = document.getElementById('order-form');
            const checkboxes = document.querySelectorAll("input[type='checkbox']:checked");

            // 폼에 기존에 추가된 입력 필드들을 초기화
            const dynamicInputs = form.querySelectorAll('.dynamic-input');
            dynamicInputs.forEach(input => input.remove());

            const checkedItems = Array.from(checkboxes).map(checkbox => {
                const row = checkbox.closest('.stockContainer');
                return {
                    memberId: row.getAttribute('data-member-id'),
                    productId: row.getAttribute('data-product-id'),
                    productName: row.getAttribute('data-product-name'),
                    quantity: row.getAttribute('data-quantity'),
                    price: row.getAttribute('data-price')
                };
            });

            if (checkedItems.length === 0) {
                alert("주문하실 상품을 선택해주세요.");
                return;
            }

            // 폼에 동적으로 데이터를 추가
            checkedItems.forEach(item => {
                console.log(item);
                form.appendChild(this.createHiddenInput('memberId', item.memberId));
                form.appendChild(this.createHiddenInput(`productId`, item.productId));
                form.appendChild(this.createHiddenInput(`productName`, item.productName));
                form.appendChild(this.createHiddenInput(`quantity`, item.quantity));
                form.appendChild(this.createHiddenInput(`price`, item.price));
            });

            // 폼 제출
            form.submit();
        });
    },

    createHiddenInput: function (name, value) {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = name;
        input.value = value;
        input.classList.add('dynamic-input');  // 폼을 리셋하거나 재사용할 때 쉽게 제거할 수 있도록 클래스 추가
        return input;
    }
};
