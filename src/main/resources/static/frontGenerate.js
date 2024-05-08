const URLCurrentUser = "/api/getCurrentUser";
const URLListUsers = "/api/getUsers";
const URLGetUserById = "/api/getUser/";
const URLUpdate = "api/update";
const URLDelete = "/api/delete/";
const URLAddNew = "/api/add/";
const URLAllRoles = "/api/findAllRoles";
const URLLogout = "/logout";
const top_panel_info = document.getElementById('top_panel_info');
const table_user_info = document.getElementById('table_user_info');
let formEdit = document.forms["formEdit"];
let formDelete = document.forms["formDelete"];
let formNew = document.forms["formNew"];
let currId;

//vvv --- user  methods --- vvv

// <<< метод получения текущего пользователя >>>
function getCurrentUser() {
    fetch(URLCurrentUser)
        .then((res) => res.json())
        .then((currentUser) => {
            currId = currentUser.id;
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

getCurrentUser();


// <<< метод преобразования ролей текущего пользователя в строку >>>
function getCurrentRoles(roles) {
    let currentRoles = "";
    for (let element of roles) {
        currentRoles += (element.name.toString().substring(5) + " ");
    }
    return currentRoles;
}

/*  ^^^ --- user     methods --- ^^^
----------------------------------------------------------------
    vvv --- admin    methods --- vvv*/

window.addEventListener("load", loadUserRoles);

// <<< метод получения списка имеющихся ролей из БД >>>
function loadUserRoles(where) {
    let edit_check_roles;
    if (where == "upd") {
        edit_check_roles = document.getElementById("role_checkbox_upd");
    } else if (where == "del") {
        edit_check_roles = document.getElementById("role_checkbox_del");
    } else {
        edit_check_roles = document.getElementById("role_checkbox_new");
    }
    edit_check_roles.innerHTML = "";

    fetch(URLAllRoles)
        .then(res => res.json())
        .then(roles => {
            roles.forEach(role => {
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


// <<< метод получения пользователя по id >>>
async function getUserById(id) {
    let response = await fetch(URLGetUserById + id);
    return await response.json();
}

// <<< метод получения списка пользователей и заполнения таблицы >>>
function getUsers() {
    fetch(URLListUsers).then(function (response) {
        return response.json();
    }).then(function (users) {
        const users_table_fill = document.getElementById('users_table_fill');
        users_table_fill.innerHTML = "";
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
                                Редактировать
                            </button>
                        </td>
                        <td>
                            <button type="button" 
                            class="btn btn-danger btn-ml" 
                            data-toggle="modal" 
                            data-target="#deleteModal" 
                            onclick="modalDelete(${user.id})">
                                Удалить
                            </button>
                        </td>
                    </tr>`;
            users_table_fill.innerHTML += fill_users;
        }
    })
}

getUsers();
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

// <<< метод заполнения модального окна >>>
async function fill_modal(form, modal, id) {
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

// <<< заполнение модального окна редактирования >>>
async function modalEdit(id) {
    const modalEdit = new bootstrap.Modal(document.querySelector('#modalEdit'));
    await fill_modal(formEdit, modalEdit, id);
    loadUserRoles("upd");
}


// <<< POST метод редактирования >>>
function editUser() {
    formEdit.addEventListener("submit", ev => {
        ev.preventDefault();

        let rolesForEdit = [];
        for (let i = 0; i < formEdit.roles.options.length; i++) {
            if (formEdit.roles.options[i].selected) rolesForEdit.push({
                id: formEdit.roles.options[i].value,
                name: "ROLE_" + formEdit.roles.options[i].text
            });
        }
        console.log(rolesForEdit);
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
            getUsers();
        });
    });
}

editUser();


// <<< заполнение модального окна удаления >>>
async function modalDelete(id) {
    const modalDelete = new bootstrap.Modal(document.querySelector('#modalDelete'));
    await fill_modal(formDelete, modalDelete, id);
    loadUserRoles("del");
}

// <<< POST метод удаления >>>
function deleteUser() {
    formDelete.addEventListener("submit", ev => {
        ev.preventDefault();
        fetch(URLDelete + formDelete.id.value, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(() => {
            $('#deleteClose').click();
            if (formDelete.id.value == currId) {
                window.location.assign(URLLogout);
            }
            getUsers();
        });
    });
}

deleteUser();


// <<< POST метод добавления нового пользователя >>>
function addNew() {
    formNew.addEventListener("submit", ev => {
        ev.preventDefault();

        let rolesForNew = [];
        for (let i = 0; i < formNew.roles.options.length; i++) {
            if (formNew.roles.options[i].selected)
                rolesForNew.push({
                    id: formNew.roles.options[i].value,
                    name: "ROLE_" + formNew.roles.options[i].text

                });
        }
        console.log(rolesForNew);
        fetch(URLAddNew, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: formNew.username.value,
                firstname: formNew.firstname.value,
                lastname: formNew.lastname.value,
                age: formNew.age.value,
                email: formNew.email.value,
                password: formNew.password.value,
                roles: rolesForNew
            })
        }).then(() => {
            formNew.reset();
            getUsers();
            $('#usersTable').click();
        });
    });
}

addNew();
//^^^ --- admin     methods --- ^^^