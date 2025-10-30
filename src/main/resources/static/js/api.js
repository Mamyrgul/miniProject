const API_BASE = "http://localhost:2025/api";
let token = localStorage.getItem("token");

async function request(path, method = "GET", body = null, isForm = false) {
    const headers = {};
    if (!isForm) headers["Content-Type"] = "application/json";
    if (token) headers["Authorization"] = "Bearer " + token;

    const res = await fetch(API_BASE + path, {
        method,
        headers,
        body: body ? (isForm ? body : JSON.stringify(body)) : null
    });

    if (!res.ok) throw new Error(await res.text());
    return res.status === 204 ? null : res.json();
}

function setToken(newToken) {
    token = newToken;
    localStorage.setItem("token", newToken);
}
