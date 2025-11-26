const form = document.getElementById("signupForm");

form.addEventListener("submit", async (e) => {
    e.preventDefault();

    document.querySelectorAll('.error').forEach(el => el.textContent = '');

    const data = {
        username: document.getElementById("username").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    }

    try{
        const res = await fetch('http://localhost:8080/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        if(res.ok) {
            alert("User created successfully");
            form.reset();
            window.location.href = "/login";
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