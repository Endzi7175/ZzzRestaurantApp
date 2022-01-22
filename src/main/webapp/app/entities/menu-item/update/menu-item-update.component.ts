import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMenuItem, MenuItem } from '../menu-item.model';
import { MenuItemService } from '../service/menu-item.service';
import { IPicture } from 'app/entities/picture/picture.model';
import { PictureService } from 'app/entities/picture/service/picture.service';
import { IIngredient } from 'app/entities/ingredient/ingredient.model';
import { IngredientService } from 'app/entities/ingredient/service/ingredient.service';
import { IMenu } from 'app/entities/menu/menu.model';
import { MenuService } from 'app/entities/menu/service/menu.service';
import { MenuItemType } from 'app/entities/enumerations/menu-item-type.model';

@Component({
  selector: 'jhi-menu-item-update',
  templateUrl: './menu-item-update.component.html',
})
export class MenuItemUpdateComponent implements OnInit {
  isSaving = false;
  menuItemTypeValues = Object.keys(MenuItemType);

  picturesCollection: IPicture[] = [];
  ingredientsSharedCollection: IIngredient[] = [];
  menusSharedCollection: IMenu[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    price: [],
    prepareTime: [],
    type: [],
    picture: [],
    ingredients: [],
    menu: [],
  });

  constructor(
    protected menuItemService: MenuItemService,
    protected pictureService: PictureService,
    protected ingredientService: IngredientService,
    protected menuService: MenuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ menuItem }) => {
      this.updateForm(menuItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const menuItem = this.createFromForm();
    if (menuItem.id !== undefined) {
      this.subscribeToSaveResponse(this.menuItemService.update(menuItem));
    } else {
      this.subscribeToSaveResponse(this.menuItemService.create(menuItem));
    }
  }

  trackPictureById(index: number, item: IPicture): number {
    return item.id!;
  }

  trackIngredientById(index: number, item: IIngredient): number {
    return item.id!;
  }

  trackMenuById(index: number, item: IMenu): number {
    return item.id!;
  }

  getSelectedIngredient(option: IIngredient, selectedVals?: IIngredient[]): IIngredient {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMenuItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(menuItem: IMenuItem): void {
    this.editForm.patchValue({
      id: menuItem.id,
      name: menuItem.name,
      description: menuItem.description,
      price: menuItem.price,
      prepareTime: menuItem.prepareTime,
      type: menuItem.type,
      picture: menuItem.picture,
      ingredients: menuItem.ingredients,
      menu: menuItem.menu,
    });

    this.picturesCollection = this.pictureService.addPictureToCollectionIfMissing(this.picturesCollection, menuItem.picture);
    this.ingredientsSharedCollection = this.ingredientService.addIngredientToCollectionIfMissing(
      this.ingredientsSharedCollection,
      ...(menuItem.ingredients ?? [])
    );
    this.menusSharedCollection = this.menuService.addMenuToCollectionIfMissing(this.menusSharedCollection, menuItem.menu);
  }

  protected loadRelationshipsOptions(): void {
    this.pictureService
      .query({ filter: 'menuitem-is-null' })
      .pipe(map((res: HttpResponse<IPicture[]>) => res.body ?? []))
      .pipe(
        map((pictures: IPicture[]) => this.pictureService.addPictureToCollectionIfMissing(pictures, this.editForm.get('picture')!.value))
      )
      .subscribe((pictures: IPicture[]) => (this.picturesCollection = pictures));

    this.ingredientService
      .query()
      .pipe(map((res: HttpResponse<IIngredient[]>) => res.body ?? []))
      .pipe(
        map((ingredients: IIngredient[]) =>
          this.ingredientService.addIngredientToCollectionIfMissing(ingredients, ...(this.editForm.get('ingredients')!.value ?? []))
        )
      )
      .subscribe((ingredients: IIngredient[]) => (this.ingredientsSharedCollection = ingredients));

    this.menuService
      .query()
      .pipe(map((res: HttpResponse<IMenu[]>) => res.body ?? []))
      .pipe(map((menus: IMenu[]) => this.menuService.addMenuToCollectionIfMissing(menus, this.editForm.get('menu')!.value)))
      .subscribe((menus: IMenu[]) => (this.menusSharedCollection = menus));
  }

  protected createFromForm(): IMenuItem {
    return {
      ...new MenuItem(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      price: this.editForm.get(['price'])!.value,
      prepareTime: this.editForm.get(['prepareTime'])!.value,
      type: this.editForm.get(['type'])!.value,
      picture: this.editForm.get(['picture'])!.value,
      ingredients: this.editForm.get(['ingredients'])!.value,
      menu: this.editForm.get(['menu'])!.value,
    };
  }
}
