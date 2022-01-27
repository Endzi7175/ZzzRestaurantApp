import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Menu e2e test', () => {
  const menuPageUrl = '/menu';
  const menuPageUrlPattern = new RegExp('/menu(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const menuSample = {};

  let menu: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/menus+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/menus').as('postEntityRequest');
    cy.intercept('DELETE', '/api/menus/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (menu) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/menus/${menu.id}`,
      }).then(() => {
        menu = undefined;
      });
    }
  });

  it('Menus menu should load Menus page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('menu');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Menu').should('exist');
    cy.url().should('match', menuPageUrlPattern);
  });

  describe('Menu page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(menuPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Menu page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/menu/new$'));
        cy.getEntityCreateUpdateHeading('Menu');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', menuPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/menus',
          body: menuSample,
        }).then(({ body }) => {
          menu = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/menus+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/menus?page=0&size=20>; rel="last",<http://localhost/api/menus?page=0&size=20>; rel="first"',
              },
              body: [menu],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(menuPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Menu page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('menu');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', menuPageUrlPattern);
      });

      it('edit button click should load edit Menu page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Menu');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', menuPageUrlPattern);
      });

      it('last delete button click should delete instance of Menu', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('menu').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', menuPageUrlPattern);

        menu = undefined;
      });
    });
  });

  describe('new Menu page', () => {
    beforeEach(() => {
      cy.visit(`${menuPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Menu');
    });

    it('should create an instance of Menu', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        menu = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', menuPageUrlPattern);
    });
  });
});
