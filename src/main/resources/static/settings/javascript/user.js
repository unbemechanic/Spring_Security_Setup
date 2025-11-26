    async function loadUser() {
    try {
    const res = await fetch("http://localhost:8080/user/current");
    if (!res.ok) throw new Error("Cannot load user");

    const user = await res.json();

    // Top bar
    document.getElementById("currentUserBar").innerHTML =
    `${user.email} with roles [${user.roles.map(r => r.name).join(', ')}]`;

    // Table
    document.getElementById("userTableBody").innerHTML = `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>${user.roles.map(r =>
    `<span class="badge bg-secondary me-1">${r.name}</span>`
    ).join('')}</td>
                </tr>
            `;
} catch (err) {
    console.error(err);
}
}

    window.addEventListener("DOMContentLoaded", loadUser);
