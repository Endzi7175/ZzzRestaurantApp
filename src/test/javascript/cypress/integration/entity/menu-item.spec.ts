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

describe('MenuItem e2e test', () => {
  const menuItemPageUrl = '/menu-item';
  const menuItemPageUrlPattern = new RegExp('/menu-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const menuItemSample = {};

  let menuItem: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/menu-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/menu-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/menu-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (menuItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/menu-items/${menuItem.id}`,
      }).then(() => {
        menuItem = undefined;
      });
    }
  });

  it('MenuItems menu should load MenuItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('menu-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MenuItem').should('exist');
    cy.url().should('match', menuItemPageUrlPattern);
  });

  describe('MenuItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(menuItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MenuItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/menu-item/new$'));
        cy.getEntityCreateUpdateHeading('MenuItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', menuItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/menu-items',
          body: menuItemSample,
        }).then(({ body }) => {
          menuItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/menu-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/menu-items?page=0&size=20>; rel="last",<http://localhost/api/menu-items?page=0&size=20>; rel="first"',
              },
              body: [menuItem],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(menuItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MenuItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('menuItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', menuItemPageUrlPattern);
      });

      it('edit button click should load edit MenuItem page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MenuItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', menuItemPageUrlPattern);
      });

      it('last delete button click should delete instance of MenuItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('menuItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', menuItemPageUrlPattern);

        menuItem = undefined;
      });
    });
  });

  describe('new MenuItem page', () => {
    beforeEach(() => {
      cy.visit(`${menuItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MenuItem');
    });

    it('should create an instance of MenuItem', () => {
      cy.get(`[data-cy="name"]`).type('Guadeloupe Liberia Intuitive').should('have.value', 'Guadeloupe Liberia Intuitive');

      cy.get(`[data-cy="description"]`).type('Harbors').should('have.value', 'Harbors');

      cy.get(`[data-cy="price"]`).type('51334').should('have.value', '51334');

      cy.get(`[data-cy="prepareTime"]`).type('66269').should('have.value', '66269');

      cy.get(`[data-cy="type"]`).select('FOOD');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        menuItem = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', menuItemPageUrlPattern);
    });
  });
});
