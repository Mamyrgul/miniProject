async function loadItems() {
    const data = await request("/item/for-users");
    const container = document.getElementById("items");
    container.innerHTML = "";
    data.allItems.forEach(i => {
        const card = document.createElement("div");
        card.className = "item-card";
        card.innerHTML = `
            <img src="${i.imageUrls?.[0] || "no-image.jpg"}" width="200">
            <h3>${i.name}</h3>
            <p>${i.description}</p>
            <button onclick="toggleFavorite(${i.id})">
                ${i.isSaved ? "Удалить из избранного" : "В избранное"}
            </button>
        `;
        container.appendChild(card);
    });
}

async function createItem() {
    const data = {
        name: document.getElementById("name").value,
        description: document.getElementById("desc").value,
        address: document.getElementById("addr").value,
        city: document.getElementById("city").value,
        region: "N/A",
        price: parseFloat(document.getElementById("price").value),
        imageUrls: [document.getElementById("imageUrl").value]
    };
    await request("/item/new", "POST", data);
    alert("Вещь добавлена!");
}
