import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IOrderItemNotificationsStatus, OrderItemNotificationsStatus } from '../order-item-notifications-status.model';
import { OrderItemNotificationsStatusService } from '../service/order-item-notifications-status.service';

import { OrderItemNotificationsStatusRoutingResolveService } from './order-item-notifications-status-routing-resolve.service';

describe('OrderItemNotificationsStatus routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: OrderItemNotificationsStatusRoutingResolveService;
  let service: OrderItemNotificationsStatusService;
  let resultOrderItemNotificationsStatus: IOrderItemNotificationsStatus | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(OrderItemNotificationsStatusRoutingResolveService);
    service = TestBed.inject(OrderItemNotificationsStatusService);
    resultOrderItemNotificationsStatus = undefined;
  });

  describe('resolve', () => {
    it('should return IOrderItemNotificationsStatus returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrderItemNotificationsStatus = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOrderItemNotificationsStatus).toEqual({ id: 123 });
    });

    it('should return new IOrderItemNotificationsStatus if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrderItemNotificationsStatus = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultOrderItemNotificationsStatus).toEqual(new OrderItemNotificationsStatus());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OrderItemNotificationsStatus })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrderItemNotificationsStatus = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOrderItemNotificationsStatus).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
