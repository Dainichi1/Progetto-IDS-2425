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

        const formData = {
            name: document.getElementById('genericName').value,
            lastname: document.getElementById('genericLastname').value,
            address: document.getElementById('genericAddress').value,
            dateOfBirth: document.getElementById('genericDateOfBirth').value
        };

        fetch('/api/generic-user/create', {
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
                        createGenericUserForm.reset();
                        // Aggiorna la lista degli utenti generici dopo la creazione
                        showGenericUsersBtn.click();
                    });
                } else {
                    return response.json().then(err => {
                        alert('Errore: ' + JSON.stringify(err));
                    });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Errore nella creazione dell\'utente generico.');
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
    showGenericUsersBtn.addEventListener('click', function () {
        fetch('/api/generic-user/list')
            .then(response => response.json())
            .then(data => {
                // Pulisce la tabella
                genericUsersTableBody.innerHTML = '';

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

                    // Data di Nascita
                    const dateOfBirthCell = document.createElement('td');
                    dateOfBirthCell.textContent = user.dateOfBirth;
                    row.appendChild(dateOfBirthCell);

                    // Registrati - Nuova Colonna con Pulsante
                    const registratiCell = document.createElement('td');
                    const registratiButton = document.createElement('button');
                    registratiButton.textContent = 'Registrati come Acquirente'; // Aggiornato
                    registratiButton.classList.add('registrati-btn');
                    registratiButton.setAttribute('data-user-id', user.userId); // Attributo dati per identificare l'utente
                    registratiCell.appendChild(registratiButton);
                    row.appendChild(registratiCell);

                    genericUsersTableBody.appendChild(row);
                });
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Errore nel recupero degli utenti generici.');
            });
    });

    // Gestione della visualizzazione degli Staff User
    showStaffUsersBtn.addEventListener('click', function () {
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

                    staffUsersTableBody.appendChild(row);
                });
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Errore nel recupero degli Staff User.');
            });
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
    genericUsersTableBody.addEventListener('click', function(event) {
        if (event.target && event.target.matches('button.registrati-btn')) {
            const userId = event.target.getAttribute('data-user-id');
            // Implementa la logica di registrazione qui in futuro
            alert(`Registrazione come Acquirente per l'utente con ID: ${userId}`);
        }
    });

    // Event Delegation per Gestire i Clic sui Pulsanti "Registrati" negli Staff User
    staffUsersTableBody.addEventListener('click', function(event) {
        if (event.target && event.target.matches('button.registrati-btn')) {
            const userId = event.target.getAttribute('data-user-id');
            // Implementa la logica di registrazione qui in futuro
            alert(`Registrazione per lo Staff User con ID: ${userId}`);
        }
    });
});
