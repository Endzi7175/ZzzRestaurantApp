import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RestaurantOrderItemComponent } from './list/restaurant-order-item.component';
import { RestaurantOrderItemDetailComponent } from './detail/restaurant-order-item-detail.component';
import { RestaurantOrderItemUpdateComponent } from './update/restaurant-order-item-update.component';
import { RestaurantOrderItemDeleteDialogComponent } from './delete/restaurant-order-item-delete-dialog.component';
import { RestaurantOrderItemRoutingModule } from './route/restaurant-order-item-routing.module';

@NgModule({
  imports: [SharedModule, RestaurantOrderItemRoutingModule],
  declarations: [
    RestaurantOrderItemComponent,
    RestaurantOrderItemDetailComponent,
    RestaurantOrderItemUpdateComponent,
    RestaurantOrderItemDeleteDialogComponent,
  ],
  entryComponents: [RestaurantOrderItemDeleteDialogComponent],
})
export class RestaurantOrderItemModule {}
