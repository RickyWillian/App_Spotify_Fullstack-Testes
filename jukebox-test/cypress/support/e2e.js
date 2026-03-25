// ***********************************************************
// This example support/e2e.js is processed and
// loaded automatically before your test files.
//
// This is a great place to put global configuration and
// behavior that modifies Cypress.
//
// You can change the location of this file or turn off
// automatically serving support files with the
// 'supportFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/configuration
// ***********************************************************

// Import commands.js using ES2015 syntax:
import './commands'

let isBackendReady = false;

before(() => {
  if (!isBackendReady) {
    // Espera apenas na primeira vez que a suíte rodar no Docker
    cy.log('Aguardando o Spring Boot iniciar...');
    cy.wait(15000); 
    isBackendReady = true;
  }
});