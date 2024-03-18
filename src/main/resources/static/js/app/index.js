function productList() {
    fetch('/api/v1/product/list')
        .then(response => response.json())
        .then(data => {
            const products = data.products;
            const productsListElement = document.getElementById('productList');
            productsListElement.innerHTML = '';

            products.forEach(product => {
                const productElement = document.createElement('div');
                productElement.innerHTML = `
                        <h2>${product.prodName}</h2>
                        <p>${product.prodPrice}</p>
                        <img src="${product.storedUrl}" alt="${product.prodName}" style="width: 100px; height: 100px">`;
                productsListElement.appendChild(productElement);
            })
        }).catch(error => console.error('Error fetching products', error));

}
document.addEventListener('DOMContentLoaded', productList);