import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MenuItemService } from '../service/menu-item.service';
import { IMenuItem, MenuItem } from '../menu-item.model';
import { IPicture } from 'app/entities/picture/picture.model';
import { PictureService } from 'app/entities/picture/service/picture.service';
import { IIngredient } from 'app/entities/ingredient/ingredient.model';
import { IngredientService } from 'app/entities/ingredient/service/ingredient.service';
import { IMenu } from 'app/entities/menu/menu.model';
import { MenuService } from 'app/entities/menu/service/menu.service';

import { MenuItemUpdateComponent } from './menu-item-update.component';

describe('MenuItem Management Update Component', () => {
  let comp: MenuItemUpdateComponent;
  let fixture: ComponentFixture<MenuItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let menuItemService: MenuItemService;
  let pictureService: PictureService;
  let ingredientService: IngredientService;
  let menuService: MenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MenuItemUpdateComponent],
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
      .overrideTemplate(MenuItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MenuItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    menuItemService = TestBed.inject(MenuItemService);
    pictureService = TestBed.inject(PictureService);
    ingredientService = TestBed.inject(IngredientService);
    menuService = TestBed.inject(MenuService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call picture query and add missing value', () => {
      const menuItem: IMenuItem = { id: 456 };
      const picture: IPicture = { id: 4391 };
      menuItem.picture = picture;

      const pictureCollection: IPicture[] = [{ id: 68524 }];
      jest.spyOn(pictureService, 'query').mockReturnValue(of(new HttpResponse({ body: pictureCollection })));
      const expectedCollection: IPicture[] = [picture, ...pictureCollection];
      jest.spyOn(pictureService, 'addPictureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      expect(pictureService.query).toHaveBeenCalled();
      expect(pictureService.addPictureToCollectionIfMissing).toHaveBeenCalledWith(pictureCollection, picture);
      expect(comp.picturesCollection).toEqual(expectedCollection);
    });

    it('Should call Ingredient query and add missing value', () => {
      const menuItem: IMenuItem = { id: 456 };
      const ingredients: IIngredient[] = [{ id: 79488 }];
      menuItem.ingredients = ingredients;

      const ingredientCollection: IIngredient[] = [{ id: 56834 }];
      jest.spyOn(ingredientService, 'query').mockReturnValue(of(new HttpResponse({ body: ingredientCollection })));
      const additionalIngredients = [...ingredients];
      const expectedCollection: IIngredient[] = [...additionalIngredients, ...ingredientCollection];
      jest.spyOn(ingredientService, 'addIngredientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      expect(ingredientService.query).toHaveBeenCalled();
      expect(ingredientService.addIngredientToCollectionIfMissing).toHaveBeenCalledWith(ingredientCollection, ...additionalIngredients);
      expect(comp.ingredientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Menu query and add missing value', () => {
      const menuItem: IMenuItem = { id: 456 };
      const menu: IMenu = { id: 23584 };
      menuItem.menu = menu;

      const menuCollection: IMenu[] = [{ id: 4346 }];
      jest.spyOn(menuService, 'query').mockReturnValue(of(new HttpResponse({ body: menuCollection })));
      const additionalMenus = [menu];
      const expectedCollection: IMenu[] = [...additionalMenus, ...menuCollection];
      jest.spyOn(menuService, 'addMenuToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      expect(menuService.query).toHaveBeenCalled();
      expect(menuService.addMenuToCollectionIfMissing).toHaveBeenCalledWith(menuCollection, ...additionalMenus);
      expect(comp.menusSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const menuItem: IMenuItem = { id: 456 };
      const picture: IPicture = { id: 61760 };
      menuItem.picture = picture;
      const ingredients: IIngredient = { id: 30850 };
      menuItem.ingredients = [ingredients];
      const menu: IMenu = { id: 34567 };
      menuItem.menu = menu;

      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(menuItem));
      expect(comp.picturesCollection).toContain(picture);
      expect(comp.ingredientsSharedCollection).toContain(ingredients);
      expect(comp.menusSharedCollection).toContain(menu);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MenuItem>>();
      const menuItem = { id: 123 };
      jest.spyOn(menuItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: menuItem }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(menuItemService.update).toHaveBeenCalledWith(menuItem);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MenuItem>>();
      const menuItem = new MenuItem();
      jest.spyOn(menuItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: menuItem }));
      saveSubject.complete();

      // THEN
      expect(menuItemService.create).toHaveBeenCalledWith(menuItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MenuItem>>();
      const menuItem = { id: 123 };
      jest.spyOn(menuItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(menuItemService.update).toHaveBeenCalledWith(menuItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPictureById', () => {
      it('Should return tracked Picture primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPictureById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackIngredientById', () => {
      it('Should return tracked Ingredient primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIngredientById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackMenuById', () => {
      it('Should return tracked Menu primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMenuById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedIngredient', () => {
      it('Should return option if no Ingredient is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedIngredient(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Ingredient for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedIngredient(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Ingredient is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedIngredient(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
