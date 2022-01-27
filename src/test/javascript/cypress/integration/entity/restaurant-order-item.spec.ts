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

describe('RestaurantOrderItem e2e test', () => {
  const restaurantOrderItemPageUrl = '/restaurant-order-item';
  const restaurantOrderItemPageUrlPattern = new RegExp('/restaurant-order-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const restaurantOrderItemSample = {};

  let restaurantOrderItem: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/restaurant-order-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/restaurant-order-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/restaurant-order-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (restaurantOrderItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/restaurant-order-items/${restaurantOrderItem.id}`,
      }).then(() => {
        restaurantOrderItem = undefined;
      });
    }
  });

  it('RestaurantOrderItems menu should load RestaurantOrderItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('restaurant-order-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RestaurantOrderItem').should('exist');
    cy.url().should('match', restaurantOrderItemPageUrlPattern);
  });

  describe('RestaurantOrderItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(restaurantOrderItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RestaurantOrderItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/restaurant-order-item/new$'));
        cy.getEntityCreateUpdateHeading('RestaurantOrderItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', restaurantOrderItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/restaurant-order-items',
          body: restaurantOrderItemSample,
        }).then(({ body }) => {
          restaurantOrderItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/restaurant-order-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/restaurant-order-items?page=0&size=20>; rel="last",<http://localhost/api/restaurant-order-items?page=0&size=20>; rel="first"',
              },
              body: [restaurantOrderItem],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(restaurantOrderItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RestaurantOrderItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('restaurantOrderItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', restaurantOrderItemPageUrlPattern);
      });

      it('edit button click should load edit RestaurantOrderItem page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RestaurantOrderItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', restaurantOrderItemPageUrlPattern);
      });

      it('last delete button click should delete instance of RestaurantOrderItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('restaurantOrderItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', restaurantOrderItemPageUrlPattern);

        restaurantOrderItem = undefined;
      });
    });
  });

  describe('new RestaurantOrderItem page', () => {
    beforeEach(() => {
      cy.visit(`${restaurantOrderItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RestaurantOrderItem');
    });

    it('should create an instance of RestaurantOrderItem', () => {
      cy.get(`[data-cy="name"]`).type('Account').should('have.value', 'Account');

      cy.get(`[data-cy="menuItemId"]`).type('95832').should('have.value', '95832');

      cy.get(`[data-cy="priceExcludingTax"]`).type('78769').should('have.value', '78769');

      cy.get(`[data-cy="priceIncludingTax"]`).type('21770').should('have.value', '21770');

      cy.get(`[data-cy="quantity"]`).type('18390').should('have.value', '18390');

      cy.get(`[data-cy="status"]`).select('SERVED');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        restaurantOrderItem = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', restaurantOrderItemPageUrlPattern);
    });
  });
});
