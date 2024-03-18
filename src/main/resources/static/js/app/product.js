const product= {
    init: function () {
        const _this = this;
        document.getElementById("btn-save")?.addEventListener("click", function () {
            _this.save();
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
        for(let i = 0; i < files.length; i++) {
            formData.append('files', files[i]);
        }

        fetch( '/api/v1/product/insert' , {
            method: "POST",
            body: formData
        }).then(response => {
            if(response.ok) {
                alert("상품이 등록되었습니다.")
                location.href = "/"
            } else {
                return response.json()
            }
        }).catch(error => console.log("Fetch error: ", error));
    }
}

document.addEventListener("DOMContentLoaded", function () {
    product.init();
})