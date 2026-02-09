// URL de base de l'API
const API_URL = '/api/employes';
const API_ENTREPRISES = '/api/entreprises';
const API_DEPARTEMENTS = '/api/departements';

// Variables globales
let currentEditingMatricule = null;
let allEmployes = [];
let userRole = null;
let allEntreprises = [];
let allDepartements = [];

// Éléments du DOM
const employeForm = document.getElementById('employeForm');
const matriculeInput = document.getElementById('matricule');
const nomInput = document.getElementById('nom');
const prenomInput = document.getElementById('prenom');
const posteInput = document.getElementById('poste');
const entrepriseSelect = document.getElementById('entrepriseSelect');
const departementSelect = document.getElementById('departementSelect');
const submitBtn = document.getElementById('submitBtn');
const refreshBtn = document.getElementById('refreshBtn');
const searchInput = document.getElementById('searchInput');
const employesTableBody = document.getElementById('employesTableBody');
const noDataDiv = document.getElementById('noData');

/**
 * Affiche une notification
 */
function showNotification(message, type) {
    const notification = document.createElement('div');
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 25px;
        background: ${type === 'success' ? '#28a745' : type === 'error' ? '#dc3545' : '#17a2b8'};
        color: white;
        border-radius: 5px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        z-index: 1000;
        animation: slideIn 0.3s ease;
        max-width: 400px;
    `;
    notification.textContent = message;
    document.body.appendChild(notification);

    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

// Initialisation
document.addEventListener('DOMContentLoaded', () => {
    checkAuthentication();
    loadEmployes();
    loadEntreprises();
    loadDepartements();
    employeForm.addEventListener('submit', handleFormSubmit);
    refreshBtn.addEventListener('click', loadEmployes);
    searchInput.addEventListener('input', filterEmployes);

    // Ajout un bouton de reset dans le formulaire si nécessaire
    const resetBtn = document.createElement('button');
    resetBtn.type = 'button';
    resetBtn.className = 'btn btn-secondary';
    resetBtn.innerHTML = '<i class="fas fa-times"></i> Annuler';
    resetBtn.onclick = resetForm;
    document.querySelector('.form-actions').appendChild(resetBtn);
});

/**
 * Vérifie l'authentification et charge les informations de l'utilisateur
 */
async function checkAuthentication() {
    try {
        const response = await fetch('/api/employes/current-user');

        if (response.status === 401) {
            window.location.href = '/auth.html';
            return;
        }

        if (response.ok) {
            const userInfo = await response.json();
            userRole = userInfo.isAdmin ? 'ADMIN' : 'EMPLOYEE';

            if (userRole === 'ADMIN') {
                enableAdminFeatures();
            } else {
                disableAdminFeatures();
            }
        }
    } catch (error) {
        console.error('Erreur lors de la vérification de l\'authentification:', error);
        disableAdminFeatures();
    }
}

/**
 * Active les fonctionnalités d'admin
 */
function enableAdminFeatures() {
    if (employeForm) {
        employeForm.style.display = 'block';
    }
    showNotification('Mode administration - Vous pouvez ajouter, modifier et supprimer', 'success');
}

/**
 * Désactive les fonctionnalités d'admin
 */
function disableAdminFeatures() {
    if (employeForm) {
        employeForm.style.display = 'none';
    }
    showNotification('Mode consultation - Vous êtes employé', 'info');
}

/**
 * Charge tous les employés depuis l'API
 */
async function loadEmployes() {
    try {
        const response = await fetch(API_URL);
        if (response.status === 401) {
            window.location.href = '/auth.html';
            return;
        }
        if (!response.ok) throw new Error('Erreur lors du chargement');

        allEmployes = await response.json();
        displayEmployes(allEmployes);
    } catch (error) {
        console.error('Erreur:', error);
        showNotification('Erreur lors du chargement des employés', 'error');
    }
}

/**
 * Affiche les employés dans le tableau
 */
function displayEmployes(employes) {
    employesTableBody.innerHTML = '';

    if (!employes || employes.length === 0) {
        noDataDiv.classList.add('show');
        return;
    }

    noDataDiv.classList.remove('show');

    employes.forEach(employe => {
        const row = document.createElement('tr');

        // Créer le contenu des actions uniquement si l'utilisateur est admin
        let actionButtons = '';
        if (userRole === 'ADMIN') {
            actionButtons = `
                <div class="action-buttons">
                    <button class="btn btn-edit" onclick="editEmploye(${employe.matricule})">
                        <i class="fas fa-edit"></i> Modifier
                    </button>
                    <button class="btn btn-delete" onclick="deleteEmploye(${employe.matricule})">
                        <i class="fas fa-trash"></i> Supprimer
                    </button>
                </div>
            `;
        } else {
            actionButtons = `<span style="color: #999; font-size: 14px;"><i class="fas fa-eye"></i> Consultation</span>`;
        }

        row.innerHTML = `
            <td>${employe.matricule}</td>
            <td>${employe.nom || ''} ${employe.prenom || ''}</td>
            <td>${employe.poste || ''}</td>
            <td>${employe.entreprise ? employe.entreprise.nom : ''}</td>
            <td>${employe.departement ? employe.departement.nom : ''}</td>
            <td>${actionButtons}</td>
        `;
        employesTableBody.appendChild(row);
    });
}

/**
 * Filtre les employés selon la recherche
 */
function filterEmployes() {
    const searchTerm = (searchInput.value || '').toLowerCase();
    const filtered = allEmployes.filter(employe =>
        (employe.nom && employe.nom.toLowerCase().includes(searchTerm)) ||
        (employe.prenom && employe.prenom.toLowerCase().includes(searchTerm)) ||
        (employe.poste && employe.poste.toLowerCase().includes(searchTerm)) ||
        (employe.entreprise && employe.entreprise.nom && employe.entreprise.nom.toLowerCase().includes(searchTerm)) ||
        (employe.departement && employe.departement.nom && employe.departement.nom.toLowerCase().includes(searchTerm)) ||
        employe.matricule.toString().includes(searchTerm)
    );
    displayEmployes(filtered);
}

/**
 * Gère la soumission du formulaire
 */
async function handleFormSubmit(e) {
    e.preventDefault();

    const nom = nomInput.value.trim();
    const prenom = prenomInput.value.trim();
    const poste = posteInput.value.trim();
    const entrepriseId = parseInt(entrepriseSelect.value);
    const departementId = parseInt(departementSelect.value);

    if (!nom || !prenom || !poste || isNaN(entrepriseId) || isNaN(departementId)) {
        showNotification('Veuillez remplir tous les champs', 'error');
        return;
    }

    if (currentEditingMatricule !== null) {
        await updateEmploye(currentEditingMatricule, { nom, prenom, poste });
    } else {
        await createEmploye({ nom, prenom, poste, entrepriseId, departementId });
    }
}

/**
 * Crée un nouvel employé
 */
async function createEmploye(payload) {
    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.status === 403) {
            showNotification('Accès refusé - Seul l\'admin peut ajouter', 'error');
            return;
        }
        if (response.status === 401) {
            window.location.href = '/auth.html';
            return;
        }
        if (!response.ok) throw new Error('Erreur lors de la création');

        showNotification('Employé ajouté avec succès!', 'success');
        resetForm();
        loadEmployes();
    } catch (error) {
        console.error('Erreur:', error);
        showNotification('Erreur lors de l\'ajout de l\'employé', 'error');
    }
}

/**
 * Charge un employé dans le formulaire pour modification
 */
function editEmploye(matricule) {
    currentEditingMatricule = matricule;
    matriculeInput.value = matricule;
    const emp = allEmployes.find(e => e.matricule === matricule);
    if (emp) {
        nomInput.value = emp.nom || '';
        prenomInput.value = emp.prenom || '';
        posteInput.value = emp.poste || '';
        entrepriseSelect.value = emp.entreprise ? emp.entreprise.id : '';
        departementSelect.value = emp.departement ? emp.departement.id : '';
    }
    submitBtn.innerHTML = '<i class="fas fa-save"></i> Mettre à jour';

    document.querySelector('.form-container').scrollIntoView({ 
        behavior: 'smooth',
        block: 'start'
    });

    nomInput.focus();
}

/**
 * Met à jour un employé
 */
async function updateEmploye(matricule, data) {
    try {
        const response = await fetch(`${API_URL}/${matricule}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.status === 403) {
            showNotification('Accès refusé - Seul l\'admin peut modifier', 'error');
            return;
        }
        if (response.status === 401) {
            window.location.href = '/auth.html';
            return;
        }
        if (!response.ok) throw new Error('Erreur lors de la mise à jour');

        showNotification('Employé mis à jour avec succès!', 'success');
        resetForm();
        loadEmployes();
    } catch (error) {
        console.error('Erreur:', error);
        showNotification('Erreur lors de la mise à jour de l\'employé', 'error');
    }
}

