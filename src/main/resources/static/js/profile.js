async function loadProfile() {
    const profile = await request("/profile/me");
    document.getElementById("profileName").textContent = profile.fullName;
    document.getElementById("profileImg").src = profile.profileImage || "avatar.png";

    const myList = document.getElementById("myItems");
    myList.innerHTML = "";
    profile.myItems.forEach(i => {
        myList.innerHTML += `<li>${i.name} (${i.address})</li>`;
    });

    const savedList = document.getElementById("savedItems");
    savedList.innerHTML = "";
    profile.savedItems.forEach(i => {
        savedList.innerHTML += `<li>${i.name}</li>`;
    });
}
