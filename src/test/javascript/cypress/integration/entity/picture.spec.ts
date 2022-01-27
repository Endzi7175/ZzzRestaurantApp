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

describe('Picture e2e test', () => {
  const picturePageUrl = '/picture';
  const picturePageUrlPattern = new RegExp('/picture(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const pictureSample = {};

  let picture: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/pictures+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pictures').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pictures/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (picture) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pictures/${picture.id}`,
      }).then(() => {
        picture = undefined;
      });
    }
  });

  it('Pictures menu should load Pictures page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('picture');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Picture').should('exist');
    cy.url().should('match', picturePageUrlPattern);
  });

  describe('Picture page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(picturePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Picture page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/picture/new$'));
        cy.getEntityCreateUpdateHeading('Picture');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', picturePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pictures',
          body: pictureSample,
        }).then(({ body }) => {
          picture = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pictures+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/pictures?page=0&size=20>; rel="last",<http://localhost/api/pictures?page=0&size=20>; rel="first"',
              },
              body: [picture],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(picturePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Picture page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('picture');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', picturePageUrlPattern);
      });

      it('edit button click should load edit Picture page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Picture');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', picturePageUrlPattern);
      });

      it('last delete button click should delete instance of Picture', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('picture').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', picturePageUrlPattern);

        picture = undefined;
      });
    });
  });

  describe('new Picture page', () => {
    beforeEach(() => {
      cy.visit(`${picturePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Picture');
    });

    it('should create an instance of Picture', () => {
      cy.get(`[data-cy="name"]`).type('Tactics Steel').should('have.value', 'Tactics Steel');

      cy.get(`[data-cy="pictureUrl"]`).type('copying').should('have.value', 'copying');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        picture = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', picturePageUrlPattern);
    });
  });
});
