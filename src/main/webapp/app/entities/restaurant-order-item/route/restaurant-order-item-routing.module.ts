import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RestaurantOrderItemComponent } from '../list/restaurant-order-item.component';
import { RestaurantOrderItemDetailComponent } from '../detail/restaurant-order-item-detail.component';
import { RestaurantOrderItemUpdateComponent } from '../update/restaurant-order-item-update.component';
import { RestaurantOrderItemRoutingResolveService } from './restaurant-order-item-routing-resolve.service';

const restaurantOrderItemRoute: Routes = [
  {
    path: '',
    component: RestaurantOrderItemComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RestaurantOrderItemDetailComponent,
    resolve: {
      restaurantOrderItem: RestaurantOrderItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RestaurantOrderItemUpdateComponent,
    resolve: {
      restaurantOrderItem: RestaurantOrderItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RestaurantOrderItemUpdateComponent,
    resolve: {
      restaurantOrderItem: RestaurantOrderItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(restaurantOrderItemRoute)],
  exports: [RouterModule],
})
export class RestaurantOrderItemRoutingModule {}
