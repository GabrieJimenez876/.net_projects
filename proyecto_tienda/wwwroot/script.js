const products = [
    { id: 1, name: "Peluche 1", price: 20, image: "https://via.placeholder.com/250" },
    { id: 2, name: "Peluche 2", price: 25, image: "https://via.placeholder.com/250" },
    { id: 3, name: "Peluche 3", price: 30, image: "https://via.placeholder.com/250" }
];

const cart = [];
const productList = document.getElementById('product-list');
const cartCount = document.getElementById('cartCount');

function renderProducts() {
    productList.innerHTML = '';
    products.forEach(product => {
        const productDiv = document.createElement('div');
        productDiv.className = 'product';
        productDiv.innerHTML = `
            <img src="${product.image}" alt="${product.name}">
            <h3>${product.name}</h3>
            <p>$${product.price}</p>
            <button onclick="addToCart(${product.id})">Agregar al carrito</button>
        `;
        productList.appendChild(productDiv);
    });
}

function addToCart(id) {
    const product = products.find(p => p.id === id);
    if (product) {
        cart.push(product);
        updateCartCount();
    }
}

function updateCartCount() {
    cartCount.textContent = cart.length;
}

renderProducts();