/**
 * Supprime un employé
 */
async function deleteEmploye(matricule) {
    const employe = allEmployes.find(e => e.matricule === matricule);

    if (!confirm(`Êtes-vous sûr de vouloir supprimer l'employé "${employe ? employe.nom : ''}" (Matricule: ${matricule}) ?`)) {
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${matricule}`, {
            method: 'DELETE'
        });

        if (response.status === 403) {
            showNotification('Accès refusé - Seul l\'admin peut supprimer', 'error');
            return;
        }
        if (response.status === 401) {
            window.location.href = '/auth.html';
            return;
        }
        if (!response.ok) throw new Error('Erreur lors de la suppression');

        showNotification('Employé supprimé avec succès!', 'success');
        loadEmployes();
    } catch (error) {
        console.error('Erreur:', error);
        showNotification('Erreur lors de la suppression de l\'employé', 'error');
    }
}

/**
 * Réinitialise le formulaire
 */
function resetForm() {
    employeForm.reset();
    matriculeInput.value = '';
    currentEditingMatricule = null;
    submitBtn.innerHTML = '<i class="fas fa-plus"></i> Ajouter';
    nomInput.focus();
}

/**
 * Charge les entreprises pour le select
 */
async function loadEntreprises() {
    try {
        const res = await fetch(API_ENTREPRISES);
        if (res.ok) {
            const data = await res.json();
            allEntreprises = Array.isArray(data) ? data : (data ? [data] : []);
            entrepriseSelect.innerHTML = '';
            allEntreprises.forEach(e => {
                const opt = document.createElement('option');
                opt.value = e.id;
                opt.textContent = e.nom + ' - ' + (e.adresse || '');
                entrepriseSelect.appendChild(opt);
            });
        }
    } catch (err) {
        console.error('Erreur chargement entreprises', err);
    }
}

/**
 * Charge les départements pour le select
 */
async function loadDepartements() {
    try {
        const res = await fetch(API_DEPARTEMENTS);
        if (res.ok) {
            allDepartements = await res.json();
            departementSelect.innerHTML = '';
            allDepartements.forEach(d => {
                const opt = document.createElement('option');
                opt.value = d.id;
                opt.textContent = d.nom + ' (' + (d.entreprise ? d.entreprise.nom : '') + ')';
                departementSelect.appendChild(opt);
            });
        }
    } catch (err) {
        console.error('Erreur chargement départements', err);
    }
}
