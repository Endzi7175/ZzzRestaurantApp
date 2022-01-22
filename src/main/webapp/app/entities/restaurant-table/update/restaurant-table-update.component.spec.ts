import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RestaurantTableService } from '../service/restaurant-table.service';
import { IRestaurantTable, RestaurantTable } from '../restaurant-table.model';

import { RestaurantTableUpdateComponent } from './restaurant-table-update.component';

describe('RestaurantTable Management Update Component', () => {
  let comp: RestaurantTableUpdateComponent;
  let fixture: ComponentFixture<RestaurantTableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let restaurantTableService: RestaurantTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RestaurantTableUpdateComponent],
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
      .overrideTemplate(RestaurantTableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RestaurantTableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    restaurantTableService = TestBed.inject(RestaurantTableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const restaurantTable: IRestaurantTable = { id: 456 };

      activatedRoute.data = of({ restaurantTable });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(restaurantTable));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RestaurantTable>>();
      const restaurantTable = { id: 123 };
      jest.spyOn(restaurantTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurantTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restaurantTable }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(restaurantTableService.update).toHaveBeenCalledWith(restaurantTable);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RestaurantTable>>();
      const restaurantTable = new RestaurantTable();
      jest.spyOn(restaurantTableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurantTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restaurantTable }));
      saveSubject.complete();

      // THEN
      expect(restaurantTableService.create).toHaveBeenCalledWith(restaurantTable);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RestaurantTable>>();
      const restaurantTable = { id: 123 };
      jest.spyOn(restaurantTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurantTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(restaurantTableService.update).toHaveBeenCalledWith(restaurantTable);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
