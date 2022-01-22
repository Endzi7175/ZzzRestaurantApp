import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RestaurantOrderItemService } from '../service/restaurant-order-item.service';
import { IRestaurantOrderItem, RestaurantOrderItem } from '../restaurant-order-item.model';
import { IOrderItemNotificationsStatus } from 'app/entities/order-item-notifications-status/order-item-notifications-status.model';
import { OrderItemNotificationsStatusService } from 'app/entities/order-item-notifications-status/service/order-item-notifications-status.service';
import { IRestaurantOrder } from 'app/entities/restaurant-order/restaurant-order.model';
import { RestaurantOrderService } from 'app/entities/restaurant-order/service/restaurant-order.service';

import { RestaurantOrderItemUpdateComponent } from './restaurant-order-item-update.component';

describe('RestaurantOrderItem Management Update Component', () => {
  let comp: RestaurantOrderItemUpdateComponent;
  let fixture: ComponentFixture<RestaurantOrderItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let restaurantOrderItemService: RestaurantOrderItemService;
  let orderItemNotificationsStatusService: OrderItemNotificationsStatusService;
  let restaurantOrderService: RestaurantOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RestaurantOrderItemUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RestaurantOrderItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RestaurantOrderItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    restaurantOrderItemService = TestBed.inject(RestaurantOrderItemService);
    orderItemNotificationsStatusService = TestBed.inject(OrderItemNotificationsStatusService);
    restaurantOrderService = TestBed.inject(RestaurantOrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call orderItemStatus query and add missing value', () => {
      const restaurantOrderItem: IRestaurantOrderItem = { id: 456 };
      const orderItemStatus: IOrderItemNotificationsStatus = { id: 94901 };
      restaurantOrderItem.orderItemStatus = orderItemStatus;

      const orderItemStatusCollection: IOrderItemNotificationsStatus[] = [{ id: 3133 }];
      jest.spyOn(orderItemNotificationsStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemStatusCollection })));
      const expectedCollection: IOrderItemNotificationsStatus[] = [orderItemStatus, ...orderItemStatusCollection];
      jest
        .spyOn(orderItemNotificationsStatusService, 'addOrderItemNotificationsStatusToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ restaurantOrderItem });
      comp.ngOnInit();

      expect(orderItemNotificationsStatusService.query).toHaveBeenCalled();
      expect(orderItemNotificationsStatusService.addOrderItemNotificationsStatusToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemStatusCollection,
        orderItemStatus
      );
      expect(comp.orderItemStatusesCollection).toEqual(expectedCollection);
    });

    it('Should call RestaurantOrder query and add missing value', () => {
      const restaurantOrderItem: IRestaurantOrderItem = { id: 456 };
      const restaurantOrder: IRestaurantOrder = { id: 54370 };
      restaurantOrderItem.restaurantOrder = restaurantOrder;

      const restaurantOrderCollection: IRestaurantOrder[] = [{ id: 94146 }];
      jest.spyOn(restaurantOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: restaurantOrderCollection })));
      const additionalRestaurantOrders = [restaurantOrder];
      const expectedCollection: IRestaurantOrder[] = [...additionalRestaurantOrders, ...restaurantOrderCollection];
      jest.spyOn(restaurantOrderService, 'addRestaurantOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ restaurantOrderItem });
      comp.ngOnInit();

      expect(restaurantOrderService.query).toHaveBeenCalled();
      expect(restaurantOrderService.addRestaurantOrderToCollectionIfMissing).toHaveBeenCalledWith(
        restaurantOrderCollection,
        ...additionalRestaurantOrders
      );
      expect(comp.restaurantOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const restaurantOrderItem: IRestaurantOrderItem = { id: 456 };
      const orderItemStatus: IOrderItemNotificationsStatus = { id: 43761 };
      restaurantOrderItem.orderItemStatus = orderItemStatus;
      const restaurantOrder: IRestaurantOrder = { id: 99672 };
      restaurantOrderItem.restaurantOrder = restaurantOrder;

      activatedRoute.data = of({ restaurantOrderItem });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(restaurantOrderItem));
      expect(comp.orderItemStatusesCollection).toContain(orderItemStatus);
      expect(comp.restaurantOrdersSharedCollection).toContain(restaurantOrder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RestaurantOrderItem>>();
      const restaurantOrderItem = { id: 123 };
      jest.spyOn(restaurantOrderItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurantOrderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restaurantOrderItem }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(restaurantOrderItemService.update).toHaveBeenCalledWith(restaurantOrderItem);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RestaurantOrderItem>>();
      const restaurantOrderItem = new RestaurantOrderItem();
      jest.spyOn(restaurantOrderItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurantOrderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restaurantOrderItem }));
      saveSubject.complete();

      // THEN
      expect(restaurantOrderItemService.create).toHaveBeenCalledWith(restaurantOrderItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RestaurantOrderItem>>();
      const restaurantOrderItem = { id: 123 };
      jest.spyOn(restaurantOrderItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurantOrderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(restaurantOrderItemService.update).toHaveBeenCalledWith(restaurantOrderItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackOrderItemNotificationsStatusById', () => {
      it('Should return tracked OrderItemNotificationsStatus primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOrderItemNotificationsStatusById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackRestaurantOrderById', () => {
      it('Should return tracked RestaurantOrder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRestaurantOrderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
