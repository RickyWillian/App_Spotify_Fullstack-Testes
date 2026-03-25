describe('Cenário 2 - Validação de casos de pesquisa', () => {
  
  beforeEach(() => {
    
    cy.visit('/'); 
    cy.intercept('GET', '**/api/search*').as('getSearch');
  });

  it('CT1 - Pesquisas validas (digitando o nome)', () => {

    cy.get('input').type('Even Flow');
    cy.get('[data-cy="search-button"]').click();

    cy.wait('@getSearch').its('response.statusCode').should('eq',200);

    cy.get('[data-cy="track-item"]').first().should('be.visible');
  });

  it('CT2 - Pesquisas validas (com o uso de json)', () => {
    cy.fixture('musicas').then((massa) => {

        cy.get('input').type(massa.buscaValida.termo);
        cy.get('[data-cy="search-button"]').click();

        cy.wait('@getSearch').its('response.statusCode').should('eq',200);

        cy.get('[data-cy="track-item"]').first().should('contain', massa.buscaValida.artistaEsperado);
    });
  });

  it('CT3 - Pesquisa com caracteres especiais (uso de json)', () => {
    cy.fixture('musicas').then((massa) =>{

        cy.get('input').type(massa.buscaEspecial.termo);
        cy.get('[data-cy="search-button"]').click();
        cy.wait('@getSearch').its('response.statusCode').should('eq',200);

        cy.get('[data-cy="track-item"]').first().should('contain', massa.buscaEspecial.artistaEsperado);
    
    });
  });

  it('CT4 - Pesquisa em branco', () => {
    cy.get('[data-cy="search-button"]').click();
    cy.get('.error-message').should('be.visible').and('contain', 'não pode ser vazio');
  });

  it('CT5 - Validação do comportamento de loading', () => {
    cy.fixture('musicas').then((massa) => {

        cy.intercept('GET', '**/api/search*', { delay: 2000 }).as('getSearchDelayed');
        cy.get('input').type(massa.buscaValida.termo);
        cy.get('[data-cy="search-button"]').click();

        cy.get('[data-cy="search-button"]').should('be.disabled').and('contain', 'Buscando...');
        cy.wait('@getSearchDelayed');

        cy.get('[data-cy="search-button"]').should('not.be.disabled').and('contain', 'Buscar');

    });
  });
});