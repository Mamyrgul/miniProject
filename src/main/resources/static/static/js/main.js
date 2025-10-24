/**
 * Рендерит список объявлений (Улучшение #4 + Дизайн)
 */
function renderItems(items) {
    UI.itemsList.innerHTML = ''; // Очищаем

    if (!items || items.length === 0) {
        UI.itemsList.innerHTML = '<p class="text-gray-500 col-span-full text-center p-10 text-xl font-semibold">😔 No listings found matching your search.</p>';
        return;
    }

    // Создаем DocumentFragment для одной вставки в DOM
    const fragment = document.createDocumentFragment();

    items.forEach(item => {
        const card = document.createElement('div');
        // Используем класс 'item-card' из style.css для базовых стилей
        card.className = 'item-card card transform hover:scale-[1.02] transition duration-300';

        // ------------------ ИЗОБРАЖЕНИЕ ------------------
        const img = document.createElement('img');
        const imageUrl = (item.imageUrls && item.imageUrls.length > 0)
            ? item.imageUrls[0]
            : '/images/default.png';
        img.src = imageUrl;
        img.className = 'item-card-image'; // Класс из style.css
        img.alt = item.name;

        // ------------------ КОНТЕНТ ------------------
        const contentDiv = document.createElement('div');
        contentDiv.className = 'item-card-content'; // Класс из style.css

        const title = document.createElement('h3');
        title.className = 'item-card-title text-gray-900';
        title.textContent = item.name;

        const address = document.createElement('p');
        address.className = 'text-sm text-gray-500 mt-1 mb-2';
        address.textContent = item.address;

        const desc = document.createElement('p');
        desc.className = 'text-gray-600 text-sm h-10 overflow-hidden line-clamp-2';
        desc.textContent = item.description;

        const price = document.createElement('p');
        price.className = 'item-card-price';
        // Форматирование цены в красивый вид, например $1,500
        price.textContent = new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD',
            minimumFractionDigits: 0, // Убираем .00
            maximumFractionDigits: 0
        }).format(item.price);

        // ------------------ СБОРКА ------------------
        contentDiv.append(title, address, desc, price);
        card.append(img, contentDiv);
        fragment.appendChild(card);
    });

    // Вставляем все карточки в DOM за одну операцию
    UI.itemsList.appendChild(fragment);
}