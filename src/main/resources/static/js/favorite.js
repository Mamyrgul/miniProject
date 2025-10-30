async function toggleFavorite(id) {
    await request("/favorite/action?itemId=" + id, "POST");
    loadItems();
}
