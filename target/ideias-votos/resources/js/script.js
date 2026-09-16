// Confirmação simples antes de votar, evitando cliques acidentais
document.addEventListener('DOMContentLoaded', function () {
    const formVoto = document.querySelector('form[action$="/votar"]');
    if (formVoto) {
        formVoto.addEventListener('submit', function (e) {
            const confirmar = confirm('Confirmar voto nesta ideia?');
            if (!confirmar) {
                e.preventDefault();
            }
        });
    }
});