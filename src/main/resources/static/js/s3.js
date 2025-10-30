async function uploadFiles() {
    const input = document.getElementById("fileInput");
    const form = new FormData();
    for (const f of input.files) form.append("files", f);

    const res = await request("/s3/upload", "POST", form, true);
    document.getElementById("result").textContent = res.join("\n");
}
