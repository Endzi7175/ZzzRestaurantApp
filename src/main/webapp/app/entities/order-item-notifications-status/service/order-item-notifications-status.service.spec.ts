import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOrderItemNotificationsStatus, OrderItemNotificationsStatus } from '../order-item-notifications-status.model';

import { OrderItemNotificationsStatusService } from './order-item-notifications-status.service';

describe('OrderItemNotificationsStatus Service', () => {
  let service: OrderItemNotificationsStatusService;
  let httpMock: HttpTestingController;
  let elemDefault: IOrderItemNotificationsStatus;
  let expectedResult: IOrderItemNotificationsStatus | IOrderItemNotificationsStatus[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrderItemNotificationsStatusService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      created: currentDate,
      accepted: currentDate,
      prepared: currentDate,
      served: currentDate,
      canceled: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          created: currentDate.format(DATE_TIME_FORMAT),
          accepted: currentDate.format(DATE_TIME_FORMAT),
          prepared: currentDate.format(DATE_TIME_FORMAT),
          served: currentDate.format(DATE_TIME_FORMAT),
          canceled: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a OrderItemNotificationsStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          created: currentDate.format(DATE_TIME_FORMAT),
          accepted: currentDate.format(DATE_TIME_FORMAT),
          prepared: currentDate.format(DATE_TIME_FORMAT),
          served: currentDate.format(DATE_TIME_FORMAT),
          canceled: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          created: currentDate,
          accepted: currentDate,
          prepared: currentDate,
          served: currentDate,
          canceled: currentDate,
        },
        returnedFromService
      );

      service.create(new OrderItemNotificationsStatus()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderItemNotificationsStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          created: currentDate.format(DATE_TIME_FORMAT),
          accepted: currentDate.format(DATE_TIME_FORMAT),
          prepared: currentDate.format(DATE_TIME_FORMAT),
          served: currentDate.format(DATE_TIME_FORMAT),
          canceled: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          created: currentDate,
          accepted: currentDate,
          prepared: currentDate,
          served: currentDate,
          canceled: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderItemNotificationsStatus', () => {
      const patchObject = Object.assign(
        {
          accepted: currentDate.format(DATE_TIME_FORMAT),
          prepared: currentDate.format(DATE_TIME_FORMAT),
        },
        new OrderItemNotificationsStatus()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          created: currentDate,
          accepted: currentDate,
          prepared: currentDate,
          served: currentDate,
          canceled: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderItemNotificationsStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          created: currentDate.format(DATE_TIME_FORMAT),
          accepted: currentDate.format(DATE_TIME_FORMAT),
          prepared: currentDate.format(DATE_TIME_FORMAT),
          served: currentDate.format(DATE_TIME_FORMAT),
          canceled: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          created: currentDate,
          accepted: currentDate,
          prepared: currentDate,
          served: currentDate,
          canceled: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a OrderItemNotificationsStatus', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOrderItemNotificationsStatusToCollectionIfMissing', () => {
      it('should add a OrderItemNotificationsStatus to an empty array', () => {
        const orderItemNotificationsStatus: IOrderItemNotificationsStatus = { id: 123 };
        expectedResult = service.addOrderItemNotificationsStatusToCollectionIfMissing([], orderItemNotificationsStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orderItemNotificationsStatus);
      });

      it('should not add a OrderItemNotificationsStatus to an array that contains it', () => {
        const orderItemNotificationsStatus: IOrderItemNotificationsStatus = { id: 123 };
        const orderItemNotificationsStatusCollection: IOrderItemNotificationsStatus[] = [
          {
            ...orderItemNotificationsStatus,
          },
          { id: 456 },
        ];
        expectedResult = service.addOrderItemNotificationsStatusToCollectionIfMissing(
          orderItemNotificationsStatusCollection,
          orderItemNotificationsStatus
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderItemNotificationsStatus to an array that doesn't contain it", () => {
        const orderItemNotificationsStatus: IOrderItemNotificationsStatus = { id: 123 };
        const orderItemNotificationsStatusCollection: IOrderItemNotificationsStatus[] = [{ id: 456 }];
        expectedResult = service.addOrderItemNotificationsStatusToCollectionIfMissing(
          orderItemNotificationsStatusCollection,
          orderItemNotificationsStatus
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderItemNotificationsStatus);
      });

      it('should add only unique OrderItemNotificationsStatus to an array', () => {
        const orderItemNotificationsStatusArray: IOrderItemNotificationsStatus[] = [{ id: 123 }, { id: 456 }, { id: 28862 }];
        const orderItemNotificationsStatusCollection: IOrderItemNotificationsStatus[] = [{ id: 123 }];
        expectedResult = service.addOrderItemNotificationsStatusToCollectionIfMissing(
          orderItemNotificationsStatusCollection,
          ...orderItemNotificationsStatusArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderItemNotificationsStatus: IOrderItemNotificationsStatus = { id: 123 };
        const orderItemNotificationsStatus2: IOrderItemNotificationsStatus = { id: 456 };
        expectedResult = service.addOrderItemNotificationsStatusToCollectionIfMissing(
          [],
          orderItemNotificationsStatus,
          orderItemNotificationsStatus2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderItemNotificationsStatus);
        expect(expectedResult).toContain(orderItemNotificationsStatus2);
      });

      it('should accept null and undefined values', () => {
        const orderItemNotificationsStatus: IOrderItemNotificationsStatus = { id: 123 };
        expectedResult = service.addOrderItemNotificationsStatusToCollectionIfMissing([], null, orderItemNotificationsStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orderItemNotificationsStatus);
      });

      it('should return initial array if no OrderItemNotificationsStatus is added', () => {
        const orderItemNotificationsStatusCollection: IOrderItemNotificationsStatus[] = [{ id: 123 }];
        expectedResult = service.addOrderItemNotificationsStatusToCollectionIfMissing(
          orderItemNotificationsStatusCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(orderItemNotificationsStatusCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
