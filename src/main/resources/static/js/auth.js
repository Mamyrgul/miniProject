async function registerUser() {
    const data = {
        fullName: document.getElementById("fullName").value,
        phoneNumber: document.getElementById("phone").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        confirmPassword: document.getElementById("confirm").value
    };
    try {
        const res = await request("/auth/register", "POST", data);
        console.log("✅ Успех:", res);
        alert("Регистрация успешна: " + res.email);
    } catch (e) {
        console.error("❌ Ошибка регистрации:", e);
        alert("Ошибка регистрации: " + e.message);
    }
}

async function loginUser() {
    const data = {
        email: document.getElementById("loginEmail").value,
        password: document.getElementById("loginPassword").value
    };
    try {
        const res = await request("/auth/login", "POST", data);
        console.log("✅ Успешный вход:", res);
        setToken(res.token);
        alert("Добро пожаловать, " + res.email);
        window.location.href = "main.html";
    } catch (e) {
        console.error("❌ Ошибка входа:", e);
        alert("Ошибка входа: " + e.message);
    }
}
