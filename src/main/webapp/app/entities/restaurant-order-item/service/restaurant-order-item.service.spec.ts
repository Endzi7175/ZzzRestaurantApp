import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';
import { IRestaurantOrderItem, RestaurantOrderItem } from '../restaurant-order-item.model';

import { RestaurantOrderItemService } from './restaurant-order-item.service';

describe('RestaurantOrderItem Service', () => {
  let service: RestaurantOrderItemService;
  let httpMock: HttpTestingController;
  let elemDefault: IRestaurantOrderItem;
  let expectedResult: IRestaurantOrderItem | IRestaurantOrderItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RestaurantOrderItemService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      menuItemId: 0,
      priceExcludingTax: 0,
      priceIncludingTax: 0,
      quantity: 0,
      status: OrderItemStatus.CREATED,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a RestaurantOrderItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new RestaurantOrderItem()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RestaurantOrderItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          menuItemId: 1,
          priceExcludingTax: 1,
          priceIncludingTax: 1,
          quantity: 1,
          status: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RestaurantOrderItem', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          menuItemId: 1,
          priceExcludingTax: 1,
          priceIncludingTax: 1,
          quantity: 1,
        },
        new RestaurantOrderItem()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RestaurantOrderItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          menuItemId: 1,
          priceExcludingTax: 1,
          priceIncludingTax: 1,
          quantity: 1,
          status: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a RestaurantOrderItem', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRestaurantOrderItemToCollectionIfMissing', () => {
      it('should add a RestaurantOrderItem to an empty array', () => {
        const restaurantOrderItem: IRestaurantOrderItem = { id: 123 };
        expectedResult = service.addRestaurantOrderItemToCollectionIfMissing([], restaurantOrderItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restaurantOrderItem);
      });

      it('should not add a RestaurantOrderItem to an array that contains it', () => {
        const restaurantOrderItem: IRestaurantOrderItem = { id: 123 };
        const restaurantOrderItemCollection: IRestaurantOrderItem[] = [
          {
            ...restaurantOrderItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addRestaurantOrderItemToCollectionIfMissing(restaurantOrderItemCollection, restaurantOrderItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RestaurantOrderItem to an array that doesn't contain it", () => {
        const restaurantOrderItem: IRestaurantOrderItem = { id: 123 };
        const restaurantOrderItemCollection: IRestaurantOrderItem[] = [{ id: 456 }];
        expectedResult = service.addRestaurantOrderItemToCollectionIfMissing(restaurantOrderItemCollection, restaurantOrderItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restaurantOrderItem);
      });

      it('should add only unique RestaurantOrderItem to an array', () => {
        const restaurantOrderItemArray: IRestaurantOrderItem[] = [{ id: 123 }, { id: 456 }, { id: 25390 }];
        const restaurantOrderItemCollection: IRestaurantOrderItem[] = [{ id: 123 }];
        expectedResult = service.addRestaurantOrderItemToCollectionIfMissing(restaurantOrderItemCollection, ...restaurantOrderItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const restaurantOrderItem: IRestaurantOrderItem = { id: 123 };
        const restaurantOrderItem2: IRestaurantOrderItem = { id: 456 };
        expectedResult = service.addRestaurantOrderItemToCollectionIfMissing([], restaurantOrderItem, restaurantOrderItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restaurantOrderItem);
        expect(expectedResult).toContain(restaurantOrderItem2);
      });

      it('should accept null and undefined values', () => {
        const restaurantOrderItem: IRestaurantOrderItem = { id: 123 };
        expectedResult = service.addRestaurantOrderItemToCollectionIfMissing([], null, restaurantOrderItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restaurantOrderItem);
      });

      it('should return initial array if no RestaurantOrderItem is added', () => {
        const restaurantOrderItemCollection: IRestaurantOrderItem[] = [{ id: 123 }];
        expectedResult = service.addRestaurantOrderItemToCollectionIfMissing(restaurantOrderItemCollection, undefined, null);
        expect(expectedResult).toEqual(restaurantOrderItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
