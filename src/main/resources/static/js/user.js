document.getElementById("userForm").addEventListener("submit", async function(event) {
    event.preventDefault();
    
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const phone = document.getElementById("phone").value;

    const response = await fetch("/user/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, email, phone })
    });

    if (response.ok) {
        alert("Usuário registrado com sucesso!");
        document.getElementById("userForm").reset();
    } else {
        alert("Erro ao registrar usuário.");
    }
});
