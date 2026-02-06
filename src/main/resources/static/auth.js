// auth.js

const authWrapper = document.querySelector('.auth-wrapper');

// Afficher le message d'erreur s'il existe
document.addEventListener('DOMContentLoaded', function() {
    const params = new URLSearchParams(window.location.search);
    if (params.has('error')) {
        const errorMsg = document.getElementById('errorMsg');
        if (errorMsg) {
            errorMsg.textContent = 'Nom d\'utilisateur ou mot de passe incorrect';
            errorMsg.classList.add('show');
        }
    }
    
    // Ajouter un listener pour gérer les erreurs de soumission
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            // Validation simple
            const username = document.getElementById('username')?.value.trim();
            const password = document.getElementById('password')?.value.trim();
            
            if (!username || !password) {
                e.preventDefault();
                const errorMsg = document.getElementById('errorMsg');
                if (errorMsg) {
                    errorMsg.textContent = 'Veuillez remplir tous les champs';
                    errorMsg.classList.add('show');
                }
            }
        });
    }
});