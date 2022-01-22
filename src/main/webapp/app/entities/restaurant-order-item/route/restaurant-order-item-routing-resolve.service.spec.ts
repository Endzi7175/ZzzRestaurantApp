import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IRestaurantOrderItem, RestaurantOrderItem } from '../restaurant-order-item.model';
import { RestaurantOrderItemService } from '../service/restaurant-order-item.service';

import { RestaurantOrderItemRoutingResolveService } from './restaurant-order-item-routing-resolve.service';

describe('RestaurantOrderItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RestaurantOrderItemRoutingResolveService;
  let service: RestaurantOrderItemService;
  let resultRestaurantOrderItem: IRestaurantOrderItem | undefined;

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
    routingResolveService = TestBed.inject(RestaurantOrderItemRoutingResolveService);
    service = TestBed.inject(RestaurantOrderItemService);
    resultRestaurantOrderItem = undefined;
  });

  describe('resolve', () => {
    it('should return IRestaurantOrderItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRestaurantOrderItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRestaurantOrderItem).toEqual({ id: 123 });
    });

    it('should return new IRestaurantOrderItem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRestaurantOrderItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRestaurantOrderItem).toEqual(new RestaurantOrderItem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RestaurantOrderItem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRestaurantOrderItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRestaurantOrderItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
