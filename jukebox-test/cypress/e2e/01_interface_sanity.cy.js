describe('Cenário 1 - Validação da Interface (Smoke Test)', () => {
  
  beforeEach(() => {
    // O '/' aqui vai usar a baseUrl que você configurou
    cy.visit('/'); 
  });

  it('CT1 - Validação do titulo da pagina', () => {
    cy.get('body').should('be.visible');
    cy.get('h1').should('exist').and('be.visible');
    cy.get('h1').should('contain', 'Jukebox Spotify');
  });

  it('CT2 - Validação da barra de pesquisa', () => {
    cy.get('input[placeholder*="Buscar por músicas..."]').should('be.visible');
  })

  it("CT3 - Validação do botão de pesquisa", () => {
    cy.get('[data-cy="search-button"]').should('not.be.disabled');
  })
});