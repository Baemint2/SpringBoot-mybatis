const product = {
    init: function () {
        const _this = this;
        document.getElementById("btn-save")?.addEventListener("click", function () {
            _this.save();
        })
        document.getElementById("btn-modify")?.addEventListener('click', function () {
            _this.update();
        })
    },

    save: function () {
        const data = {
            prodName: document.getElementById("prodName").value,
            description: document.getElementById("description").value,
            prodPrice: document.getElementById("prodPrice").value,
            stockQuantity: document.getElementById("stockQuantity").value,
        }
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
                alert("상품이 등록되었습니다.")
                location.href = "/"
            } else {
                return response.json().then(data => {
                    alert(data.message);
                })
            }
        }).catch(error =>
            console.log("Fetch error: ", error));
    },
    update: function () {
        const data = {
            prodName: document.getElementById("prodName").value,
            description: document.getElementById("description").value,
            prodPrice: document.getElementById("prodPrice").value,
            stockQuantity: document.getElementById("stockQuantity").value,
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
            console.log(response)
            if (!response.ok)
                return response.json().then(data => {
                    // 서버로부터 받은 오류 메시지를 표시
                    Object.keys(data).forEach(function (field) {
                        const errorElement = document.getElementById(`${field}-error`);
                        if (errorElement) {
                            errorElement.textContent = data[field];
                            errorElement.style.display = 'block';
                        }
                    });
                    return Promise.reject(new Error("글 수정 중 문제가 발생했습니다."));
                });
            return response.json();
        }).then(() => {
            // window.location.href = `/product/detail/${productId}`;
        }).catch(error => {
            // 네트워크 오류 또는 response.ok가 false 인 경우 여기서 처리
            console.error('Error : ', error.message)
        });

    },

}

document.addEventListener("DOMContentLoaded", function () {
    product.init();
})