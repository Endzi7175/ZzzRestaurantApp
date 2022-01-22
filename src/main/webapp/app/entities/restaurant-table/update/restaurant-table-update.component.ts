import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRestaurantTable, RestaurantTable } from '../restaurant-table.model';
import { RestaurantTableService } from '../service/restaurant-table.service';

@Component({
  selector: 'jhi-restaurant-table-update',
  templateUrl: './restaurant-table-update.component.html',
})
export class RestaurantTableUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    tableNo: [],
    seatsNo: [],
    description: [],
    xPositionFrom: [],
    xPositionTo: [],
    yPositionFrom: [],
    yPositionTo: [],
  });

  constructor(
    protected restaurantTableService: RestaurantTableService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurantTable }) => {
      this.updateForm(restaurantTable);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restaurantTable = this.createFromForm();
    if (restaurantTable.id !== undefined) {
      this.subscribeToSaveResponse(this.restaurantTableService.update(restaurantTable));
    } else {
      this.subscribeToSaveResponse(this.restaurantTableService.create(restaurantTable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaurantTable>>): void {
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

  protected updateForm(restaurantTable: IRestaurantTable): void {
    this.editForm.patchValue({
      id: restaurantTable.id,
      tableNo: restaurantTable.tableNo,
      seatsNo: restaurantTable.seatsNo,
      description: restaurantTable.description,
      xPositionFrom: restaurantTable.xPositionFrom,
      xPositionTo: restaurantTable.xPositionTo,
      yPositionFrom: restaurantTable.yPositionFrom,
      yPositionTo: restaurantTable.yPositionTo,
    });
  }

  protected createFromForm(): IRestaurantTable {
    return {
      ...new RestaurantTable(),
      id: this.editForm.get(['id'])!.value,
      tableNo: this.editForm.get(['tableNo'])!.value,
      seatsNo: this.editForm.get(['seatsNo'])!.value,
      description: this.editForm.get(['description'])!.value,
      xPositionFrom: this.editForm.get(['xPositionFrom'])!.value,
      xPositionTo: this.editForm.get(['xPositionTo'])!.value,
      yPositionFrom: this.editForm.get(['yPositionFrom'])!.value,
      yPositionTo: this.editForm.get(['yPositionTo'])!.value,
    };
  }
}
