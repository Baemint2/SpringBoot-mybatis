const product = {
    init: function () {
        const _this = this;
        document.getElementById("btn-save")?.addEventListener("click", function () {
            _this.productSave();
        })
        document.getElementById("btn-modify")?.addEventListener('click', function () {
            _this.productUpdate();
        })
        document.getElementById("btn-remove")?.addEventListener('click', function () {
            _this.productDelete();
        })
    },

    productSave: function () {
        const data = {
            prodName: document.getElementById("prodName").value,
            description: document.getElementById("description").value,
            prodPrice: document.getElementById("prodPrice").value,
            stockQuantity: document.getElementById("stockQuantity").value,
            categoryId: document.getElementById("mediumCategory").value
        }
        console.log(data)
        const formData = new FormData();
        formData.append('product', new Blob([JSON.stringify(data)], {type: "application/json"}));

        const files = document.getElementById("files").files;
        for (let i = 0; i < files.length; i++) {
            formData.append('files', files[i]);
        }

        fetch('/api/v1/product/insert', {
            method: "POST",
            body: formData
        }).then(response => {
            if (response.ok) {
                alert("글이 등록되었습니다.")
                window.location.href = "/"
            } else {
                return response.json()
            }
        }).then(errorResponse => {
            console.log(errorResponse)
            document.querySelectorAll('.error-message').forEach(errorContainer => {
                errorContainer.style.display = 'none';
                errorContainer.textContent = '';
            });

            Object.keys(errorResponse).forEach(field => {
                console.log(errorResponse[field])
                const errorContainer = document.getElementById(`${field}-error`);
                console.log(errorContainer.textContent = errorResponse[field]);
                if (errorContainer) {
                    errorContainer.textContent = errorResponse[field];
                    errorContainer.style.display = 'block';
                }
            })
        }).catch(error => console.log("Fetch error: ", error));
    },
    productUpdate: function () {
        const data = {
            prodName: document.getElementById("prodName").value,
            description: document.getElementById("description").value,
            prodPrice: document.getElementById("prodPrice").value,
            stockQuantity: document.getElementById("stockQuantity").value,
            categoryId: document.getElementById("mediumCategory").value
        }
        const formData = new FormData();
        formData.append('product', new Blob([JSON.stringify(data)], {type: "application/json"}));

        const files = document.getElementById("files").files;
        for (let i = 0; i < files.length; i++) {
            formData.append('files', files[i]);
        }
        const productId = document.getElementById("productId").value;
        console.log(productId)
        console.log(data)
        console.log(formData)
        fetch(`/api/v1/product/update/${productId}`, {
            method: 'PUT',
            body: formData,
        }).then(response => {
            if (response.ok) {
                alert("글이 수정되었습니다.")
                window.location.href = `/product/detail/${productId}`
            } else {
                return response.json()
            }
        }).then(errorResponse => {
            console.log(errorResponse)
            document.querySelectorAll('.error-message').forEach(errorContainer => {
                errorContainer.style.display = 'none';
                errorContainer.textContent = '';
            });

            Object.keys(errorResponse).forEach(field => {
                console.log(errorResponse[field])
                const errorContainer = document.getElementById(`${field}-error`);
                console.log(errorContainer.textContent = errorResponse[field]);
                if (errorContainer) {
                    errorContainer.textContent = errorResponse[field];
                    errorContainer.style.display = 'block';
                }
            })
        }).catch(error => console.log("Fetch error: ", error));
    },
    productDelete: function () {
        const prodId = document.getElementById('productId').value;
        console.log(prodId)
        fetch(`/api/v1/product/remove/${prodId}`, {
            method: 'DELETE',
            headers: {'Content-Type': 'application/json'}
        }).then(response => {
            if (!response.ok) {
                throw new Error("네트워크 에러가 발생했습니다.")
            }
            alert("상품이 삭제되었습니다.")
            location.href = "/";
        }).catch(error =>
            alert(`오류가 발생했습니다. ${error.message}`));
    },

    formattedPrice:function () {
        const priceElement = document.querySelectorAll('.formattedPrice')

        priceElement.forEach(element => {
            const price = parseInt(element.textContent, 10);
            element.textContent = new Intl.NumberFormat('ko-KR').format(price);
        })
    }



}

document.addEventListener("DOMContentLoaded", function () {
    product.init();
    product.formattedPrice();
})