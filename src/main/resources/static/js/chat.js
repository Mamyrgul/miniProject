async function loadChat(otherId) {
    const msgs = await request("/messages/chat/" + otherId);
    const box = document.getElementById("chatBox");
    box.innerHTML = msgs.map(m => `<p><b>${m.senderName}:</b> ${m.content}</p>`).join("");
}

async function sendMessage() {
    const id = document.getElementById("receiverId").value;
    const content = document.getElementById("msgText").value;
    await request("/messages/send", "POST", { receiverId: id, content });
    loadChat(id);
}
