describe('Cenário 2 - Validação de casos de pesquisa com mock de api', () => {

    beforeEach(() => {
        cy.visit('/');
        cy.intercept('GET', '**/api/search*', {fixture:'spotify_res_mock.json'}).as('getSearchMock');
    });
     
    it('CT1 - Pesquisas validas(dados mockados)', () => {
        cy.get('input').type('Qualquer musica');
        cy.get('[data-cy="search-button"]').click();
        cy.wait('@getSearchMock');
        cy.get('[data-cy="track-item"]').should('have.length', 2);
        cy.get('[data-cy="track-item"]').first().should('contain', 'Even Flow').and('contain', 'Pearl Jam');
    });

    it('CT2 - Pesquisa com caracteres especiais', () => {
        cy.intercept('GET', '**/api/search?query=Panic*', {
        statusCode: 200,
        body: [
        {
            "id": "123",
            "name": "Death of a Bachelor",
            "artists": [{ "name": "Panic! At The Disco" }],
            "album": {
            "name": "Death of a Bachelor",
            "images": [{ "url": "https://via.placeholder.com/150" }]
            }
        }
        ]
        }).as('getPanicMock');

        cy.get('[data-cy="search-input"]').type('Panic! At The Disco');
        cy.get('[data-cy="search-button"]').click();
        cy.wait('@getPanicMock');

        cy.get('[data-cy="track-item"]').should('be.visible').and('contain', 'Panic! At The Disco');
    });

    it('CT3 - Pesquisa em branco', () => {
        cy.get('[data-cy="search-button"]').click();
        cy.get('.error-message').should('be.visible').and('contain', 'não pode ser vazio');
    });

    it('CT4 - Validação do comportamento de loading', () => {

        cy.intercept('GET', '**/api/search*', {fixture: 'spotify_res_mock.json', delay: 2000}).as('getSearchDelayed');

        cy.get('input').type('Qualquer musica');
        cy.get('[data-cy="search-button"]').click();
        cy.get('[data-cy="search-button"]').should('be.disabled').and('contain', 'Buscando...');
        cy.wait('@getSearchDelayed');

        cy.get('[data-cy="search-button"]').should('not.be.disabled').and('contain', 'Buscar');
        cy.get('[data-cy="track-item"]').should('have.length', 2);

    });
});