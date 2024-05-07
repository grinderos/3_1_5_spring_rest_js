const URLCurrentUser = "/api/getCurrentUser";
const URLListUsers = "/api/getUsers";
const URLUpdate = "api/update";
const top_panel_info = document.getElementById('top_panel_info');
const table_user_info = document.getElementById('table_user_info');
let formEdit = document.forms["formEdit"];

//vvv--- user methods ---vvv
getCurrentUser();

function getCurrentUser() {
    fetch(URLCurrentUser)
        .then((res) => res.json())
        .then((currentUser) => {

            let currentRoles = getCurrentRoles(currentUser.roles);
            top_panel_info.innerHTML = `
                                        <span>Вы вошли как: <b>${currentUser.username}</b></span 
>                                       <span> | Обладает ролью:</span>
                                        <span>${currentRoles}</span>
                                        `;

            table_user_info.innerHTML = `
                                <tr>
                                    <td>${currentUser.id}</td>
                                    <td>${currentUser.username}</td>
                                    <td>${currentUser.firstname}</td>
                                    <td>${currentUser.lastname}</td>
                                    <td>${currentUser.age}</td>
                                    <td>${currentUser.email}</td>
                                    <td>${currentRoles}</td>
                                </tr>`;
        });
}

function getCurrentRoles(roles) {
    let currentRoles = "";
    for (let element of roles) {
        currentRoles += (element.name.toString().substring(5) + " ");
    }
    return currentRoles;
}

//^^^--- user methods ---^^^

//vvv--- admin methods ---vvv
getUsers();

function getUsers() {
    fetch(URLListUsers).then(function (response) {
        return response.json();
    }).then(function (users) {
        const users_table_fill = document.getElementById('users_table_fill');
        for (let user of users) {
            let userRoles = getCurrentRoles(user.roles);

            let fill_users = `<tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.firstname}</td>
                        <td>${user.lastname}</td>
                        <td>${user.age}</td>
                        <td>${user.email}</td>
                        <td>${userRoles}</td>
                        <td>
                          <button type="button"
                          class="btn btn-info btn-ml"
                          data-bs-toogle="modal"
                          data-bs-target="#editModal"
                          onclick="modalEdit(${user.id})">
                                Edit
                            </button>
                        </td>
                        <td>
                            <button type="button" 
                            class="btn btn-danger btn-ml" 
                            data-toggle="modal" 
                            data-target="#deleteModal" 
                            onclick="deleteModal(${user.id})">
                                Delete
                            </button>
                        </td>
                    </tr>`;
            users_table_fill.innerHTML += fill_users;
        }
    })
}
//
// <td>
//     <button className="btn btn-info btn-ml"
//             data-bs-toggle="modal"
//             data-bs-target="#modalEdit"
//             onClick="modalEdit(${user.id})"
//     >Редактировать
//     </button>
// </td>
// <td>
//     <button className="btn btn-danger btn-ml"
//             data-bs-toggle="modal"
//             data-target="#modalDelete"
//             onClick="modalDelete(${user.id})"
//     >Удалить
//     </button>
// </td>

async function modalEdit(id) {
    const modalEdit = new bootstrap.Modal(document.querySelector('#modalEdit'));
    await fill_modalEdit(formEdit, modalEdit, id);
    loadRolesForEdit();
}

function loadRolesForEdit() {
    let edit_check_roles = document.getElementById("role_checkbox_upd");
    edit_check_roles.innerHTML = "";

    fetch("/api/findAllRoles")
        .then(res => res.json())
        .then(data => {
            data.forEach(role => {
                let option = document.createElement("option");
                option.value = role.id;
                option.text = role.name.substring(5);
                edit_check_roles.appendChild(option);


                //let checkboxes  = document.createElement("input");
                //console.log(checkboxes);
                //checkboxes.class = "form-check-input";
                //checkboxes.type = "checkbox";
                //checkboxes.value = role.id;
                //checkboxes.text = role.name;
                //console.log(checkboxes);
                //edit_check_roles.appendChild(checkboxes);
            });
        })
        .catch(error => console.error(error));
}

window.addEventListener("load", loadRolesForEdit);

async function fill_modalEdit(form, modal, id) {
    modal.show();
    let user = await getUserById(id);
    form.id.value = user.id;
    form.username.value = user.username;
    form.firstname.value = user.firstname;
    form.lastname.value = user.lastname;
    form.age.value = user.age;
    form.email.value = user.email;
    form.password.value = user.password;
    form.roles.value = user.roles;
}

async function getUserById(id) {
    let response = await fetch("api/getUser/" + id);
    return await response.json();
}

function editUser() {
    formEdit.addEventListener("submit", ev => {
        ev.preventDefault();

        let rolesForEdit = [];
        for (let i = 0; i < formEdit.roles.options.length; i++) {
            if (formEdit.roles.options[i].selected) rolesForEdit.push({
                id: formEdit.roles.options[i].value,
                role: "ROLE_" + formEdit.roles.options[i].text
            });
            console.log(rolesForEdit);
        }

        fetch(URLUpdate, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: formEdit.id.value,
                username: formEdit.username.value,
                firstname: formEdit.firstname.value,
                lastname: formEdit.lastname.value,
                age: formEdit.age.value,
                email: formEdit.email.value,
                password: formEdit.password.value,
                roles: rolesForEdit
            })
        }).then(() => {
            $('#editClose').click();
            getAllUsers();
        });
    });
}
editUser();
//^^^--- admin methods ---^^^