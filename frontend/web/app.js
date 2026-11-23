const backendStatus = document.getElementById("backend-status");
const databaseStatus = document.getElementById("database-status");
const taskList = document.getElementById("task-list");
const message = document.getElementById("message");
const reloadButton = document.getElementById("reload-button");

async function loadStatus() {
    try {
        const response = await fetch("/api/status");
        const data = await response.json();

        backendStatus.textContent =
            data.application === "running" ? "Operativo" : "No disponible";

        databaseStatus.textContent =
            data.database === "connected" ? "Conectada" : "No disponible";
    } catch (error) {
        backendStatus.textContent = "No disponible";
        databaseStatus.textContent = "No disponible";
    }
}

async function loadTasks() {
    taskList.innerHTML = "";
    message.textContent = "Cargando tareas...";

    try {
        const response = await fetch("/api/tasks");

        if (!response.ok) {
            throw new Error("No se pudieron obtener las tareas");
        }

        const tasks = await response.json();
        message.textContent = "";

        tasks.forEach(task => {
            const item = document.createElement("li");
            item.className = task.completed ? "task completed" : "task";

            const state = document.createElement("span");
            state.className = "task-state";
            state.textContent = task.completed ? "✓" : "○";

            const title = document.createElement("span");
            title.textContent = task.title;

            item.appendChild(state);
            item.appendChild(title);
            taskList.appendChild(item);
        });
    } catch (error) {
        message.textContent =
            "No ha sido posible obtener las tareas. Comprueba el backend y la base de datos.";
    }
}

async function refresh() {
    await loadStatus();
    await loadTasks();
}

reloadButton.addEventListener("click", refresh);

refresh();
