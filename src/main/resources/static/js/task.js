const socket = new WebSocket("ws://localhost:8080/ws/tasks");

socket.onmessage = function(event) {
    console.log("Mensagem do servidor:", event.data);
    loadTasks();
};

function loadTasks() {
    fetch("/task/list")
        .then(response => response.json())
        .then(data => {
            let taskList = document.getElementById("taskList");
            taskList.innerHTML = "";

            if (data.length === 0) {
                taskList.innerHTML = `<p class="text-muted text-center">Nenhuma tarefa encontrada.</p>`;
                return;
            }

            data.forEach(task => {
                let li = document.createElement("li");
                li.classList.add("list-group-item", "d-flex", "justify-content-between", "align-items-center");

                let commentsHTML = "";
                if (task.comments && Array.isArray(task.comments) && task.comments.length > 0) {
                    commentsHTML = "<strong>Comentários:</strong><ul class='list-unstyled'>";
                    task.comments.forEach(comment => {
                        commentsHTML += `
                            <li>
                                <em>"${comment.text}"</em> - <strong>${comment.author ? comment.author.name : "Anônimo"}</strong> 
                                <small class="text-muted">(${new Date(comment.instant).toLocaleString()})</small>
                            </li>
                        `;
                    });
                    commentsHTML += "</ul>";
                } else {
                    commentsHTML = "<em class='text-muted'>Sem comentários</em>";
                }

                li.innerHTML = `
                    <div>
                        <strong>${task.title}</strong> - ${task.description}
                        <br> <em class="text-muted">Responsável: ${task.user.name}</em>
                        <br> ${commentsHTML}
                    </div>
                    <button class="btn btn-danger btn-sm" onclick="deleteTask('${task.id}')">
                        <i class="bi bi-trash"></i> Excluir
                    </button>
                `;

                taskList.appendChild(li);
            });
        })
        .catch(error => console.error("Erro ao carregar tarefas:", error));
}

function addTask() {
    let title = document.getElementById("taskTitle").value;
    let description = document.getElementById("taskDescription").value;
    let userId = document.getElementById("id_user").value;

    if (title.trim() === "" || description.trim() === "" || userId.trim() === "") {
        showAlert("Preencha todos os campos!", "danger");
        return;
    }

    fetch(`/task/create/${userId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title: title, description: description })
    })
    .then(response => {
        if (!response.ok) throw new Error("Erro ao criar tarefa");
        return response.json();
    })
    .then(() => {
        document.getElementById("taskTitle").value = "";
        document.getElementById("taskDescription").value = "";
        showAlert("Tarefa adicionada com sucesso!", "success");
        loadTasks();
    })
    .catch(error => {
        console.error("Erro ao adicionar tarefa:", error);
        showAlert("Erro ao adicionar tarefa!", "danger");
    });
}

function deleteTask(taskId) {
    fetch(`/task/delete/${taskId}`, { method: "DELETE" })
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao excluir tarefa: " + response.statusText);
            }
            if (response.status === 204) {
                showAlert("Tarefa excluída com sucesso!", "success");
                loadTasks();
            }
        })
        .catch(error => {
            console.error("Erro ao excluir tarefa:", error);
            showAlert("Erro ao excluir tarefa!", "danger");
        });
}

function showAlert(message, type) {
    const alertBox = document.getElementById("alertBox");
    alertBox.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;
}

window.onload = loadTasks;
