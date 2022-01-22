import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRestaurantOrderItem } from '../restaurant-order-item.model';
import { RestaurantOrderItemService } from '../service/restaurant-order-item.service';

@Component({
  templateUrl: './restaurant-order-item-delete-dialog.component.html',
})
export class RestaurantOrderItemDeleteDialogComponent {
  restaurantOrderItem?: IRestaurantOrderItem;

  constructor(protected restaurantOrderItemService: RestaurantOrderItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.restaurantOrderItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
