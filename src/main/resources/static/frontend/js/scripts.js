document.addEventListener('DOMContentLoaded', function () {
    const createGenericUserForm = document.getElementById('createGenericUserForm');
    const createStaffUserForm = document.getElementById('createStaffUserForm');
    const showGenericUsersBtn = document.getElementById('showGenericUsersBtn');
    const showStaffUsersBtn = document.getElementById('showStaffUsersBtn');
    const genericUsersTableBody = document.querySelector('#genericUsersTable tbody');
    const staffUsersTableBody = document.querySelector('#staffUsersTable tbody');

    // Elementi per i Bottoni e le Tabelle
    const viewProductsBtn = document.getElementById('viewProductsBtn');
    const viewTraceabilityBtn = document.getElementById('viewTraceabilityBtn');
    // Rimosso l'elemento 'registerBtn'
    const productsInfoTable = document.getElementById('productsInfoTable');
    const productsInfoTableBody = productsInfoTable.querySelector('tbody');
    const traceabilityTable = document.getElementById('traceabilityTable');
    const traceabilityTableBody = traceabilityTable.querySelector('tbody');

    // Gestione della creazione di un nuovo Utente Generico
    createGenericUserForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const name = document.getElementById('genericName').value.trim();
        const lastname = document.getElementById('genericLastname').value.trim();
        const address = document.getElementById('genericAddress').value.trim();
        const dateOfBirth = document.getElementById('genericDateOfBirth').value.trim();

        if (!name || !lastname || !address || !dateOfBirth) {
            alert('Tutti i campi sono obbligatori.');
            return;
        }

        const formData = { name, lastname, address, dateOfBirth };

        fetch('/api/generic-user/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
        })
            .then(response => {
                if (response.ok) {
                    return response.text().then(text => {
                        alert(text);
                        createGenericUserForm.reset();
                        showGenericUsersBtn.click();
                    });
                } else {
                    return response.json().then(err => {
                        alert('Errore: ' + JSON.stringify(err));
                    });
                }
            })
            .catch(error => {
                console.error('Errore:', error);
                alert('Errore nella creazione dell\'utente generico. Controlla la console.');
            });
    });

    // Gestione della creazione di un nuovo Staff User
    createStaffUserForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const formData = {
            name: document.getElementById('staffName').value,
            lastname: document.getElementById('staffLastname').value,
            address: document.getElementById('staffAddress').value,
            role: document.getElementById('staffRole').value,
            dateOfBirth: document.getElementById('staffDateOfBirth').value
        };

        fetch('/api/staff-user/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        })
            .then(response => {
                if (response.ok) {
                    return response.text().then(text => {
                        alert(text);
                        createStaffUserForm.reset();
                        // Aggiorna la lista degli staff user dopo la creazione
                        showStaffUsersBtn.click();
                    });
                } else {
                    return response.json().then(err => {
                        alert('Errore: ' + JSON.stringify(err));
                    });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Errore nella creazione dello Staff User.');
            });
    });

    // Gestione della visualizzazione degli Utenti Generici
    let isGenericUsersTableVisible = false; // Variabile per tracciare la visibilità della tabella

    showGenericUsersBtn.addEventListener('click', function () {
        if (isGenericUsersTableVisible) {
            // Nascondi la tabella
            genericUsersTableBody.innerHTML = ''; // Pulisci la tabella
            genericUsersTableBody.parentElement.style.display = 'none'; // Nascondi il corpo della tabella
            showGenericUsersBtn.textContent = 'Mostra Utenti Generici';
        } else {
            // Mostra la tabella e carica i dati
            genericUsersTableBody.parentElement.style.display = 'table'; // Mostra il corpo della tabella
            showGenericUsersBtn.textContent = 'Nascondi Utenti Generici';

            fetch('/api/generic-user/list')
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`Errore durante il recupero degli utenti generici: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    // Pulisce la tabella
                    genericUsersTableBody.innerHTML = '';

                    data.forEach(user => {
                        const row = document.createElement('tr');

                        // User ID
                        const userIdCell = document.createElement('td');
                        userIdCell.textContent = user.userId || 'N/D';
                        row.appendChild(userIdCell);

                        // Nome
                        const nameCell = document.createElement('td');
                        nameCell.textContent = user.name || 'N/D';
                        row.appendChild(nameCell);

                        // Cognome
                        const lastnameCell = document.createElement('td');
                        lastnameCell.textContent = user.lastname || 'N/D';
                        row.appendChild(lastnameCell);

                        // Indirizzo
                        const addressCell = document.createElement('td');
                        addressCell.textContent = user.address || 'N/D';
                        row.appendChild(addressCell);

                        // Data di Nascita
                        const dateOfBirthCell = document.createElement('td');
                        dateOfBirthCell.textContent = user.dateOfBirth || 'N/D';
                        row.appendChild(dateOfBirthCell);

                        // Pulsante "Registrati come Acquirente"
                        const registratiCell = document.createElement('td');
                        const registratiButton = document.createElement('button');
                        registratiButton.textContent = 'Registrati come Acquirente';
                        registratiButton.classList.add('registrati-btn');
                        registratiButton.setAttribute('data-user-id', user.userId);
                        registratiCell.appendChild(registratiButton);
                        row.appendChild(registratiCell);

                        // Pulsante "Login"
                        const loginCell = document.createElement('td');
                        const loginButton = document.createElement('button');
                        loginButton.textContent = 'Login';
                        loginButton.classList.add('login-btn');
                        loginButton.setAttribute('data-user-id', user.userId);
                        loginCell.appendChild(loginButton);
                        row.appendChild(loginCell);

                        genericUsersTableBody.appendChild(row);
                    });
                })
                .catch(error => {
                    console.error('Errore:', error);
                    alert('Errore nel recupero degli utenti generici. Controlla la console.');
                });
        }
        isGenericUsersTableVisible = !isGenericUsersTableVisible; // Inverti lo stato di visibilità
    });

    // Gestione della visualizzazione degli Staff User
    let isStaffUsersTableVisible = false; // Variabile per tracciare la visibilità della tabella

    showStaffUsersBtn.addEventListener('click', function () {
        if (isStaffUsersTableVisible) {
            // Nascondi la tabella
            staffUsersTableBody.innerHTML = ''; // Pulisci la tabella
            staffUsersTableBody.parentElement.style.display = 'none'; // Nascondi il corpo della tabella
            showStaffUsersBtn.textContent = 'Mostra Staff User';
        } else {
            // Mostra la tabella e carica i dati
            staffUsersTableBody.parentElement.style.display = 'table'; // Mostra il corpo della tabella
            showStaffUsersBtn.textContent = 'Nascondi Staff User';

            fetch('/api/staff-user/list')
                .then(response => response.json())
                .then(data => {
                    // Pulisce la tabella
                    staffUsersTableBody.innerHTML = '';

                    data.forEach(user => {
                        const row = document.createElement('tr');

                        // User ID
                        const userIdCell = document.createElement('td');
                        userIdCell.textContent = user.userId;
                        row.appendChild(userIdCell);

                        // Nome
                        const nameCell = document.createElement('td');
                        nameCell.textContent = user.name;
                        row.appendChild(nameCell);

                        // Cognome
                        const lastnameCell = document.createElement('td');
                        lastnameCell.textContent = user.lastname;
                        row.appendChild(lastnameCell);

                        // Indirizzo
                        const addressCell = document.createElement('td');
                        addressCell.textContent = user.address;
                        row.appendChild(addressCell);

                        // Ruolo
                        const roleCell = document.createElement('td');
                        roleCell.textContent = user.role;
                        row.appendChild(roleCell);

                        // Data di Nascita
                        const dateOfBirthCell = document.createElement('td');
                        dateOfBirthCell.textContent = user.dateOfBirth;
                        row.appendChild(dateOfBirthCell);

                        // Codice
                        const codiceCell = document.createElement('td');
                        codiceCell.textContent = user.codice;
                        row.appendChild(codiceCell);

                        // Registrati - Nuova Colonna con Pulsante
                        const registratiCell = document.createElement('td');
                        const registratiButton = document.createElement('button');
                        registratiButton.textContent = 'Registrati'; // Puoi decidere se modificare anche questo
                        registratiButton.classList.add('registrati-btn');
                        registratiButton.setAttribute('data-user-id', user.userId); // Attributo dati per identificare l'utente
                        registratiCell.appendChild(registratiButton);
                        row.appendChild(registratiCell);

                        // Login - Nuova Colonna con Pulsante
                        const loginCell = document.createElement('td');
                        const loginButton = document.createElement('button');
                        loginButton.textContent = 'Login';
                        loginButton.classList.add('login-btn');
                        loginButton.setAttribute('data-user-id', user.userId); // Attributo dati per identificare l'utente
                        loginCell.appendChild(loginButton);
                        row.appendChild(loginCell);

                        staffUsersTableBody.appendChild(row);
                    });
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Errore nel recupero degli Staff User.');
                });
        }
        isStaffUsersTableVisible = !isStaffUsersTableVisible; // Inverti lo stato di visibilità
    });


    // Gestione della visualizzazione della Tabella "Visualizza Prodotti e Info"
    viewProductsBtn.addEventListener('click', function () {
        // Toggle visibilità della tabella
        if (productsInfoTable.style.display === 'none') {
            productsInfoTable.style.display = 'table';
            // Nasconde eventualmente altre tabelle
            traceabilityTable.style.display = 'none';

            // (In futuro) Implementare la logica per popolare la tabella
        } else {
            productsInfoTable.style.display = 'none';
        }
    });

    // Gestione della visualizzazione della Tabella "Visualizza Tracciabilità"
    viewTraceabilityBtn.addEventListener('click', function () {
        // Toggle visibilità della tabella
        if (traceabilityTable.style.display === 'none') {
            traceabilityTable.style.display = 'table';
            // Nasconde eventualmente altre tabelle
            productsInfoTable.style.display = 'none';

            // (In futuro) Implementare la logica per popolare la tabella
        } else {
            traceabilityTable.style.display = 'none';
        }
    });

    // Event Delegation per Gestire i Clic sui Pulsanti "Registrati" negli Utenti Generici
    genericUsersTableBody.addEventListener('click', function (event) {
        if (event.target && event.target.matches('button.registrati-btn')) {
            const userId = event.target.getAttribute('data-user-id');
            if (!userId) {
                alert('Errore: ID utente non trovato.');
                return;
            }
            // Reindirizza alla pagina di registrazione, passando userId come query param
            // Esempio: /frontend/registerBuyer.html?userId=123
            window.location.href = `/frontend/registerBuyer.html?userId=${userId}`;
        }
    });





    // Event Delegation per Gestire i Clic sui Pulsanti "Registrati" negli Staff User
    staffUsersTableBody.addEventListener('click', function(event) {
        if (event.target && event.target.matches('button.registrati-btn')) {
            const userId = event.target.getAttribute('data-user-id');
            if (!userId) {
                alert('Errore: ID Staff User non trovato.');
                return;
            }
            // Reindirizza alla pagina di registrazione staff, passando l'ID
            window.location.href = `/frontend/registerStaff.html?userId=${userId}`;
        }
    });

});
