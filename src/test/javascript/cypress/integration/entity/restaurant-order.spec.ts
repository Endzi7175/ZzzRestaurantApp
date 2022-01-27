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

describe('RestaurantOrder e2e test', () => {
  const restaurantOrderPageUrl = '/restaurant-order';
  const restaurantOrderPageUrlPattern = new RegExp('/restaurant-order(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const restaurantOrderSample = {};

  let restaurantOrder: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/restaurant-orders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/restaurant-orders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/restaurant-orders/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (restaurantOrder) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/restaurant-orders/${restaurantOrder.id}`,
      }).then(() => {
        restaurantOrder = undefined;
      });
    }
  });

  it('RestaurantOrders menu should load RestaurantOrders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('restaurant-order');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RestaurantOrder').should('exist');
    cy.url().should('match', restaurantOrderPageUrlPattern);
  });

  describe('RestaurantOrder page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(restaurantOrderPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RestaurantOrder page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/restaurant-order/new$'));
        cy.getEntityCreateUpdateHeading('RestaurantOrder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', restaurantOrderPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/restaurant-orders',
          body: restaurantOrderSample,
        }).then(({ body }) => {
          restaurantOrder = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/restaurant-orders+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/restaurant-orders?page=0&size=20>; rel="last",<http://localhost/api/restaurant-orders?page=0&size=20>; rel="first"',
              },
              body: [restaurantOrder],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(restaurantOrderPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RestaurantOrder page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('restaurantOrder');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', restaurantOrderPageUrlPattern);
      });

      it('edit button click should load edit RestaurantOrder page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RestaurantOrder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', restaurantOrderPageUrlPattern);
      });

      it('last delete button click should delete instance of RestaurantOrder', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('restaurantOrder').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', restaurantOrderPageUrlPattern);

        restaurantOrder = undefined;
      });
    });
  });

  describe('new RestaurantOrder page', () => {
    beforeEach(() => {
      cy.visit(`${restaurantOrderPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RestaurantOrder');
    });

    it('should create an instance of RestaurantOrder', () => {
      cy.get(`[data-cy="date"]`).type('2022-01-26T09:31').should('have.value', '2022-01-26T09:31');

      cy.get(`[data-cy="priceExcludingTax"]`).type('86905').should('have.value', '86905');

      cy.get(`[data-cy="priceIncludingTax"]`).type('48531').should('have.value', '48531');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        restaurantOrder = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', restaurantOrderPageUrlPattern);
    });
  });
});
