import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RestaurantOrderService } from '../service/restaurant-order.service';
import { IRestaurantOrder, RestaurantOrder } from '../restaurant-order.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IRestaurantTable } from 'app/entities/restaurant-table/restaurant-table.model';
import { RestaurantTableService } from 'app/entities/restaurant-table/service/restaurant-table.service';

import { RestaurantOrderUpdateComponent } from './restaurant-order-update.component';

describe('RestaurantOrder Management Update Component', () => {
  let comp: RestaurantOrderUpdateComponent;
  let fixture: ComponentFixture<RestaurantOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let restaurantOrderService: RestaurantOrderService;
  let userService: UserService;
  let restaurantTableService: RestaurantTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RestaurantOrderUpdateComponent],
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
      .overrideTemplate(RestaurantOrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RestaurantOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    restaurantOrderService = TestBed.inject(RestaurantOrderService);
    userService = TestBed.inject(UserService);
    restaurantTableService = TestBed.inject(RestaurantTableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const restaurantOrder: IRestaurantOrder = { id: 456 };
      const user: IUser = { id: 88774 };
      restaurantOrder.user = user;

      const userCollection: IUser[] = [{ id: 94023 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ restaurantOrder });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call RestaurantTable query and add missing value', () => {
      const restaurantOrder: IRestaurantOrder = { id: 456 };
      const restaurantTable: IRestaurantTable = { id: 26670 };
      restaurantOrder.restaurantTable = restaurantTable;

      const restaurantTableCollection: IRestaurantTable[] = [{ id: 59146 }];
      jest.spyOn(restaurantTableService, 'query').mockReturnValue(of(new HttpResponse({ body: restaurantTableCollection })));
      const additionalRestaurantTables = [restaurantTable];
      const expectedCollection: IRestaurantTable[] = [...additionalRestaurantTables, ...restaurantTableCollection];
      jest.spyOn(restaurantTableService, 'addRestaurantTableToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ restaurantOrder });
      comp.ngOnInit();

      expect(restaurantTableService.query).toHaveBeenCalled();
      expect(restaurantTableService.addRestaurantTableToCollectionIfMissing).toHaveBeenCalledWith(
        restaurantTableCollection,
        ...additionalRestaurantTables
      );
      expect(comp.restaurantTablesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const restaurantOrder: IRestaurantOrder = { id: 456 };
      const user: IUser = { id: 35732 };
      restaurantOrder.user = user;
      const restaurantTable: IRestaurantTable = { id: 38340 };
      restaurantOrder.restaurantTable = restaurantTable;

      activatedRoute.data = of({ restaurantOrder });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(restaurantOrder));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.restaurantTablesSharedCollection).toContain(restaurantTable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RestaurantOrder>>();
      const restaurantOrder = { id: 123 };
      jest.spyOn(restaurantOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurantOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restaurantOrder }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(restaurantOrderService.update).toHaveBeenCalledWith(restaurantOrder);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RestaurantOrder>>();
      const restaurantOrder = new RestaurantOrder();
      jest.spyOn(restaurantOrderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurantOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restaurantOrder }));
      saveSubject.complete();

      // THEN
      expect(restaurantOrderService.create).toHaveBeenCalledWith(restaurantOrder);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RestaurantOrder>>();
      const restaurantOrder = { id: 123 };
      jest.spyOn(restaurantOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurantOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(restaurantOrderService.update).toHaveBeenCalledWith(restaurantOrder);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackRestaurantTableById', () => {
      it('Should return tracked RestaurantTable primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRestaurantTableById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
