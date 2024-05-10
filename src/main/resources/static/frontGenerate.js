const URLCurrentUser = "/api/user/getCurrentUser";
const URLListUsers = "/api/admin/getUsers";
const URLGetUserById = "/api/admin/getUser/";
const URLUpdate = "api/admin/update";
const URLDelete = "/api/admin/delete/";
const URLAddNew = "/api/admin/add/";
const URLAllRoles = "/api/admin/findAllRoles";
const URLLogout = "/logout";
let top_panel_info = document.getElementById('top_panel_info');
let table_user_info = document.getElementById('table_user_info');
let side_user_tab = document.getElementById('side-user-tab');
let formEdit = document.forms["formEdit"];
let formDelete = document.forms["formDelete"];
let formNew = document.forms["formNew"];
let currId;
let currUsername;
//vvv --- user  methods --- vvv

// <<< метод получения текущего пользователя >>>
function getCurrentUser() {
    fetch(URLCurrentUser)
        .then((res) => res.json())
        .then((currentUser) => {
            currId = currentUser.id;
            currUsername = currentUser.username;

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
    for (let role of roles) {
        currentRoles += (role.name.toString().substring(5) + " ");
    }
    return currentRoles;
}

/*  ^^^ --- user     methods --- ^^^
----------------------------------------------------------------
    vvv --- admin    methods --- vvv*/

window.addEventListener("load", loadUserRoles);

// <<< метод получения списка имеющихся ролей из БД >>>
async function loadUserRoles(where) {
    let disabled = "";
    let edit_check_roles;
    if (where == "upd") {
        edit_check_roles = await document.getElementById("edit_check_roles");
    } else if (where == "del") {
        edit_check_roles = await document.getElementById("delete_check_roles");
        disabled = "disabled";
    } else {
        edit_check_roles = await document.getElementById("new_check_roles");
        where = "new";
    }
    if (edit_check_roles != null) {
        await edit_check_roles.replaceChildren();
    }

    await fetch(URLAllRoles)
        .then(res => res.json())
        .then(roles => {
            roles.forEach(role => {
                let inputId = role.name+'_'+where;

                edit_check_roles.innerHTML += `
                                <label class="font-weight-bold" 
                                for="${role.name}"
                                >${role.name.substring(5)}</label>
                                <input class="messageCheckbox" 
                                type="checkbox" 
                                value="${role.id}" 
                                id="${inputId}"
                                name="${role.name.substring(5)}"
                                ${disabled}
                                >`;
            });
        });
}


// <<< метод получения пользователя по id >>>
async function getUserById(id) {
    let response = await fetch(URLGetUserById + id);
    return await response.json();
}

// <<< метод получения списка пользователей и заполнения таблицы >>>
function getUsers() {
    // erase();
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


// <<< метод заполнения модального окна >>>
async function fill_modal(form, modal, id, where) {

    modal.show();
    let user = await getUserById(id);
    form.id.value = user.id;
    form.username.value = user.username;
    form.firstname.value = user.firstname;
    form.lastname.value = user.lastname;
    form.age.value = user.age;
    form.email.value = user.email;
    form.password.value = user.password;

    for( let role of user.roles){
        let inputId = role.name+'_'+where;
        if(role.name == "ROLE_ADMIN"){
            await document.getElementById(inputId).click();
        } else if(role.name == "ROLE_USER"){
            await document.getElementById(inputId).click();
        }
    }
}

// <<< заполнение модального окна редактирования >>>
async function modalEdit(id) {
    const modalEdit = new bootstrap.Modal(await document.querySelector('#modalEdit'));
    await loadUserRoles("upd");
    await fill_modal(formEdit, modalEdit, id, "upd");
}


// <<< POST метод редактирования >>>
function editUser() {
    let check400 = false;
    formEdit.addEventListener("submit", ev => {
        ev.preventDefault();
        let usernameField = document.getElementById('username_upd');

        let rolesForEdit = [];
        let inputElements = document.getElementsByClassName('messageCheckbox');
        for (let i = 0; inputElements[i]; ++i) {
            if (inputElements[i].checked) {
                rolesForEdit.push({
                    id: inputElements[i].value,
                    name: "ROLE_" + inputElements[i].name
                })
            }
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
        })
            .then(response => {
                check400 = checkStatus(response, usernameField);
            })
            .then(() => {
                if (check400) {
                    getUsers();
                } else {
                    if (formEdit.id.value == currId
                        && formEdit.username.value != currUsername) {window.location.assign(URLLogout);}
                    let hasAdmin = false;
                    rolesForEdit.forEach(role => {if (role.name == "ROLE_ADMIN"){hasAdmin=true;}})
                    if(formEdit.id.value == currId && rolesForEdit.length>0 && !hasAdmin)
                    {window.location.assign(URLLogout);}
                    document.getElementById('editClose').click();
                    getCurrentUser();
                    getUsers();

                }
            });
    });
}
editUser();


// <<< заполнение модального окна удаления >>>
async function modalDelete(id) {
    const modalDelete = new bootstrap.Modal(await document.querySelector('#modalDelete'));
    await loadUserRoles("del");
    await fill_modal(formDelete, modalDelete, id, "del");
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
            if (formDelete.id.value == currId) {
                window.location.assign(URLLogout);
            }
            document.getElementById('deleteClose').click();
            getUsers();
        });
    });
}
deleteUser();


// <<< POST метод добавления нового пользователя >>>
function addNew() {
    let check400 = false;
    formNew.addEventListener("submit", ev => {
        ev.preventDefault();
        let usernameField = document.getElementById('username_new');

        let rolesForNew = [];
        let inputElements = [];
        inputElements.push(document.getElementById(  'ROLE_ADMIN_new'));
        inputElements.push(document.getElementById(  'ROLE_USER_new'));
        for (let i = 0; inputElements[i]; ++i) {
            if (inputElements[i].checked) {
                rolesForNew.push({
                    id: inputElements[i].value,
                    name: "ROLE_" + inputElements[i].name
                })
            }
        }

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
        })
            .then(response => {
                check400 = checkStatus(response, usernameField);
            })
            .then(() => {
                if (check400) {
                    getUsers();
                } else {
                    formNew.reset();
                    getUsers();
                    document.getElementById('users-list-tab').click();
                }
            });
    });
}
addNew();

function erase(usernameField) {
    usernameField.classList.remove('is-invalid');
    let err;
    if ((err = document.getElementById('errorDiv')) != null) {
        err.remove();
    }
}

function checkStatus(response, usernameField) {
    if (response.status === 400) {
        usernameField.classList.add('is-invalid');
        let errorDiv = document.createElement('div');
        errorDiv.id = 'errorDiv';
        errorDiv.innerText = 'Имя пользователя должно быть уникальным';
        usernameField.parentElement.append(errorDiv);
        setTimeout(() => {
            erase(usernameField);
        }, 3000);
        return true;
    } else {
        return false;
    }
}

//^^^ --- admin     methods --- ^^^