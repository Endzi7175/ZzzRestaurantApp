import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrderItemNotificationsStatusService } from '../service/order-item-notifications-status.service';
import { IOrderItemNotificationsStatus, OrderItemNotificationsStatus } from '../order-item-notifications-status.model';

import { OrderItemNotificationsStatusUpdateComponent } from './order-item-notifications-status-update.component';

describe('OrderItemNotificationsStatus Management Update Component', () => {
  let comp: OrderItemNotificationsStatusUpdateComponent;
  let fixture: ComponentFixture<OrderItemNotificationsStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderItemNotificationsStatusService: OrderItemNotificationsStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrderItemNotificationsStatusUpdateComponent],
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
      .overrideTemplate(OrderItemNotificationsStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderItemNotificationsStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderItemNotificationsStatusService = TestBed.inject(OrderItemNotificationsStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const orderItemNotificationsStatus: IOrderItemNotificationsStatus = { id: 456 };

      activatedRoute.data = of({ orderItemNotificationsStatus });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(orderItemNotificationsStatus));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrderItemNotificationsStatus>>();
      const orderItemNotificationsStatus = { id: 123 };
      jest.spyOn(orderItemNotificationsStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItemNotificationsStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderItemNotificationsStatus }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderItemNotificationsStatusService.update).toHaveBeenCalledWith(orderItemNotificationsStatus);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrderItemNotificationsStatus>>();
      const orderItemNotificationsStatus = new OrderItemNotificationsStatus();
      jest.spyOn(orderItemNotificationsStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItemNotificationsStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderItemNotificationsStatus }));
      saveSubject.complete();

      // THEN
      expect(orderItemNotificationsStatusService.create).toHaveBeenCalledWith(orderItemNotificationsStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrderItemNotificationsStatus>>();
      const orderItemNotificationsStatus = { id: 123 };
      jest.spyOn(orderItemNotificationsStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItemNotificationsStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderItemNotificationsStatusService.update).toHaveBeenCalledWith(orderItemNotificationsStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
