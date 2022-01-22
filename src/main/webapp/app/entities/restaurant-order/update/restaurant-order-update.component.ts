import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRestaurantOrder, RestaurantOrder } from '../restaurant-order.model';
import { RestaurantOrderService } from '../service/restaurant-order.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IRestaurantTable } from 'app/entities/restaurant-table/restaurant-table.model';
import { RestaurantTableService } from 'app/entities/restaurant-table/service/restaurant-table.service';

@Component({
  selector: 'jhi-restaurant-order-update',
  templateUrl: './restaurant-order-update.component.html',
})
export class RestaurantOrderUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  restaurantTablesSharedCollection: IRestaurantTable[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    priceExcludingTax: [],
    priceIncludingTax: [],
    user: [],
    restaurantTable: [],
  });

  constructor(
    protected restaurantOrderService: RestaurantOrderService,
    protected userService: UserService,
    protected restaurantTableService: RestaurantTableService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurantOrder }) => {
      if (restaurantOrder.id === undefined) {
        const today = dayjs().startOf('day');
        restaurantOrder.date = today;
      }

      this.updateForm(restaurantOrder);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restaurantOrder = this.createFromForm();
    if (restaurantOrder.id !== undefined) {
      this.subscribeToSaveResponse(this.restaurantOrderService.update(restaurantOrder));
    } else {
      this.subscribeToSaveResponse(this.restaurantOrderService.create(restaurantOrder));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackRestaurantTableById(index: number, item: IRestaurantTable): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaurantOrder>>): void {
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

  protected updateForm(restaurantOrder: IRestaurantOrder): void {
    this.editForm.patchValue({
      id: restaurantOrder.id,
      date: restaurantOrder.date ? restaurantOrder.date.format(DATE_TIME_FORMAT) : null,
      priceExcludingTax: restaurantOrder.priceExcludingTax,
      priceIncludingTax: restaurantOrder.priceIncludingTax,
      user: restaurantOrder.user,
      restaurantTable: restaurantOrder.restaurantTable,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, restaurantOrder.user);
    this.restaurantTablesSharedCollection = this.restaurantTableService.addRestaurantTableToCollectionIfMissing(
      this.restaurantTablesSharedCollection,
      restaurantOrder.restaurantTable
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.restaurantTableService
      .query()
      .pipe(map((res: HttpResponse<IRestaurantTable[]>) => res.body ?? []))
      .pipe(
        map((restaurantTables: IRestaurantTable[]) =>
          this.restaurantTableService.addRestaurantTableToCollectionIfMissing(restaurantTables, this.editForm.get('restaurantTable')!.value)
        )
      )
      .subscribe((restaurantTables: IRestaurantTable[]) => (this.restaurantTablesSharedCollection = restaurantTables));
  }

  protected createFromForm(): IRestaurantOrder {
    return {
      ...new RestaurantOrder(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      priceExcludingTax: this.editForm.get(['priceExcludingTax'])!.value,
      priceIncludingTax: this.editForm.get(['priceIncludingTax'])!.value,
      user: this.editForm.get(['user'])!.value,
      restaurantTable: this.editForm.get(['restaurantTable'])!.value,
    };
  }
}
