async function loadHtml(containerId, url) {
    const container = document.getElementById(containerId);
    const response = await fetch(url);
    const html = await response.text();
    container.innerHTML = html;
}
(async function init() {
    await loadHtml('userPanel', 'user-details.html');
    await loadHtml('updateModal', 'edit-user.html');
    await loadHtml('new-user', 'new-user.html');
    await newUserSetup();
    addUserFormSubmit();
    await loadUsers();
    setupDeleteHandler();
    addEditButtonListener();
    setupEditFormSubmit();
    await loadUser();
})();

const editModalEl = document.getElementById('editModal');
const editModal = new bootstrap.Modal(editModalEl); // create a new instance
editModal.show();

// Switch Panels JS
    const adminBtn = document.getElementById('adminBtn');
    const userBtn = document.getElementById('userBtn');
    const adminPanel = document.getElementById('adminPanel');
    const userPanel = document.getElementById('userPanel');

    adminBtn.addEventListener('click', () => {
    adminPanel.style.display = 'block';
    userPanel.style.display = 'none';
    adminBtn.classList.add('active');
    userBtn.classList.remove('active');
});

    userBtn.addEventListener('click', () => {
    adminPanel.style.display = 'none';
    userPanel.style.display = 'block';
    userBtn.classList.add('active');
    adminBtn.classList.remove('active');
    console.log("user details btn clicked!")
});

    // Example: Load users dynamically (replace with your REST API)
   async function loadUsers() {
       try{
           const res = await fetch('http://localhost:8080/admin/users');
           if(!res.ok) throw new Error('Failed to load users');
           const users = await res.json();
           const tbody = document.getElementById('usersTableBody');
           users.forEach(user => {
               const tr = document.createElement('tr');
               tr.innerHTML = `
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>${user.roles.map(r=>`<span class="badge bg-secondary me-1">${r.name}</span>`).join('')}</td>
            <td><button class="btn btn-warning editBtn" data-bs-toggle="modal" data-bs-target="#editModal">Edit</button></td>
            <td><button class="btn btn-danger btn-sm remove-user" data-id="${user.id}">Delete</button></td>
        `;
               tbody.appendChild(tr);
           });
           const currentUser = await fetch('http://localhost:8080/admin/api/user');
           const user = await currentUser.json();
           const userInfo = document.getElementById('currentUser');
           userInfo.innerHTML = `${user.email} with role(s) [${user.roles.map(r => `<span>${r.name}</span>`).join('')}]`
       }catch (error) {
           console.log(error);
       }
   }

   // load a single user (current)
    async function loadUser() {
        try{
            const res = await fetch('http://localhost:8080/admin/api/user');
            if(!res.ok) throw new Error('Failed to load users');

            const user = await res.json();

            const tbody = document.getElementById('userTableBody');
            tbody.innerHTML =`
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>${user.roles.map(r=>`<span class="badge bg-secondary me-1">${r.name}</span>`).join('')}</td>
                </tr>
            `
        }catch (error) {
            console.log(error);
        }
    }

    function addEditButtonListener() {
        document.addEventListener('click', async (e) => {
            if(e.target.classList.contains('editBtn')) {

                const row = e.target.closest("tr");
                const id = row.children[0].textContent; // user.id from first <td>

                // Fetch user details
                const res = await fetch(`http://localhost:8080/admin/users/${id}`);
                const user = await res.json();

                // Fill modal inputs
                document.getElementById("editId").value = user.id;
                document.getElementById("editUsername").value = user.username;
                document.getElementById("editEmail").value = user.email;
                document.getElementById("editUserForm").action = `/admin/users/${id}`;
                document.getElementById("editRolesText").value = user.roles.map(r=>r.name).join(', ');

                const allRoles = await fetch('http://localhost:8080/admin/roles');
                const roles = await allRoles.json();

                const select = document.getElementById('editRoles');
                roles.forEach(role => {
                    const option = document.createElement('option');
                    option.value = role.id;
                    option.textContent = role.name;
                    select.appendChild(option);
                })

                // Show modal (optional)
                const modal = new bootstrap.Modal(document.getElementById('editModal'));
                modal.show();
            }
        })
    }

    function setupEditFormSubmit(){
        const editForm = document.getElementById('editUserForm');
        editForm.addEventListener('submit', async (e)=> {
            e.preventDefault();
            const id = document.getElementById("editId").value;
            const data = {
                username: document.getElementById('editUsername').value,
                email: document.getElementById('editEmail').value,
                password: document.getElementById('editPassword').value,
                roles: Array.from(document.getElementById('editRoles').selectedOptions)
                    .map(option => ({ id: option.value }))
            }

            try{
                const res = await fetch(`http://localhost:8080/admin/users/${id}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                })
                if (res.ok) {
                    alert("User updated successfully");

                    const editModalEl = document.getElementById('editModal');
                    const modalInstance = bootstrap.Modal.getInstance(editModalEl); // get existing instance
                    if (modalInstance) modalInstance.hide();
                    editForm.reset();
                } else {
                    alert("Something went wrong");
                }
            }catch (error) {
                alert("Failed to connect to server");
            }
        })
    }
function setupDeleteHandler() {
    const tbody = document.getElementById("usersTableBody");

    tbody.addEventListener("click", async (e) => {
        if (e.target.classList.contains("remove-user")) {
            const id = e.target.dataset.id;

            if (!confirm("Are you sure you want to delete this user?")) return;

            try {
                const res = await fetch(`http://localhost:8080/admin/users/remove/${id}`, {
                    method: "DELETE"
                });

                if (res.ok) {
                    alert("User deleted");
                    e.target.closest("tr").remove(); // remove row from UI
                } else {
                    alert("Failed to delete user");
                }
            } catch (err) {
                alert("Server error");
            }
        }
    });
}
function newUserSetup(){
       const newUserBtn = document.getElementById("new-user-tab");
       newUserBtn.addEventListener("click", async (e) => {
           const allRoles = await fetch('http://localhost:8080/admin/roles');
           const roles = await allRoles.json();

           const select = document.getElementById('newRoles');
           roles.forEach(role => {
               const option = document.createElement('option');
               option.value = role.id;
               option.textContent = role.name;
               select.appendChild(option);
           })
       })
}
function addUserFormSubmit(){
    const addUserForm = document.getElementById("addUserForm");

    addUserForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        document.querySelectorAll('.error').forEach(el => el.textContent = '');

        const data = {
            username: document.getElementById("newUsername").value,
            email: document.getElementById("newEmail").value,
            password: document.getElementById("newPassword").value,
            roles: Array.from(document.getElementById('newRoles').selectedOptions)
                .map(option => ({ id: option.value }))
        }

        try{
            const res = await fetch('http://localhost:8080/admin/users/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
            if(res.ok) {
                alert("User created successfully");
                addUserForm.reset();
                loadUsers();
                window.location.href = '/admin';
            }else if (res.status == 400) {
                const errors = await res.json();
                if (errors.username) document.getElementById("usernameError").textContent = errors.username;
                if (errors.email) document.getElementById("emailError").textContent = errors.email;
                if (errors.password) document.getElementById("passwordError").textContent = errors.password;
            }else{
                alert("Something went wrong");
            }
        }catch (error) {
            alert("Failed to connect to server");
        }
    })
}
