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

describe('OrderItemNotificationsStatus e2e test', () => {
  const orderItemNotificationsStatusPageUrl = '/order-item-notifications-status';
  const orderItemNotificationsStatusPageUrlPattern = new RegExp('/order-item-notifications-status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const orderItemNotificationsStatusSample = {};

  let orderItemNotificationsStatus: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/order-item-notifications-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-item-notifications-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-item-notifications-statuses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (orderItemNotificationsStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-item-notifications-statuses/${orderItemNotificationsStatus.id}`,
      }).then(() => {
        orderItemNotificationsStatus = undefined;
      });
    }
  });

  it('OrderItemNotificationsStatuses menu should load OrderItemNotificationsStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-item-notifications-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderItemNotificationsStatus').should('exist');
    cy.url().should('match', orderItemNotificationsStatusPageUrlPattern);
  });

  describe('OrderItemNotificationsStatus page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderItemNotificationsStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderItemNotificationsStatus page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/order-item-notifications-status/new$'));
        cy.getEntityCreateUpdateHeading('OrderItemNotificationsStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', orderItemNotificationsStatusPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-item-notifications-statuses',
          body: orderItemNotificationsStatusSample,
        }).then(({ body }) => {
          orderItemNotificationsStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-item-notifications-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/order-item-notifications-statuses?page=0&size=20>; rel="last",<http://localhost/api/order-item-notifications-statuses?page=0&size=20>; rel="first"',
              },
              body: [orderItemNotificationsStatus],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderItemNotificationsStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OrderItemNotificationsStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderItemNotificationsStatus');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', orderItemNotificationsStatusPageUrlPattern);
      });

      it('edit button click should load edit OrderItemNotificationsStatus page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderItemNotificationsStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', orderItemNotificationsStatusPageUrlPattern);
      });

      it('last delete button click should delete instance of OrderItemNotificationsStatus', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orderItemNotificationsStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', orderItemNotificationsStatusPageUrlPattern);

        orderItemNotificationsStatus = undefined;
      });
    });
  });

  describe('new OrderItemNotificationsStatus page', () => {
    beforeEach(() => {
      cy.visit(`${orderItemNotificationsStatusPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderItemNotificationsStatus');
    });

    it('should create an instance of OrderItemNotificationsStatus', () => {
      cy.get(`[data-cy="created"]`).type('2022-01-27T00:05').should('have.value', '2022-01-27T00:05');

      cy.get(`[data-cy="accepted"]`).type('2022-01-26T10:36').should('have.value', '2022-01-26T10:36');

      cy.get(`[data-cy="prepared"]`).type('2022-01-26T07:47').should('have.value', '2022-01-26T07:47');

      cy.get(`[data-cy="served"]`).type('2022-01-26T19:36').should('have.value', '2022-01-26T19:36');

      cy.get(`[data-cy="canceled"]`).type('2022-01-26T14:28').should('have.value', '2022-01-26T14:28');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        orderItemNotificationsStatus = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', orderItemNotificationsStatusPageUrlPattern);
    });
  });
});
